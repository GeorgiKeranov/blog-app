package georgi.com.BlogApp.Threads;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static georgi.com.BlogApp.Configs.ServerURLs.REGISTER_URL;


public class RegisterThread extends AsyncTask<String, Void, JSONObject> {

    private String TAG = getClass().getSimpleName();

    private Context context;

    // Getting context from called Activity.
    public RegisterThread(Context context){
        this.context = context;
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
            conn.setDoInput(true);

            // Writing the request params.
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write("firstName=" + strings[0] +
                        "&lastName=" + strings[1] +
                        "&email=" + strings[2] +
                        "&username=" + strings[3] +
                        "$password=" + strings[4]);

            writer.flush();
            writer.close();

            // Reading the response.
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
        super.onPostExecute(response);
    }

}
