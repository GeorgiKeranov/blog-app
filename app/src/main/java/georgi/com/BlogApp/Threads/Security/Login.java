package georgi.com.BlogApp.Threads.Security;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import georgi.com.BlogApp.Activities.Posts.LatestPostsActivity;
import georgi.com.BlogApp.Helpers.PreferencesHelper;

import static georgi.com.BlogApp.Configs.ServerURLs.LOGIN_URL;


// This thread sends username and password to the server
// and saving the cookie if the user is authenticated.
public class Login extends AsyncTask<String, Void, String>{

    private Context context;
    private PreferencesHelper preferencesHelper;

    public Login(Context context) {
        this.context = context;
        this.preferencesHelper = new PreferencesHelper(context);
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            URL url = new URL(LOGIN_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Writing the params to the request.
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write("username=" + strings[0] + "&" + "password=" + strings[1]);
            writer.flush();
            writer.close();

            // Reading the response from the server.
            InputStreamReader is = new InputStreamReader(conn.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(is);

            JSONObject response = new JSONObject(reader.readLine());

            reader.close();
            is.close();

            String cookie = null;

            // Getting the response from the server.
            if(response.getBoolean("authenticated")){

                String cookies = conn.getHeaderField("set-cookie");
                String[] cookiesArray = cookies.split(";");

                // Setting the cookie to returned from server "set-cookie".
                cookie = cookiesArray[0];
            }

            return cookie;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override // TODO MAKE loading circle.
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String cookie) {

        if(cookie == null) {

            // Creating invalid credentials dialog.
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Error");
            builder.setMessage("Invalid Credentials");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();
        }

        else {

            // Cookie is stored in SharedPreferences.
            preferencesHelper.setCookie(cookie);

            // Starting LatestPostsActivity and clearing previous activities.
            Intent homeActivity = new Intent(context, LatestPostsActivity.class);
            homeActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(homeActivity);
        }

    }
}
