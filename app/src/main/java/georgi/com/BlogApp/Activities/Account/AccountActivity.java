package georgi.com.BlogApp.Activities.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import georgi.com.BlogApp.Activities.Posts.YourPostsActivity;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Account.AccountDetails;


public class AccountActivity extends AppCompatActivity{

    private ImageView profile_picture;
    private TextView firstName, lastName, email;

    private Button editAccount, viewPosts;

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

        editAccount = (Button) findViewById(R.id.account_editAccount);
        viewPosts = (Button) findViewById(R.id.account_viewYourPosts);

        editAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starting EditAccountActivity.
                Intent intent = new Intent(getApplicationContext(), EditAccountActivity.class);
                startActivity(intent);
            }
        });

        viewPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starting YourPostsActivity.
                Intent intent = new Intent(getApplicationContext(), YourPostsActivity.class);
                startActivity(intent);
            }
        });

    }

    // This method is setting the UI elements.
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
