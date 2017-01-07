package georgi.com.LoginRegister.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import georgi.com.LoginRegister.R;
import georgi.com.LoginRegister.Threads.LoginThread;

public class LoginActivity extends AppCompatActivity {

    // Getting "LoginActivity" class name for debugging.
    String TAG = getClass().getSimpleName();

    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.id_email);
        password = (EditText) findViewById(R.id.id_password);

    }

    public void onClick(View view){

        switch (view.getId()){
            case R.id.but_login :
                LoginThread loginThread = new LoginThread(getApplicationContext());
                loginThread.execute(email.getText().toString(), password.getText().toString());
                break;

            case R.id.id_register :
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
