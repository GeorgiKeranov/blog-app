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

import georgi.com.BlogApp.Adapters.CommentsAdapter;
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Comment;
import georgi.com.BlogApp.POJO.Reply;
import georgi.com.BlogApp.POJO.User;

import static georgi.com.BlogApp.Configs.ServerURLs.POST_URL;


// This thread is sending request to get comments for some post
// and handling comments and replies then setting them in recyclerView.
public class CommentsOnPost extends AsyncTask<Long, Void, List<Comment>> {

    private Context context;

    // This recyclerView is used for the comments.
    private RecyclerView commentsRecyclerView;

    public CommentsOnPost(Context context, RecyclerView commentsRecyclerView) {
        this.context = context;
        this.commentsRecyclerView = commentsRecyclerView;
    }

    @Override
    protected List<Comment> doInBackground(Long... longs) {

        try {

            // Sending the request to get comments on post with id - longs[0].
            HttpRequest httpRequest =
                    new HttpRequest(POST_URL + longs[0] + "/comments",
                            new PreferencesHelper(context).getCookie(), "GET");

            // Sending the request and getting the response.
            String response = httpRequest.sendTheRequest();

            Gson gson = new Gson();
            // Converting the response from JSON object to List of comments and returning it.
            Comment[] comments = gson.fromJson(response, Comment[].class);

            return new ArrayList<>(Arrays.asList(comments));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Comment> comments) {

        // Creating the adapter for the list of comments.
        CommentsAdapter commentsAdapter = new CommentsAdapter(context, comments);

        // Setting the adapter to the recyclerView.
        commentsRecyclerView.setAdapter(commentsAdapter);
    }
}
