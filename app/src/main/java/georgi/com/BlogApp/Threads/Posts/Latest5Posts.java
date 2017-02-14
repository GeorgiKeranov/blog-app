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
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Post;


// This thread is sending request to server for the latest 5 posts
// and set them to the recyclerView.
public class Latest5Posts extends AsyncTask<String, Void, List<Post>>{

    private String TAG = getClass().getSimpleName();

    private Context context;

    private RecyclerView recyclerView;

    public Latest5Posts(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    protected List<Post> doInBackground(String... strings) {

        try {

            // Creating the request.
            // strings[0] is the url to send that request.
            HttpRequest httpRequest = new HttpRequest(strings[0],
                    new PreferencesHelper(context).getCookie(), "GET");

            // Sending the request and getting the response.
            String response = httpRequest.sendTheRequest();

            // Converting the response to JSONArray
            JSONArray jsonResponse = new JSONArray(response);

            return convertToListOfObjects(jsonResponse);

        } catch(IOException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    // This method is used to convert JSONarray to list of Post objects.
    private List<Post> convertToListOfObjects(JSONArray jsonArray) throws JSONException {

        List<Post> postsResponse = new ArrayList<>();

        for(int i = 0; i<jsonArray.length(); i++) {

            JSONObject curPost = jsonArray.getJSONObject(i);

            Post newPost = new Post();
            newPost.setId(curPost.getLong("id"));
            newPost.setTitle(curPost.getString("title"));
            newPost.setDescription(curPost.getString("description"));
            newPost.setIcon(curPost.getString("icon"));
            newPost.setDate(curPost.getString("date"));

            postsResponse.add(newPost);
        }

        return  postsResponse;
    }

    @Override
    protected void onPostExecute(List<Post> posts) {

        // Getting the adapter from the recyclerView
        PostsAdapter postsAdapter = (PostsAdapter) recyclerView.getAdapter();

        // Getting the posts from the postAdapter
        List<Post> oldPosts = postsAdapter.getPosts();

        // Deleting the old posts.
        oldPosts.clear();

        // Adding the new posts to the old posts reference.
        for(Post post : posts) {
            oldPosts.add(post);
        }

        // And notifying the adapter that data is changed.
        postsAdapter.notifyDataSetChanged();
    }
}
