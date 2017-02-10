package georgi.com.BlogApp.Threads.Posts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import georgi.com.BlogApp.Helpers.HttpMultipartRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;

import static georgi.com.BlogApp.Configs.ServerURLs.POST_URL;


public class UpdatePost extends AsyncTask<String, Void, JSONObject> {

    private Context context;

    private Uri fileLocation;

    public UpdatePost(Context context, Uri fileLocation) {
        this.context = context;
        this.fileLocation = fileLocation;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        try {

            HttpMultipartRequest multipartRequest =
                    new HttpMultipartRequest(POST_URL + strings[0],
                            new PreferencesHelper(context).getCookie());


            if(fileLocation != null)
                multipartRequest.addFileField("picture",
                        new File(generateLocationFromUri(fileLocation)));

            multipartRequest.addStringField("title", strings[1]);
            multipartRequest.addStringField("description", strings[2]);


            JSONObject jsonObject = new JSONObject(multipartRequest.sendTheRequest());

            return jsonObject;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject response) {

        try {

            boolean error = response.getBoolean("error");

            if (error) {
                //TODO dialog and add error msg in server side
            } else {
                ((Activity) context).finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String generateLocationFromUri(Uri uri) {

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();

        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

        String location = cursor.getString(index);
        cursor.close();

        return location;
    }

}
