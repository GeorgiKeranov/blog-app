package georgi.com.LoginRegister.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import georgi.com.LoginRegister.R;


public class LoggedActivity extends AppCompatActivity {
    String TAG = getClass().getSimpleName();
    TextView text_name, text_email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);

        text_name = (TextView) findViewById(R.id.text_name);
        text_email = (TextView) findViewById(R.id.text_email);

        Intent intent = getIntent();
        if (intent.hasExtra("JSON")) getResponse(intent.getStringExtra("JSON"));
    }

    private void getResponse(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONObject user = json.getJSONObject("user");
            String name = user.getString("name");
            String email = user.getString("email");

            text_name.setText(name);
            text_email.setText(email);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
