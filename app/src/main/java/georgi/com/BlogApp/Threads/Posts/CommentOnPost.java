package georgi.com.BlogApp.Threads.Posts;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.IOException;

import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.ErrorHandler;

import static georgi.com.BlogApp.Configs.ServerURLs.POSTS_URL;


// This thread is handling comments by the user and sending them to the server.
public class CommentOnPost extends AsyncTask<String, Void, ErrorHandler>{

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
    protected ErrorHandler doInBackground(String... strings) {

        try {

            // Sending POST request to the server.
            HttpRequest httpRequest =
                    new HttpRequest(POSTS_URL + "/" + id + "/comment",
                            new PreferencesHelper(context).getCookie(), "POST");

            // Adding the actual comment to the request.
            httpRequest.addStringField("comment", strings[0]);

            // Sending request and getting the response.
            String response = httpRequest.sendTheRequest();

            Gson gson = new Gson();
            // Converting the JSON response to ErrorHandler object.
            return gson.fromJson(response, ErrorHandler.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(ErrorHandler errorHandler) {

        if(errorHandler.getError()) {
            // Creating AlertDialog with the error_msg from the server for message.
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Error").setMessage(errorHandler.getError_msg());

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            builder.create().show();
        }

        else {
            // Creating new thread to get the comments from the server
            // and set them in recyclerView.
            CommentsOnPost commentsOnPost =
                    new CommentsOnPost(context, commentsRecyclerView);
            commentsOnPost.execute(id);
        }

    }
}
