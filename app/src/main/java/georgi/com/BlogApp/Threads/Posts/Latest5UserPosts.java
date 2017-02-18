package georgi.com.BlogApp.Threads.Posts;

import android.content.Context;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import georgi.com.BlogApp.Adapters.PostsAdapter;
import georgi.com.BlogApp.Adapters.YourPostsAdapter;
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Post;

import static georgi.com.BlogApp.Configs.ServerURLs.AUTH_USER_LATEST5_POSTS;


// This thread is sending request to server for the latest 5 posts
// created by the authenticated user and set them to the postsRecycler.
public class Latest5UserPosts extends AsyncTask<String, Void, List<Post>>{

    private Context context;

    // This String is used to determinate what kind of adapter to use in PostExecute method.
    private String whatAdapter;

    private RecyclerView postsRecycler;

    public Latest5UserPosts(Context context, RecyclerView postsRecycler){
        this.context = context;
        this.postsRecycler = postsRecycler;
    }

    @Override
    protected List<Post> doInBackground(String... strings) {

        // Setting the whatAdapter value.
        if(strings[0].equals(AUTH_USER_LATEST5_POSTS)) whatAdapter = "YourPostsAdapter";
        else whatAdapter = "PostsAdapter";

        try {

            // Creating the request.
            HttpRequest normalRequest = new HttpRequest(strings[0], // string[0] - url for the request.
                            new PreferencesHelper(context).getCookie(), "GET");

            // Sending the request and getting the response.
            String response = normalRequest.sendTheRequest();

            Gson gson = new Gson();
            // Converting the response from JSON object to array of posts.
            Post[] posts = gson.fromJson(response, Post[].class);

            // Converting array of posts to List of posts.
            return new ArrayList<>(Arrays.asList(posts));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Post> posts) {

        // Checks the value of whatAdapter.

        if (whatAdapter.equals("PostsAdapter")) {
            // Getting the adapter from yourPostsRecyclerView.
            PostsAdapter postsAdapter = (PostsAdapter) postsRecycler.getAdapter();

            // Getting the oldPosts in the adapter.
            List<Post> oldPosts = postsAdapter.getPosts();

            // Deleting the old posts.
            oldPosts.clear();

            // Adding the new Post objects to the old list.
            for (Post post : posts) oldPosts.add(post);

            // Notifying the postsAdapter that data is changed.
            postsAdapter.notifyDataSetChanged();
        }

        else if (whatAdapter.equals("YourPostsAdapter")) {
            // Getting the adapter from yourPostsRecyclerView.
            YourPostsAdapter yourPostsAdapter = (YourPostsAdapter) postsRecycler.getAdapter();

            // Getting the oldPosts in the adapter.
            List<Post> oldPosts = yourPostsAdapter.getPosts();

            // Deleting the old posts.
            oldPosts.clear();

            // Adding the new Post objects to the old list.
            for (Post post : posts) oldPosts.add(post);

            // Notifying the postsAdapter that data is changed.
            yourPostsAdapter.notifyDataSetChanged();
        }

    }
}
