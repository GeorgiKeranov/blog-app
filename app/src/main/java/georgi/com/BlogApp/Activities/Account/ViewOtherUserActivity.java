package georgi.com.BlogApp.Activities.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import georgi.com.BlogApp.Activities.Posts.CreateNewPostActivity;
import georgi.com.BlogApp.Activities.Posts.LatestPostsActivity;
import georgi.com.BlogApp.Activities.Posts.YourPostsActivity;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Account.UserByUserUrl;
import georgi.com.BlogApp.Threads.Security.Logout;


public class ViewOtherUserActivity extends AppCompatActivity {

    private String userUrl = null;

    private ImageView profilePic;
    private TextView fullname, email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_other_user);

        // Checks if this activity is started properly
        // (if the activity have extra String with name "userUrl").
        userUrl = getIntent().getStringExtra("userUrl");
        if(userUrl == null) finish();

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_other_user_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        profilePic = (ImageView) findViewById(R.id.view_other_user_image);

        fullname = (TextView) findViewById(R.id.view_other_user_fullName);
        email = (TextView) findViewById(R.id.view_other_user_email);

        // This thread is sending request to the server
        // with the above userUrl in order to get the user.
        UserByUserUrl userByUserUrl = new UserByUserUrl(this,
                profilePic, fullname, email);

        // Starting the thread with the userUrl as param.
        userByUserUrl.execute(userUrl);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = null;

        switch (item.getItemId()) {

            case R.id.toolbar_latestPosts:
                intent = new Intent(this, LatestPostsActivity.class);
                break;

            case R.id.toolbar_createPost:
                intent = new Intent(this, CreateNewPostActivity.class);
                break;

            case R.id.toolbar_yourPosts:
                intent = new Intent(this, YourPostsActivity.class);
                break;

            case R.id.toolbar_account:
                intent = new Intent(this, AccountActivity.class);
                break;

            case R.id.toolbar_edit_account:
                intent = new Intent(this, EditAccountActivity.class);
                break;

            case R.id.toolbar_logout:
                Logout logout = new Logout(this);
                logout.execute();
                break;
        }

        if (intent != null) startActivity(intent);

        return super.onOptionsItemSelected(item);

    }

}
