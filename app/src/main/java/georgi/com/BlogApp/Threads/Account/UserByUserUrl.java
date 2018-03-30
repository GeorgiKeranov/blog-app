package georgi.com.BlogApp.Threads.Account;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.User;

import static georgi.com.BlogApp.Configs.ServerURLs.DEFAULT_USER_IMG;
import static georgi.com.BlogApp.Configs.ServerURLs.SERVER_URL;
import static georgi.com.BlogApp.Configs.ServerURLs.USER_IMAGES_URL;

// This thread is sending request to server with
// some user account url and set the UI thread
// elements with the response from the server.
// (response contains : profile picture name, first name, last name, email and user url).
public class UserByUserUrl extends AsyncTask<String, Void, User>{

    private Context context;

    private ProgressBar profilePicProgressBar;
    private ImageView profilePic;
    private TextView fullName, email;

    public UserByUserUrl(Context context, ProgressBar profilePicProgressBar, ImageView profilePic,
                         TextView fullName, TextView email) {

        this.context = context;
        this.profilePicProgressBar = profilePicProgressBar;
        this.profilePic = profilePic;
        this.fullName = fullName;
        this.email = email;
    }

    @Override
    protected User doInBackground(String... strings) {

        try {
            // Creating request to the server. strings[0] : userUrl from User object.
            HttpRequest httpRequest = new HttpRequest(SERVER_URL + "/" + strings[0],
                    new PreferencesHelper(context).getCookie(), "GET");

            // Sending the request and handling the response.
            String response = httpRequest.sendTheRequest();

            Gson gson = new Gson();

            // Converting the response(JSON) to User object.
            return gson.fromJson(response, User.class);

        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(User user) {

        // Checking if there is not user.
        if(user == null) return;

        // Loading the picture directly from above url.
        Glide.with(context)
                .load(user.getProfPicUrl())
                .override(400, 400)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        profilePicProgressBar.setVisibility(View.GONE);
                        profilePic.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        profilePicProgressBar.setVisibility(View.GONE);
                        profilePic.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(profilePic);

        // Setting the TextViews with the User object details.
        fullName.setText(user.getFullName());
        email.setText(user.getEmail());
    }
}
