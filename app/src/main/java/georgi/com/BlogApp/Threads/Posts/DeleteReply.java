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

import static georgi.com.BlogApp.Configs.ServerURLs.DELETE_REPLY_URL;

public class DeleteReply extends AsyncTask<Void, Void, ErrorHandler> {

    private Context context;

    private Long replyId;

    public DeleteReply(Context context, Long replyId) {
        this.context = context;
        this.replyId = replyId;
    }

    @Override
    protected ErrorHandler doInBackground(Void... voids) {

        try {
            // Creating the request to delete reply with POST method.
            HttpRequest httpRequest = new HttpRequest(DELETE_REPLY_URL,
                    new PreferencesHelper(context).getCookie(), "POST");

            // Adding the id of the reply to delete in the httpRequest.
            httpRequest.addStringField("replyId", "" + replyId);

            // Sending the request and getting the response.
            String response = httpRequest.sendTheRequest();
            Log.d("ERROR", response);

            Gson gson = new Gson();
            // Converting the JSON to ErrorHandler object and returning it.
            return gson.fromJson(response, ErrorHandler.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ErrorHandler(false);
    }

    @Override
    protected void onPostExecute(ErrorHandler errorHandler) {

        if(errorHandler.getError()) {
            Log.d("ERROR", errorHandler.getError_msg());
        }

        else {
            ((PostActivity) context).deleteReplyById(replyId);
        }
    }
}
