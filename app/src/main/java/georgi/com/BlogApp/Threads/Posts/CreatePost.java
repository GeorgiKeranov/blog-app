package georgi.com.BlogApp.Threads.Posts;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

import georgi.com.BlogApp.Helpers.HttpMultipartRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;

import static georgi.com.BlogApp.Configs.ServerURLs.CREATE_POST_URL;

public class CreatePost extends AsyncTask<String, Void, Void> {

    private Context context;
    private Uri uri;

    public CreatePost(Context context, Uri uri) {
        this.context = context;
        this.uri = uri;
    }

    @Override
    protected Void doInBackground(String... strings) {

        try {

            HttpMultipartRequest multipart = new HttpMultipartRequest(CREATE_POST_URL, new PreferencesHelper(context).getCookie());
            multipart.addFileField("picture", new File(getRealPathFromURI(uri)));
            multipart.addStringField("title", strings[0]);
            multipart.addStringField("description", strings[1]);
            String response = multipart.sendTheRequest();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        //TODO alert dialog if it has errors.
        //TODO alert dialog for successful.

        ((Activity)context).finish();
    }

    private String getRealPathFromURI(Uri contentURI) {

        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);

        cursor.moveToFirst();
        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

        String result = cursor.getString(index);
        cursor.close();

        return result;
    }
}
