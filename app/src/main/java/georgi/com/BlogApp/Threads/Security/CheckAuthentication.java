package georgi.com.BlogApp.Threads.Security;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import georgi.com.BlogApp.Activities.Account.LoginActivity;
import georgi.com.BlogApp.Activities.Posts.LatestPostsActivity;
import georgi.com.BlogApp.Helpers.HttpRequest;
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
        if(cookie == null || cookie.equals("NO-COOKIE")) return false;

        try {

            HttpRequest httpRequest = new HttpRequest(AUTHENTICATION_URL, cookie, "GET");
            String response = httpRequest.sendTheRequest();

            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getBoolean("authenticated");

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
