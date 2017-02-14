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

import georgi.com.BlogApp.Adapters.YourPostsAdapter;
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Post;

import static georgi.com.BlogApp.Configs.ServerURLs.LATEST_USER_POSTS_URL;


// This thread is sending request to server for the latest 5 posts
// created by the authenticated user and set them to the yourPostsRecycler.
public class Latest5UserPosts extends AsyncTask<Void, Void, List<Post>>{

    private Context context;

    private RecyclerView yourPostsRecycler;

    public Latest5UserPosts(Context context, RecyclerView yourPostsRecycler){
        this.context = context;
        this.yourPostsRecycler = yourPostsRecycler;
    }

    @Override
    protected List<Post> doInBackground(Void... voids) {

        try {

            // Creating the request.
            HttpRequest normalRequest =
                    new HttpRequest(LATEST_USER_POSTS_URL,
                            new PreferencesHelper(context).getCookie(),
                            "POST");

            // Sending the request and getting the response.
            String response = normalRequest.sendTheRequest();

            // Converting the response to JSONArray.
            JSONArray responseObj = new JSONArray(response);

            // Converting JSONArray to List of Post objects.
            return convertToListOfObjects(responseObj);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(List<Post> posts) {

        // Getting the adapter from yourPostsRecyclerView.
        YourPostsAdapter adapter = (YourPostsAdapter) yourPostsRecycler.getAdapter();

        // Getting the old Post list from the adapter.
        List<Post> oldPosts = adapter.getPosts();

        // Deleting the old posts.
        oldPosts.clear();

        // Adding the new posts in oldPosts reference.
        for(Post post : posts) {
            oldPosts.add(post);
        }

        // Notifying the adapter that the data is changed.
        adapter.notifyDataSetChanged();
    }

    // This method is used to convert JSONArray to List of post objects.
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
