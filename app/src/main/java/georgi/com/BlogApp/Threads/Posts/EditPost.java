package georgi.com.BlogApp.Threads.Posts;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import georgi.com.BlogApp.Helpers.HttpMultipartRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;

import static georgi.com.BlogApp.Configs.ServerURLs.POST_URL;


// This thread is sending new data for existing post to server
// and server is changing this post with the given data.
public class EditPost extends AsyncTask<String, Void, Boolean> {

    // This variable is for debugging.
    private String TAG = getClass().getSimpleName();

    private Context context;

    // This is the fileLocation for picture if the user is changing post's picture.
    private Uri fileLocation;

    public EditPost(Context context, Uri fileLocation) {
        this.context = context;
        this.fileLocation = fileLocation;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        try {

            // Creating a multipart request.
            HttpMultipartRequest multipartRequest =
                    // strings[0] - id of the post that will be edited.
                    new HttpMultipartRequest(POST_URL + strings[0],
                            new PreferencesHelper(context).getCookie());

            // Adding the params/files to the request.

            // Checking if new image is selected.
            if(fileLocation != null)
                multipartRequest.addFileField("picture",
                        new File(generateLocationFromUri(fileLocation)));

            multipartRequest.addStringField("title", strings[1]);
            multipartRequest.addStringField("description", strings[2]);

            String response = multipartRequest.sendTheRequest();

            if(response.equals("Successful")) return true;
            else return false;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Boolean isSucc) {

        // Checking if the server side response have error.
        if (!isSucc) {

                /*// Creating alert dialog with the error message from the server.
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Error");
                builder.setMessage();
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();*/


        } else {

            ((Activity) context).finish();
        }

    }


    // This method is getting the real file location from the uri.
    private String generateLocationFromUri(Uri uri) {

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();

        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

        String location = cursor.getString(index);
        cursor.close();

        return location;
    }

}
