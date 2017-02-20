package georgi.com.BlogApp.Threads.Posts;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;

import georgi.com.BlogApp.Activities.Posts.PostActivity;
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.ErrorHandler;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static georgi.com.BlogApp.Configs.ServerURLs.DELETE_COMMENT_URL;

public class DeleteComment extends AsyncTask<Long, Void, ErrorHandler> {

    private Context context;

    // That is the id of post from who the comment is.
    private Long postId;

    public DeleteComment(Context context, Long postId) {
        this.context = context;
        this.postId = postId;
    }

    @Override
    protected ErrorHandler doInBackground(Long... longs) {

        try {

            // Creating the request.
            HttpRequest httpRequest = new HttpRequest(DELETE_COMMENT_URL,
                    new PreferencesHelper(context).getCookie(), "POST");

            // longs[0] is the id for the comment to delete.
            httpRequest.addStringField("commentId", "" + longs[0]);

            // Sending the request and getting the response.
            String response = httpRequest.sendTheRequest();

            Gson gson = new Gson();
            // Converting the JSON response to ErrorHandler.
            return gson.fromJson(response, ErrorHandler.class);

        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ErrorHandler errorHandler) {

        if(errorHandler.getError()) {
            // TODO handle the error msg.
        }

        else {
            // Starting new PostActivity of the same Post and clearing
            // the previous activity that is with the deleted comment.
            Intent intent = new Intent(context, PostActivity.class);
            intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("post_id", postId);
            context.startActivity(intent);
        }
    }
}
