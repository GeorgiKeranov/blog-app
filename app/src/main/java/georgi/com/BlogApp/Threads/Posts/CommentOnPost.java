package georgi.com.BlogApp.Threads.Posts;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;

import georgi.com.BlogApp.Adapters.CommentsAdapter;
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;

import static georgi.com.BlogApp.Configs.ServerURLs.POST_URL;


// This thread is handling comments by the user and sending them to the server.
public class CommentOnPost extends AsyncTask<String, Void, Void>{

    private Context context;

    // This is the recyclerView for the comments and replies.
    private RecyclerView commentsRecyclerView;

    // This is the id of the post that is clicked.
    private Long id;

    public CommentOnPost(Context context, Long id, RecyclerView recyclerView) {
        this.context = context;
        this.id = id;
        this.commentsRecyclerView = recyclerView;
    }

    @Override
    protected Void doInBackground(String... strings) {

        try {

            // Sending POST request to the server.
            HttpRequest httpRequest =
                    new HttpRequest(POST_URL + id + "/comment",
                            new PreferencesHelper(context).getCookie(), "POST");

            // Adding the actual comment to the request.
            httpRequest.addStringField("comment", strings[0]);

            // Sending request and getting the response.
            String response = httpRequest.sendTheRequest();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {

        // Creating new thread to get the comments from the server
        // and set them in recyclerView.
        CommentsOnPost commentsOnPost =
                new CommentsOnPost(context, commentsRecyclerView);
        commentsOnPost.execute(id);

    }
}
