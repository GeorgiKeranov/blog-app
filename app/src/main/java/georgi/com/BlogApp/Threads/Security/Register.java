package georgi.com.BlogApp.Threads.Security;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import georgi.com.BlogApp.Activities.Account.LoginActivity;
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;

import static georgi.com.BlogApp.Configs.ServerURLs.REGISTER_URL;


// This thread is registering new user to the server.
public class Register extends AsyncTask<String, Void, JSONObject> {

    private String TAG = getClass().getSimpleName();

    private Context context;

    // These editTexts are used when server return response that
    // username or email are already used in the database.
    private EditText username, email;

    public Register(Context context, EditText username, EditText email){
        this.context = context;
        this.username = username;
        this.email = email;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        try {

            // Creating request.
            HttpRequest httpRequest = new HttpRequest(REGISTER_URL,
                    new PreferencesHelper(context).getCookie(), "POST");

            // Adding requested fields to the request.
            httpRequest.addStringField("firstName", strings[0]);
            httpRequest.addStringField("lastName", strings[1]);
            httpRequest.addStringField("email", strings[2]);
            httpRequest.addStringField("username", strings[3]);
            httpRequest.addStringField("password", strings[4]);

            // Sending the request and getting the response.
            String response = httpRequest.sendTheRequest();

            // Converting response to JSONObject and returning it.
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

            // Checking if server has returned error.
            if(error){

                // Getting the error message from the server's response.
                final String error_msg = response.getString("error_msg");

                // Creating error AlertDialog.
                builder.setTitle("Error");
                builder.setMessage(error_msg);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                        // If message equals "Email is already taken" text
                        // from email element is deleted.
                        if(error_msg.equals("Email is already taken."))
                            email.setText("");

                        // If message not equals above message then
                        // text from username element is deleted.
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

                        // When the "OK" button is clicked
                        // LoginActivity is started and previous activities are cleared.
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        dialogInterface.dismiss();

                        context.startActivity(intent);
                    }
                });
            }

            AlertDialog dialog = builder.create();

            // Showing the AlertDialog.
            dialog.show();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
