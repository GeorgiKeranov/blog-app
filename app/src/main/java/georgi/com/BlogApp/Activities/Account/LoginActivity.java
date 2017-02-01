package georgi.com.BlogApp.Activities.Account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Security.Login;

public class LoginActivity extends Activity {

    // Getting "LoginActivity" class name for debugging.
    String TAG = getClass().getSimpleName();

    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.id_username);
        password = (EditText) findViewById(R.id.id_password);

    }

    public void onClick(View view){

        switch (view.getId()){
            case R.id.but_login :
                Login login = new Login(this);
                login.execute(username.getText().toString(),
                                    password.getText().toString());
                break;

            case R.id.id_register :
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
