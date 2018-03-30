package georgi.com.BlogApp.Threads.Posts;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import georgi.com.BlogApp.Activities.Posts.YourPostsActivity;
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;

import static georgi.com.BlogApp.Configs.ServerURLs.POSTS_URL;

public class DeleteMyPost extends AsyncTask<Void, Void, Boolean> {

    private Context context;

    private Long postId;

    public DeleteMyPost(Context context, Long postId) {
        this.context = context;
        this.postId = postId;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {

            // Creating the request.
            HttpRequest httpRequest = new HttpRequest(POSTS_URL + "/" + postId + "/delete",
                    new PreferencesHelper(context).getCookie(), "POST");

            // Sending the request and getting the response.
            String response = httpRequest.sendTheRequest();

            // Converting the response to JSONObject.
            JSONObject resp = new JSONObject(response);

            return resp.getBoolean("error");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return false;
    }


    @Override
    protected void onPostExecute(Boolean error) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if(!error) {
            builder.setTitle("Successful!");
            builder.setMessage("Your post is successfully deleted.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    ((YourPostsActivity) context).deletePostById(postId);

                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }

        else {
            builder.setTitle("Error!");
            builder.setMessage("Error please try again.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            builder.create().show();
        }
    }

}
