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

public class DeleteMyPost extends AsyncTask<Long, Void, Boolean> {

    private Context context;

    public DeleteMyPost(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Long... id) {

        try {

            // Creating the request.
            HttpRequest httpRequest = new HttpRequest(POSTS_URL + id[0] + "/delete",
                    new PreferencesHelper(context).getCookie(), "POST");

            // Sending the request and getting the response.
            String response = httpRequest.sendTheRequest();

            // Converting the response to JSONObject.
            JSONObject resp = new JSONObject(response);

            return resp.getBoolean("deleted");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return false;
    }


    @Override
    protected void onPostExecute(Boolean deleted) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if(deleted) {
            builder.setTitle("Successful!");
            builder.setMessage("Your post is successfully deleted.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent intent = new Intent(context, YourPostsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);

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
