package georgi.com.BlogApp.Activities.Posts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import georgi.com.BlogApp.Activities.Account.AccountActivity;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Posts.GetPostsThread;

import static georgi.com.BlogApp.Configs.ServerURLs.LATEST10POSTS_URL;

public class LatestPostsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        username = new PreferencesHelper(this).getCustomKeyValue("username");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.posts_toolbar);
        myToolbar.setTitle("Latest Posts");

        setSupportActionBar(myToolbar);


        recyclerView = (RecyclerView) findViewById(R.id.posts_recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        GetPostsThread getLatestPosts = new GetPostsThread(this, recyclerView, adapter);
        getLatestPosts.execute(LATEST10POSTS_URL);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setTitle(username);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.toolbar_account :
                Intent intent = new Intent(this, AccountActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
