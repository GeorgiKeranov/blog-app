package georgi.com.BlogApp.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import georgi.com.BlogApp.Threads.RegisterThread;
import georgi.com.BlogApp.R;

public class RegisterActivity extends AppCompatActivity {

    // Getting "RegisterActivity" class name for debuging.
    String TAG = getClass().getSimpleName();

    EditText firstName, lastName, email, username, password, confirmPass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = (EditText) findViewById(R.id.reg_firstname);
        lastName = (EditText) findViewById(R.id.reg_lastname);
        email = (EditText) findViewById(R.id.reg_email);
        username = (EditText) findViewById(R.id.reg_username);
        password = (EditText) findViewById(R.id.reg_password);
        confirmPass = (EditText) findViewById(R.id.reg_confirmPass);


    }

    public void onClick(View view){

        // Handling Clicks.
        switch (view.getId()){

            // If register button is clicked :
            case R.id.but_register :
                // If password and confirm password are not equal.
                if(!password.getText().toString().equals(confirmPass.getText().toString())){

                    // Show alert that passwords are not the same.
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Passwords error");
                    builder.setMessage("Passwords are not the same");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            // Password and confirm password are cleared.
                            password.setText("");
                            confirmPass.setText("");
                        }
                    }).create().show();

                    break;
                }

                RegisterThread regThread = new RegisterThread(this, username, email);
                regThread.execute(
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        email.getText().toString(),
                        username.getText().toString(),
                        password.getText().toString());

                break;

            // If login button is clicked finish activity
            // so it is going to login activity.
            case R.id.goToLogin :
                finish();
                break;

        }

    }
}
