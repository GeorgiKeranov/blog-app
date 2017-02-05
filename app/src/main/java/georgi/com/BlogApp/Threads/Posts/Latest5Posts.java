package georgi.com.BlogApp.Threads.Posts;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import georgi.com.BlogApp.Activities.Posts.PostActivity;
import georgi.com.BlogApp.Adapters.PostsAdapter;
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Post;


public class Latest5Posts extends AsyncTask<String, Void, List<Post>>{

    private String TAG = getClass().getSimpleName();

    private Context context;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    public Latest5Posts(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    protected List<Post> doInBackground(String... strings) {
        try {

            HttpRequest httpRequest = new HttpRequest(strings[0],
                    new PreferencesHelper(context).getCookie(), "GET");
            String response = httpRequest.sendTheRequest();

            JSONArray jsonResponse = new JSONArray(response);

            return convertToListOfObjects(jsonResponse);

        } catch(IOException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Post> posts) {

        PostsAdapter postsAdapter = (PostsAdapter) recyclerView.getAdapter();
        List<Post> oldPosts = postsAdapter.getPosts();

        for(Post post : posts) {
            oldPosts.add(post);
        }

        postsAdapter.notifyDataSetChanged();
    }

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
}
