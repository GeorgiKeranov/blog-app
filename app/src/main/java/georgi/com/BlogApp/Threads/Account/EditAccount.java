package georgi.com.BlogApp.Threads.Account;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import georgi.com.BlogApp.Helpers.HttpMultipartRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.ErrorHandler;

import static georgi.com.BlogApp.Configs.ServerURLs.ACCOUNT_URL;

public class EditAccount extends AsyncTask<String, Void, ErrorHandler> {

    private Context context;

    private Uri filePath;

    public EditAccount(Context context, Uri filePath) {
        this.context = context;
        this.filePath = filePath;
    }

    @Override
    protected ErrorHandler doInBackground(String... strings) {

        try {

            // Creating the request.
            HttpMultipartRequest request =
                    new HttpMultipartRequest(ACCOUNT_URL, new PreferencesHelper(context).getCookie());

            // If there is file path it adds picture field with the file from that path.
            if(filePath != null)
                request.addFileField("picture", new File(getRealLocationFromUri(filePath)));

            // Adding the other fields to the request.
            request.addStringField("firstName", strings[0]);
            request.addStringField("lastName", strings[1]);
            request.addStringField("email", strings[2]);

            // strings[3] is the new password. If it is not empty add it to request.
            if(!strings[3].equals("")) request.addStringField("newPassword", strings[3]);

            request.addStringField("currPassword", strings[4]);

            // Sending the request and getting the response.
            String response = request.sendTheRequest();

            Gson gson = new Gson();

            // Converting the response from the server to ErrorHandler object.
            return gson.fromJson(response, ErrorHandler.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(ErrorHandler errorHandler) {

        // Checking if the response from the server have returned error.
        if(errorHandler.getError()) {

            // Creating AlertDialog with the error_msg from the server.
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Error");
            builder.setMessage(errorHandler.getError_msg());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            builder.create().show();
        }

        // Else if there is not error closing the activity.
        else {
            ((Activity) context).finish();
        }

    }


    // This method is getting the real file location for file in the phone from it Uri.
    private String getRealLocationFromUri(Uri uri) {

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();

        int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

        String realLocation =  cursor.getString(index);

        cursor.close();

        return realLocation;
    }
}
