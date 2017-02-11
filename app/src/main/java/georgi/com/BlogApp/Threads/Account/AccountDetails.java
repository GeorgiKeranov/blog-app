package georgi.com.BlogApp.Threads.Account;

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

import static georgi.com.BlogApp.Configs.ServerURLs.ACCOUNT_URL;
import static georgi.com.BlogApp.Configs.ServerURLs.DEFAULT_USER_IMG;
import static georgi.com.BlogApp.Configs.ServerURLs.USER_IMAGES_URL;

// This thread is sending GET request to get the authenticated user's
// details like first name , last name  ... And setting them to the UI thread.
public class AccountDetails extends AsyncTask<Void, Void, String[]>{

    private Context context;

    private ImageView profile;
    private TextView firstName, lastName, email;

    public AccountDetails(Context context,
                          ImageView profile,
                          TextView firstName,
                          TextView lastName,
                          TextView email) {

        this.context = context;
        this.profile = profile;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    protected String[] doInBackground(Void... voids) {

        try {

            // Sending GET request to the server for authenticated user details.
            HttpRequest httpRequest = new HttpRequest(ACCOUNT_URL,
                            new PreferencesHelper(context).getCookie(), "GET");

            // Getting the response.
            String response = httpRequest.sendTheRequest();

            // Converting the response to json object.
            JSONObject jsonResponse = new JSONObject(response);

            // Converting the json object in String array.
            String[] information = new String[4];

            information[0] = jsonResponse.getString("firstName");
            information[1] = jsonResponse.getString("lastName");
            information[2] = jsonResponse.getString("email");
            information[3] = jsonResponse.getString("profile_picture");

            // If profile picture equals "no" means that there is no profile picture
            // and it is changing it to the default image url.
            if(information[3].equals("no"))
                information[3] = DEFAULT_USER_IMG;

            // If there is profile picture it will create the url that is needed.
            else
                information[3] = USER_IMAGES_URL +
                        jsonResponse.getString("userUrl") + "/" + information[3];

            return information;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String[] info) {

        // Default width and height is 140 px.
        // This size is used when we use that thread
        // only for setting user's images.
        int size = 140;

        if(firstName != null && lastName != null && email != null) {
            // If firstName, lastName and email are not null
            // It is setting the UI elements with the response info.
            firstName.setText(info[0]);
            lastName.setText(info[1]);
            email.setText(info[2]);

            // Setting the width and height of the image to 600px.
            size = 600;
        }

        // Loading the image from url to the ImageView.
        Glide.with(context)
                .load(info[3])
                .override(size, size)
                .into(profile);
    }
}
