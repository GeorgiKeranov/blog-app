package georgi.com.BlogApp.Activities.Posts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import georgi.com.BlogApp.Activities.Account.AccountActivity;
import georgi.com.BlogApp.Activities.Account.EditAccountActivity;
import georgi.com.BlogApp.Adapters.YourPostsAdapter;
import georgi.com.BlogApp.POJO.Post;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Posts.Latest5UserPosts;
import georgi.com.BlogApp.Threads.Posts.Update5Posts;
import georgi.com.BlogApp.Threads.Security.Logout;

import static georgi.com.BlogApp.Configs.ServerURLs.AUTH_USER_LATEST5_POSTS;
import static georgi.com.BlogApp.Configs.ServerURLs.AUTH_USER_UPDATE_5POSTS_URL;


public class YourPostsActivity extends AppCompatActivity{

    private GridLayoutManager gridLayout;
    private RecyclerView recyclerView;
    private YourPostsAdapter adapter;

    private List<Post> userPosts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_posts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.your_posts_toolbar);
        toolbar.setTitle("Your Posts");
        setSupportActionBar(toolbar);

        userPosts = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.your_posts_recyclerView);
        gridLayout = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayout);

        adapter = new YourPostsAdapter(this, userPosts);
        recyclerView.setAdapter(adapter);

        // Getting the latest 5 user posts and setting the UI thread.
        getLatest5Posts();

        // On scroll in posts it will check if the last item is visible
        // and if it is it will update ui thread with 5 new posts.
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (gridLayout.findLastVisibleItemPosition() == userPosts.size() - 1) {
                    updateWith5Posts();
                }
            }
        });

    }


    private void updateWith5Posts() {

        // If list of posts is empty don't do anything.
        if(userPosts.size() <= 0) return;

        Update5Posts update5Posts = new Update5Posts(this, recyclerView);
        update5Posts.execute(AUTH_USER_UPDATE_5POSTS_URL,
                "" + userPosts.get(userPosts.size() - 1).getId());
    }

    private void getLatest5Posts() {

        // This thread gets the latest 5 posts that authenticated user have been posted.
        Latest5UserPosts latest5UserPosts = new Latest5UserPosts(this, recyclerView);
        latest5UserPosts.execute(AUTH_USER_LATEST5_POSTS);
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

    @Override
    protected void onResume() {
        super.onResume();

        // Updating UI with new Posts.
        getLatest5Posts();
    }
}
