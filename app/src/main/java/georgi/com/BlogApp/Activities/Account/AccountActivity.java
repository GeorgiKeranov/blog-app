package georgi.com.BlogApp.Activities.Account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Account.GetAccountThread;
import georgi.com.BlogApp.Threads.Posts.GetPostsThread;

import static georgi.com.BlogApp.Configs.ServerURLs.AUTH_USER_POSTS_URL;

public class AccountActivity extends AppCompatActivity{

    ImageView profile_picture;
    TextView firstName, lastName, email;

    LayoutManager layoutManager;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.account_toolbar);
        myToolbar.setTitle("Your Account");

        setSupportActionBar(myToolbar);

        recyclerView = (RecyclerView) findViewById(R.id.account_recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        profile_picture = (ImageView) findViewById(R.id.account_picture);
        firstName = (TextView) findViewById(R.id.account_firstName);
        lastName = (TextView) findViewById(R.id.account_lastName);
        email = (TextView) findViewById(R.id.account_email);

        GetAccountThread getAccount = new GetAccountThread(this,
                profile_picture,
                firstName,
                lastName,
                email);

        getAccount.execute();

        GetPostsThread getPostsThread = new GetPostsThread(this, recyclerView, adapter);
        getPostsThread.execute(AUTH_USER_POSTS_URL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.getItem(0).setTitle("Latest Posts");

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            default: return super.onOptionsItemSelected(item);
        }

    }
}
