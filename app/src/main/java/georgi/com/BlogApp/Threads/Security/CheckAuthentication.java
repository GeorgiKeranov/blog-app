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


// This thread is checking if the user is authenticated in the server
// By getting saved value of cookie from SharedPreferences and
// sending request to the server to check the cookie.
public class CheckAuthentication extends AsyncTask<Void, Void, Boolean>{

    private String TAG = getClass().getSimpleName();

    private Context context;

    public CheckAuthentication(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        // Checks if there is a cookie.
        String cookie = new PreferencesHelper(context).getCookie();
        if(cookie == null || cookie.equals("NO-COOKIE")) return false;

        try {

            // Creating request to the server.
            HttpRequest httpRequest = new HttpRequest(AUTHENTICATION_URL, cookie, "GET");

            // Sending the request and getting the response from the server.
            String response = httpRequest.sendTheRequest();

            // Converting the response to JSONObject.
            JSONObject jsonResponse = new JSONObject(response);

            // Returning boolean from the response.
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
            // Intent for LatestPostsActivity if authentication is successful.
            intent = new Intent(context, LatestPostsActivity.class);
        }

        else {
            // Intent for LoginActivity if authentication is unsuccessful.
            intent = new Intent(context, LoginActivity.class);
        }

        // Removing previous activities.
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        context.startActivity(intent);
    }
}
