package georgi.com.BlogApp.Threads;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static georgi.com.BlogApp.Configs.ServerURLs.REGISTER_URL;


public class RegisterThread extends AsyncTask<String, Void, JSONObject> {

    private String TAG = getClass().getSimpleName();

    private Context context;

    private EditText username, email;

    // Getting context from called Activity.
    public RegisterThread(Context context, EditText username, EditText email){
        this.context = context;
        this.username = username;
        this.email = email;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        JSONObject response = null;

        try {
            // Opening connection.
            URL url = new URL(REGISTER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Writing the request params.
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

            String request = "firstName=" + strings[0] +
                    "&lastName=" + strings[1] +
                    "&email=" + strings[2] +
                    "&username=" + strings[3] +
                    "&password=" + strings[4];

            Log.d(TAG, "REQUEST : " + request);

            writer.write(request);

            writer.flush();
            writer.close();

            // Reading the response.
            InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder jsonString = new StringBuilder();
            String line;

            while((line = bufferedReader.readLine()) != null){
                jsonString.append(line);
            }

            bufferedReader.close();
            inputStreamReader.close();

            response = new JSONObject(jsonString.toString());

        } catch(JSONException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        try {
            boolean error = response.getBoolean("error");

            if(error){
                final String error_msg = response.getString("error_msg");

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Error");
                builder.setMessage(error_msg);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        if(error_msg.equals("Email is already taken.")) email.setText("");
                        else username.setText("");
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            else {

                //TODO

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
