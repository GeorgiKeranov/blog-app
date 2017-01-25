package georgi.com.BlogApp.Threads;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import georgi.com.BlogApp.Helpers.PreferencesHelper;

import static georgi.com.BlogApp.Configs.ServerURLs.SERVER_URL;


public class GetUserInfoThread extends AsyncTask<Void,Void,JSONObject>{

    private Context context;

    public GetUserInfoThread(Context context) {
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {

        JSONObject response = null;
        PreferencesHelper helper = new PreferencesHelper(context);
        String cookie = helper.getCookie();

        try {

            URL url = new URL(SERVER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Cookie", cookie);

            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(isr);

            String line = "";
            StringBuilder builder = new StringBuilder();

            while((line = reader.readLine()) != null){
                builder.append(line);
            }

            reader.close();

            response = new JSONObject(builder.toString());

            return response;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return response;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_LONG).show();
    }

}
