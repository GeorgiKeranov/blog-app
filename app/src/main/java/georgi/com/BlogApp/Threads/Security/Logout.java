package georgi.com.BlogApp.Threads.Security;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import georgi.com.BlogApp.Activities.MainActivity;
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;

import static georgi.com.BlogApp.Configs.ServerURLs.LOGOUT_URL;


// This thread logout the authenticated user
// by sending request to server with the saved cookie
// and deleting the cookie from SharedPreferences.
public class Logout extends AsyncTask<Void, Void, Boolean>{

    private Context context;

    private PreferencesHelper prefHelper;

    public Logout(Context context) {
        this.context = context;
        prefHelper = new PreferencesHelper(context);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {

            // Creating the request.
            HttpRequest normalRequest =
                    new HttpRequest(LOGOUT_URL, prefHelper.getCookie(), "POST");

            // Sending the request and getting the response.
            String response = normalRequest.sendTheRequest();

            // Converting response to JSONObject.
            JSONObject jsonResponse = new JSONObject(response);

            // Getting boolean from the response and returning it.
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

        if(!authenticated) {

            // Deleting the last saved cookie.
            prefHelper.setCookie("NO-COOKIE");

            // Starting MainActivity and clearing previous activities.
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }

    }
}
