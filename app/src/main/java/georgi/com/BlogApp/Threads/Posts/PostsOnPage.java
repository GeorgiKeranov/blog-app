package georgi.com.BlogApp.Threads.Posts;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import georgi.com.BlogApp.Activities.Account.ViewOtherUserActivity;
import georgi.com.BlogApp.Activities.Posts.LatestPostsActivity;
import georgi.com.BlogApp.Activities.Posts.YourPostsActivity;
import georgi.com.BlogApp.Adapters.PostsAdapter;
import georgi.com.BlogApp.Adapters.YourPostsAdapter;
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Comment;
import georgi.com.BlogApp.POJO.Post;


// This thread is sending request to server for the latest 5 posts
// and set them to the recyclerView.
public class PostsOnPage extends AsyncTask<String, Void, List<Post>>{

    private String TAG = getClass().getSimpleName();

    private Context context;

    private RecyclerView recyclerView;

    private String activityName;

    public PostsOnPage(Context context, RecyclerView recyclerView, String activityName) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.activityName = activityName;
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

            Gson gson = new Gson();
            // Converting the response from JSON object to array of posts.
            Post[] posts = gson.fromJson(response, Post[].class);

            // Converting array of posts to List of posts and returning it.
            return new ArrayList<>(Arrays.asList(posts));

        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Post> newPosts) {

        if(newPosts.size() > 0) {

            // Getting the adapter from the recyclerView
            switch(this.activityName) {
                case "ViewOtherUserActivity":
                case "LatestPostsActivity":
                    PostsAdapter postsAdapter = (PostsAdapter) recyclerView.getAdapter();

                    // Getting the posts from the postAdapter
                    List<Post> posts = postsAdapter.getPosts();

                    // Adding the new posts to the old posts reference.
                    for (Post post : newPosts) {
                        posts.add(post);
                    }

                    // And notifying the adapter that data is changed.
                    postsAdapter.notifyDataSetChanged();
                    break;

                case "YourPostsActivity":
                    YourPostsAdapter yourPostsAdapter = (YourPostsAdapter) recyclerView.getAdapter();

                    // Getting the posts from the postAdapter
                    List<Post> yourPosts = yourPostsAdapter.getPosts();

                    // Adding the new posts to the old posts reference.
                    for (Post post : newPosts) {
                        yourPosts.add(post);
                    }

                    // And notifying the adapter that data is changed.
                    yourPostsAdapter.notifyDataSetChanged();
                    break;
            }

        } else {

            switch(this.activityName) {

                case "LatestPostsActivity": ((LatestPostsActivity) context).morePages = false; break;
                case "YourPostsActivity": ((YourPostsActivity) context).morePages = false; break;
                case "ViewOtherUserActivity": ((ViewOtherUserActivity) context).morePages = false; break;
            }

        }

    }
}
