package georgi.com.LoginRegister.Threads;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

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

import georgi.com.LoginRegister.Activities.LoggedActivity;


public class RegisterThread extends AsyncTask<String, Void, JSONObject> {

    private String TAG = getClass().getSimpleName();

    private final String registerUrl = "http://192.168.0.102/android-api/register.php";

    private Context context;

    // Getting context from called Activity.
    public RegisterThread(Context context){
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        String request = "name=" + strings[0] +
                "&email=" + strings[1] +
                "&password=" + strings[2];

        JSONObject response = null;
        try {
            URL url = new URL(registerUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStreamWriter output = new OutputStreamWriter(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(output);
            writer.write(request);
            writer.close();

            InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder jsonString = new StringBuilder();
            String curRow;

            while((curRow = bufferedReader.readLine()) != null){
                jsonString.append(curRow);
            }

            inputStreamReader.close();
            bufferedReader.close();

            response = new JSONObject(jsonString.toString());

        } catch(JSONException e){
            e.printStackTrace();
        } catch(MalformedURLException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(JSONObject response) {

        if (response != null) {
            try {
                if (!response.getBoolean("error")) {
                    Intent intent = new Intent(context, LoggedActivity.class);
                    intent.putExtra("JSON", response.toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else{
                    Log.d(TAG, "Response Error : " + response.getString("error_msg"));
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}
