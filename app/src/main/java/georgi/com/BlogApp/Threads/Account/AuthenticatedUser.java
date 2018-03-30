package georgi.com.BlogApp.Threads.Account;

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

import java.io.IOException;

import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.User;

import static georgi.com.BlogApp.Configs.ServerURLs.ACCOUNT_URL;

// This thread is sending GET request to get the authenticated user's
// details like first name , last name  ... And setting them to the UI thread.
public class AuthenticatedUser extends AsyncTask<Void, Void, User>{

    private Context context;

    private ProgressBar userImageProgressBar;
    private ImageView userImage;
    private TextView firstName, lastName, email;

    public AuthenticatedUser(Context context, ProgressBar userImageProgressBar, ImageView userImage,
                             TextView firstName, TextView lastName, TextView email) {

        this.context = context;

        this.userImageProgressBar = userImageProgressBar;
        this.userImage = userImage;

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    protected User doInBackground(Void... voids) {

        try {
            // Sending GET request to the server for authenticated user details.
            HttpRequest httpRequest = new HttpRequest(ACCOUNT_URL,
                            new PreferencesHelper(context).getCookie(), "GET");

            // Getting the response.
            String response = httpRequest.sendTheRequest();

            Gson gson = new Gson();

            // Converting the json response to User object.
            return gson.fromJson(response, User.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(User user) {

        // Default width and height is 140 px.
        // This size is used when we use that thread
        // only for setting user's images.
        int size = 140;

        if(firstName != null && lastName != null && email != null) {
            // If firstName, lastName and email are not null
            // It is setting the UI elements with the response info.
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            email.setText(user.getEmail());

            // Setting the width and height of the image to 600px.
            size = 600;
        }

        // Loading the image from url to the ImageView.
        Glide.with(context)
                .load(user.getProfPicUrl())
                .override(size, size)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        userImageProgressBar.setVisibility(View.GONE);
                        userImage.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        userImageProgressBar.setVisibility(View.GONE);
                        userImage.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(userImage);
    }
}
