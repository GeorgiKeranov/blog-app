package georgi.com.BlogApp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.SetImageThread;


public class LoggedActivity extends AppCompatActivity {

    String TAG = getClass().getSimpleName();

    TextView text_name;
    ImageView image_user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);

        text_name = (TextView) findViewById(R.id.text_name);

        image_user = (ImageView) findViewById(R.id.image_user);

        Intent intent = getIntent();
        if (intent.hasExtra("JSON")) onResponse(intent.getStringExtra("JSON"));
    }

    private void onResponse(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            String name = json.getString("fullname");

            text_name.setText(name);

            String imageName = json.getString("profile_picture");
            if(imageName == null || imageName.equals("")) return;

            SetImageThread imageThread = new SetImageThread(image_user);
            imageThread.execute(json.getString("username"), imageName);


        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
