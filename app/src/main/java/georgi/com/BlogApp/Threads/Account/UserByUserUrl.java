package georgi.com.BlogApp.Threads.Account;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;

import static georgi.com.BlogApp.Configs.ServerURLs.DEFAULT_USER_IMG;
import static georgi.com.BlogApp.Configs.ServerURLs.SERVER_URL;
import static georgi.com.BlogApp.Configs.ServerURLs.USER_IMAGES_URL;

// This thread is sending request to server with
// some user account url and set the UI thread
// elements with the response from the server.
// (response contains : profile picture name, first name, last name, email and user url).
public class UserByUserUrl extends AsyncTask<String, Void, JSONObject>{

    private Context context;

    private ImageView profilePic;
    private TextView fullName, email;

    public UserByUserUrl(Context context, ImageView profilePic, TextView fullName, TextView email) {
        this.context = context;
        this.profilePic = profilePic;
        this.fullName = fullName;
        this.email = email;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        try {

            // Creating request to the server. strings[0] : userUrl from User object.
            HttpRequest httpRequest = new HttpRequest(SERVER_URL + "/" + strings[0],
                    new PreferencesHelper(context).getCookie(), "GET");

            // Sending the request and handling the response.
            String response = httpRequest.sendTheRequest();

            // Converting the String response to JSONObject.
            return new JSONObject(response);

        } catch(IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(JSONObject response) {

        // Checking if there is response.
        if(response == null) return;

        // Checking if the response is returning User object.
        if(response.has("userUrl")) {

            try {

                String profPicUrl = response.getString("profile_picture");
                // "no" means that there is no picture set to the User's account.
                // so we are setting the profPicUrl to default profile picture url.
                if(profPicUrl.equals("no")) profPicUrl = DEFAULT_USER_IMG;

                // else we are changing the url to the needed.
                else profPicUrl = USER_IMAGES_URL + response.getString("userUrl") + "/" + profPicUrl;

                // Loading the picture directly from above url.
                Glide.with(context)
                        .load(profPicUrl)
                        .override(400, 400)
                        .into(profilePic);

                // Setting the TextViews with the response from the server.
                String fullName = response.getString("firstName") + " " + response.getString("lastName");
                this.fullName.setText(fullName);

                email.setText(response.getString("email"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
