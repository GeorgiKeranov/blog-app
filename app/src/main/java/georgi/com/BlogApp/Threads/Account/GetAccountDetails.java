package georgi.com.BlogApp.Threads.Account;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;

import static georgi.com.BlogApp.Configs.ServerURLs.ACCOUNT_URL;
import static georgi.com.BlogApp.Configs.ServerURLs.DEFAULT_USER_IMG;
import static georgi.com.BlogApp.Configs.ServerURLs.USER_IMAGES_URL;

public class GetAccountDetails extends AsyncTask<Void, Void, String[]>{

    private Context context;

    private ImageView profile;
    private TextView firstName, lastName, email;

    public GetAccountDetails(Context context,
                             ImageView profile,
                             TextView firstName,
                             TextView lastName,
                             TextView email) {

        this.profile = profile;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.context = context;
    }

    @Override
    protected String[] doInBackground(Void... voids) {

        try {

            HttpRequest httpRequest = new HttpRequest(ACCOUNT_URL,
                            new PreferencesHelper(context).getCookie(), "GET");
            String response = httpRequest.sendTheRequest();

            JSONObject jsonResponse = new JSONObject(response);

            String[] information = new String[4];

            information[0] = jsonResponse.getString("firstName");
            information[1] = jsonResponse.getString("lastName");
            information[2] = jsonResponse.getString("email");
            information[3] = jsonResponse.getString("profile_picture");

            if(information[3].equals("no"))
                information[3] = DEFAULT_USER_IMG;

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

        int size = 140;

        if(firstName != null && lastName != null && email != null) {
            firstName.setText(info[0]);
            lastName.setText(info[1]);
            email.setText(info[2]);

            size = 600;
        }

        Glide.with(context)
                .load(info[3])
                .override(size, size)
                .into(profile);
    }
}
