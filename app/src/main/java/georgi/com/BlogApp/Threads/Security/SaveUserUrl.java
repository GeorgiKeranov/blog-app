package georgi.com.BlogApp.Threads.Security;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;

import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.User;

import static georgi.com.BlogApp.Configs.ServerURLs.ACCOUNT_URL;

public class SaveUserUrl implements Runnable {

    private PreferencesHelper preferencesHelper;

    public SaveUserUrl(Context context) {
        this.preferencesHelper = new PreferencesHelper(context);
    }

    @Override
    public void run() {

        try {
            // Creating request to the server.
            HttpRequest httpRequest = new HttpRequest(ACCOUNT_URL,
                    preferencesHelper.getCookie(), "GET");

            // Sending the request and getting the response.
            String response = httpRequest.sendTheRequest();

            Gson gson = new Gson();
            // Converting the JSON response to User object.
            User userReturned = gson.fromJson(response, User.class);

            // Saving the userUrl in SharedPreferences.
            preferencesHelper.setCustomKeyValue("userUrl", userReturned.getUserUrl());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
