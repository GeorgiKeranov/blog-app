package georgi.com.BlogApp.Threads.Posts;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import georgi.com.BlogApp.Adapters.PostsAdapter;
import georgi.com.BlogApp.Adapters.YourPostsAdapter;
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Post;

import static georgi.com.BlogApp.Configs.ServerURLs.UPDATE_5POSTS_URL;
import static georgi.com.BlogApp.Configs.ServerURLs.UPDATE_USER_5POSTS_URL;


// This thread is updating with 5 new posts the RecyclerView.
public class Update5Posts extends AsyncTask<String, Void, List<Post>> {

    private Context context;
    private RecyclerView postsRecyclerView;

    // This String is used to determinate what kind of adapter to use in PostExecute method.
    private String whatAdapter;

    public Update5Posts(Context context, RecyclerView postsRecyclerView) {
        this.context = context;
        this.postsRecyclerView = postsRecyclerView;
    }


    @Override
    protected List<Post> doInBackground(String... strings) {

        // Setting the whatAdapter value.
        if(strings[0].equals(UPDATE_5POSTS_URL)) whatAdapter = "PostsAdapter";
        if(strings[0].equals(UPDATE_USER_5POSTS_URL)) whatAdapter = "YourPostsAdapter";

        try {

            // Sending the request.
            HttpRequest httpRequest =
                    new HttpRequest(strings[0], new PreferencesHelper(context).getCookie(), "GET");

            // Adding the id of the last downloaded post.
            httpRequest.addStringField("postsBeforeId", strings[1]);

            // Sending the request and getting the response from the server.
            String response = httpRequest.sendTheRequest();

            // Converting the response to JSONArray.
            JSONArray jsonArray = new JSONArray(response);

            // Converting the JSONArray to List of Post objects and returning it.
            return convertToListOfObjects(jsonArray);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Post> posts) {

        // Checks the value of whatAdapter.

        if(whatAdapter.equals("PostsAdapter")) {
            // Getting the adapter from recyclerView.
            PostsAdapter postsAdapter = (PostsAdapter) postsRecyclerView.getAdapter();
            // Getting the oldPosts in the adapter.
            List<Post> oldPosts = postsAdapter.getPosts();

            // Adding the new Post objects to the old list.
            for(Post post : posts) {
                oldPosts.add(post);
            }

            // Notifying the postsAdapter that data is changed.
            postsAdapter.notifyDataSetChanged();
        }

        else if(whatAdapter.equals("YourPostsAdapter")) {
            // Getting the adapter from recyclerView.
            YourPostsAdapter yourPostsAdapter = (YourPostsAdapter) postsRecyclerView.getAdapter();
            // Getting the oldPosts in the adapter.
            List<Post> oldPosts = yourPostsAdapter.getPosts();

            // Adding the new Post objects to the old list.
            for(Post post : posts) {
                oldPosts.add(post);
            }

            // Notifying the postsAdapter that data is changed.
            yourPostsAdapter.notifyDataSetChanged();
        }
    }


    // This method is converting JSONArray to List of Posts.
    private List<Post> convertToListOfObjects(JSONArray jsonArray) throws JSONException {

        List<Post> postsResponse = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject curPost = jsonArray.getJSONObject(i);

            Post newPost = new Post();
            newPost.setId(curPost.getLong("id"));
            newPost.setTitle(curPost.getString("title"));
            newPost.setDescription(curPost.getString("description"));
            newPost.setIcon(curPost.getString("icon"));
            newPost.setDate(curPost.getString("date"));

            postsResponse.add(newPost);
        }

        return postsResponse;

    }
}
