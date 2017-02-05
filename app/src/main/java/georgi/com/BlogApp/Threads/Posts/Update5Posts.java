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

public class Update5Posts extends AsyncTask<String, Void, List<Post>> {

    private Context context;
    private RecyclerView recyclerView;

    private String whatAdapter;

    public Update5Posts(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }


    @Override
    protected List<Post> doInBackground(String... strings) {

        if(strings[0].equals(UPDATE_5POSTS_URL)) whatAdapter = "PostsAdapter";
        if(strings[0].equals(UPDATE_USER_5POSTS_URL)) whatAdapter = "YourPostsAdapter";

        try {
            HttpRequest httpRequest =
                    new HttpRequest(strings[0], new PreferencesHelper(context).getCookie(), "GET");

            httpRequest.addStringField("postsBeforeId", strings[1]);

            String response = httpRequest.sendTheRequest();

            JSONArray jsonArray = new JSONArray(response);


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

        if(whatAdapter.equals("PostsAdapter")) {
            PostsAdapter postsAdapter = (PostsAdapter) recyclerView.getAdapter();
            List<Post> oldPosts = postsAdapter.getPosts();

            for(Post post : posts) {
                oldPosts.add(post);
            }

            postsAdapter.notifyDataSetChanged();
        }

        else if(whatAdapter.equals("YourPostsAdapter")) {
            YourPostsAdapter yourPostsAdapter = (YourPostsAdapter) recyclerView.getAdapter();
            List<Post> oldPosts = yourPostsAdapter.getPosts();

            for(Post post : posts) {
                oldPosts.add(post);
            }

            yourPostsAdapter.notifyDataSetChanged();
        }
    }

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
