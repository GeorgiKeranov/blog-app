package georgi.com.BlogApp.Activities.Account;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Account.AccountDetails;

public class AccountActivity extends AppCompatActivity{

    private ImageView profile_picture;
    private TextView firstName, lastName, email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Toolbar settings.
        Toolbar myToolbar = (Toolbar) findViewById(R.id.account_toolbar);
        myToolbar.setTitle("Your Account");
        setSupportActionBar(myToolbar);

        profile_picture = (ImageView) findViewById(R.id.account_picture);
        firstName = (TextView) findViewById(R.id.account_firstName);
        lastName = (TextView) findViewById(R.id.account_lastName);
        email = (TextView) findViewById(R.id.account_email);

        setLayoutElements();

    }

    private void setLayoutElements() {
        // AccountDetails is async thread.
        // It is getting the details for the
        // authenticated user and use them
        // to set UI elements below.
        AccountDetails getAccount = new AccountDetails(this,
                profile_picture,
                firstName,
                lastName,
                email);

        // Starting the thread.
        getAccount.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            default: return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // When the application is resumed
        // The UI elements are refreshed by this function
        setLayoutElements();
    }
}
