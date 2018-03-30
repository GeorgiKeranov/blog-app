package georgi.com.BlogApp.Threads.Posts;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import georgi.com.BlogApp.Activities.Posts.PostActivity;
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.ErrorHandler;

import static georgi.com.BlogApp.Configs.ServerURLs.POSTS_URL;

public class ReplyOnComment extends AsyncTask<String, Void, ErrorHandler> {

    private Context context;

    // The id of the post on who we will reply.
    private Long postId;

    public ReplyOnComment(Context context, Long postId) {
        this.context = context;
        this.postId = postId;
    }

    @Override
    protected ErrorHandler doInBackground(String... strings) {

        try {
            // Needed url to reply to some comment : /rest/posts/{HERE ID OF THE POST}/reply .
            HttpRequest httpRequest = new HttpRequest(POSTS_URL + "/" + postId + "/reply",
                    new PreferencesHelper(context).getCookie(), "POST");

            // strings[0] : Text that will be in the new reply.
            httpRequest.addStringField("reply", strings[0]);
            // strings[1] : Id of the comment that we will reply.
            httpRequest.addStringField("commentIdToReply", strings[1]);

            // Sending the request and getting the response.
            String response = httpRequest.sendTheRequest();

            Gson gson = new Gson();
            // Converting JSON response to ErrorHandler object and returning it.
            return gson.fromJson(response, ErrorHandler.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ErrorHandler errorHandler) {

        if(errorHandler.getError()) {
            // TODO AlertDialog.
        }

        else {
            // Starting new PostActivity of the same Post and clearing
            // the previous activity that is without the new reply.
            Intent intent = new Intent(context, PostActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("post_id", postId);
            context.startActivity(intent);
        }

    }
}
