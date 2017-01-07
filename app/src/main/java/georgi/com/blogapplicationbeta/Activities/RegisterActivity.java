package georgi.com.blogapplicationbeta.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import georgi.com.blogapplicationbeta.Threads.RegisterThread;
import georgi.com.blogapplicationbeta.R;

/**
 * Created by georgi on 12/14/16.
 */

public class RegisterActivity extends AppCompatActivity {

    // Getting "RegisterActivity" class name for debuging.
    String TAG = getClass().getSimpleName();

    EditText email, name, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.reg_name);
        email = (EditText) findViewById(R.id.reg_email);
        password = (EditText) findViewById(R.id.reg_password);


    }

    public void onClick(View view){

        // Handling Clicks.
        switch (view.getId()){

            case R.id.but_register :
                RegisterThread registerThread = new RegisterThread(getApplicationContext());
                registerThread.execute(name.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString());
                break;

            case R.id.goToLogin :
                finish();
                break;

        }

    }
}
