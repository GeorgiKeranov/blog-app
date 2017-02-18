package georgi.com.BlogApp.Threads.Posts;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import georgi.com.BlogApp.Helpers.HttpMultipartRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.ErrorHandler;

import static georgi.com.BlogApp.Configs.ServerURLs.CREATE_POST_URL;

// This thread is sending multipart POST request to the server
// with image of post, title and comment to create a post.
public class CreatePost extends AsyncTask<String, Void, ErrorHandler> {

    private Context context;
    private Uri uri;

    public CreatePost(Context context, Uri uri) {
        this.context = context;
        this.uri = uri;
    }

    @Override
    protected ErrorHandler doInBackground(String... strings) {

        try {

            // Creating the multipart request.
            HttpMultipartRequest multipart = new HttpMultipartRequest(CREATE_POST_URL, new PreferencesHelper(context).getCookie());

            // Checking if there is a picture uri.
            if(uri != null) {
                multipart.addFileField("picture", new File(getRealPathFromURI(uri)));
            }

            // Sending the other params.
            multipart.addStringField("title", strings[0]);
            multipart.addStringField("description", strings[1]);

            // Sending the request and getting the response.
            String response = multipart.sendTheRequest();

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

        // Checking if the server response have error.
        if (errorHandler.getError()) {

            // Creating alert dialog with the error message from the server.
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Error");
            builder.setMessage(errorHandler.getError_msg());
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();
        }

        // If there is not error closing the activity.
        else ((Activity) context).finish();
    }

    // This method is used to get the real file path in
    // the phone's storage by Uri of the file.
    private String getRealPathFromURI(Uri contentURI) {

        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);

        cursor.moveToFirst();
        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

        String result = cursor.getString(index);
        cursor.close();

        return result;
    }
}
