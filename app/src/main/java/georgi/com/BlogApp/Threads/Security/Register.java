package georgi.com.BlogApp.Threads.Security;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import georgi.com.BlogApp.Activities.Account.LoginActivity;
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;

import static georgi.com.BlogApp.Configs.ServerURLs.REGISTER_URL;


public class Register extends AsyncTask<String, Void, JSONObject> {

    private String TAG = getClass().getSimpleName();

    private Context context;

    private EditText username, email;

    public Register(Context context, EditText username, EditText email){
        this.context = context;
        this.username = username;
        this.email = email;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        try {

            HttpRequest httpRequest = new HttpRequest(REGISTER_URL,
                    new PreferencesHelper(context).getCookie(), "POST");

            httpRequest.addStringField("firstName", strings[0]);
            httpRequest.addStringField("lastName", strings[1]);
            httpRequest.addStringField("email", strings[2]);
            httpRequest.addStringField("username", strings[3]);
            httpRequest.addStringField("password", strings[4]);

            String response = httpRequest.sendTheRequest();

            return new JSONObject(response);

        } catch(JSONException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        try {
            boolean error = response.getBoolean("error");

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            if(error){
                final String error_msg = response.getString("error_msg");

                builder.setTitle("Error");
                builder.setMessage(error_msg);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        if(error_msg.equals("Email is already taken.")) email.setText("");
                        else username.setText("");
                    }
                });
            }

            else {
                builder.setTitle("Successful");
                builder.setMessage("You have been registered successful!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        dialogInterface.dismiss();

                        context.startActivity(intent);
                    }
                });
            }

            AlertDialog dialog = builder.create();
            dialog.show();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
