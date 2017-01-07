package georgi.com.blogapplicationbeta.Threads;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import georgi.com.blogapplicationbeta.Activities.LoggedActivity;

public class LoginThread extends AsyncTask<String, Void, JSONObject>{

    private String TAG = getClass().getSimpleName();

    private Context context;

    private final String loginUrl = "http://192.168.1.102/android-api/login.php";

    public LoginThread(Context context) {
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        String requestParams = "?email=" + strings[0] + "&password=" + strings[1];
        JSONObject jsonResponse = null;

        try {

            URL url = new URL(loginUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStreamWriter output = new OutputStreamWriter(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(output);
            writer.write(requestParams);
            writer.close();

            InputStreamReader input = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(input);

            StringBuilder response = new StringBuilder();
            String curLine;

            while((curLine = reader.readLine()) != null){
                response.append(curLine);
            }
            input.close();
            reader.close();

            jsonResponse = new JSONObject(response.toString());

        } catch(JSONException e){
            e.printStackTrace();
        } catch(MalformedURLException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }

        return jsonResponse;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {

        try {
            boolean error = jsonObject.getBoolean("error");
            if(error)
                Toast.makeText(context, jsonObject.getString("error_msg"), Toast.LENGTH_LONG).show();
            else {
                Intent logged = new Intent(context, LoggedActivity.class);
                logged.putExtra("JSON", jsonObject.toString());
                context.startActivity(logged);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
