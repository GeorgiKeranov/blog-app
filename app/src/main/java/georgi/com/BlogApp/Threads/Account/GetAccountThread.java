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

import georgi.com.BlogApp.Helpers.PreferencesHelper;

import static georgi.com.BlogApp.Configs.ServerURLs.ACCOUNT_URL;
import static georgi.com.BlogApp.Configs.ServerURLs.DEFAULT_USER_IMG;
import static georgi.com.BlogApp.Configs.ServerURLs.USER_IMAGES_URL;

public class GetAccountThread extends AsyncTask<Void, Void, String[]>{

    private Context context;

    private ImageView profile;
    private TextView firstName, lastName, email;

    public GetAccountThread(Context context,
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
            URL url = new URL(ACCOUNT_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Cookie", new PreferencesHelper(context).getCookie());

            InputStreamReader is = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(is);

            String line;
            StringBuilder builder = new StringBuilder();

            while((line = reader.readLine()) != null) {
                builder.append(line);
            }

            reader.close();
            is.close();

            JSONObject response = new JSONObject(builder.toString());

            Log.d("RESPONSE", response.toString());

            String[] information = new String[4];

            information[0] = response.getString("firstName");
            information[1] = response.getString("lastName");
            information[2] = response.getString("email");
            information[3] = response.getString("profile_picture");

            if(information[3].equals("no"))
                information[3] = DEFAULT_USER_IMG;

            else
                information[3] = USER_IMAGES_URL +
                        response.getString("userUrl") +
                        "/" + response.getString("profile_picture");

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

        firstName.setText(info[0]);
        firstName.setVisibility(View.VISIBLE);

        lastName.setText(info[1]);
        lastName.setVisibility(View.VISIBLE);

        email.setText(info[2]);
        email.setVisibility(View.VISIBLE);

        Glide.with(context)
                .load(info[3])
                .override(600, 600)
                .into(profile);

        profile.setVisibility(View.VISIBLE);
    }
}
