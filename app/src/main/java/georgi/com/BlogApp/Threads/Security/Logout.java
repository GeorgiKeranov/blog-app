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

            HttpRequest normalRequest =
                    new HttpRequest(LOGOUT_URL, prefHelper.getCookie(), "POST");

            String response = normalRequest.sendTheRequest();

            JSONObject jsonResponse = new JSONObject(response);

            boolean isLogout = !(jsonResponse.getBoolean("authenticated"));

            if(isLogout) return true;
            else return false;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean loggedOut) {

        if(loggedOut) {

            prefHelper.setCookie("NO-COOKIE");

            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }

        else {
            //TODO
        }

    }
}
