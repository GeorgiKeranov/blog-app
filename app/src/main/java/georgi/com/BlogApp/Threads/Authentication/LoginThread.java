package georgi.com.BlogApp.Threads.Authentication;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.Threads.GetUserInfoThread;

import static georgi.com.BlogApp.Configs.ServerURLs.LOGIN_URL;

public class LoginThread extends AsyncTask<String, Void, String>{

    private Context context;

    public LoginThread(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            URL url = new URL(LOGIN_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write("username=" + strings[0] + "&" + "password=" + strings[1]);
            writer.flush();
            writer.close();

            InputStreamReader is = new InputStreamReader(conn.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(is);

            JSONObject response = new JSONObject(reader.readLine());

            reader.close();
            is.close();

            if(response.getBoolean("authenticated")){
                String cookies = conn.getHeaderField("set-cookie");
                String[] cookiesArray = cookies.split(";");
                String JSESSION_ID = cookiesArray[0];
                return JSESSION_ID;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override // TODO MAKE loading circle.
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String cookie) {

        if(cookie == null) return; // Open Windows that says Incorrect credentials.
        else {
            PreferencesHelper helper = new PreferencesHelper(context);
            helper.setCookie(cookie);

            GetUserInfoThread getUser = new GetUserInfoThread(context);
            getUser.execute();

        }

    }
}
