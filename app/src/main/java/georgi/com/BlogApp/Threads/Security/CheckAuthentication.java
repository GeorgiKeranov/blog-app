package georgi.com.BlogApp.Threads.Security;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import georgi.com.BlogApp.Activities.Account.LoginActivity;
import georgi.com.BlogApp.Activities.Posts.LatestPostsActivity;
import georgi.com.BlogApp.Helpers.PreferencesHelper;

import static georgi.com.BlogApp.Configs.ServerURLs.AUTHENTICATION_URL;

public class CheckAuthentication extends AsyncTask<Void, Void, Boolean>{

    private String TAG = getClass().getSimpleName();

    private Context context;

    public CheckAuthentication(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        String cookie = new PreferencesHelper(context).getCookie();
        if(cookie == null) return false;

        try {

            URL url = new URL(AUTHENTICATION_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Cookie", cookie);

            InputStreamReader is = new InputStreamReader(conn.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(is);

            JSONObject response = new JSONObject(reader.readLine());

            reader.close();
            is.close();

            return response.getBoolean("authenticated");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    protected void onPostExecute(Boolean authenticated) {

        Intent intent;

        if(authenticated) {
            intent = new Intent(context, LatestPostsActivity.class);
        }

        else {
            intent = new Intent(context, LoginActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
