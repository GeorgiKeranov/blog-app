package georgi.com.BlogApp.Activities.Posts;

import android.content.Intent;
import android.os.Bundle;
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
import georgi.com.BlogApp.Adapters.PostsAdapter;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Post;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Posts.Latest5Posts;
import georgi.com.BlogApp.Threads.Posts.Update5Posts;
import georgi.com.BlogApp.Threads.Security.Logout;

import static georgi.com.BlogApp.Configs.ServerURLs.LATEST_POSTS_URL;
import static georgi.com.BlogApp.Configs.ServerURLs.UPDATE_5POSTS_URL;


public class LatestPostsActivity extends AppCompatActivity {

    // The list of posts that is used for the PostsAdapter.
    private List<Post> posts;

    private RecyclerView recyclerView;
    private PostsAdapter adapter;
    private GridLayoutManager gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        // Toolbar settings.
        Toolbar myToolbar = (Toolbar) findViewById(R.id.posts_toolbar);
        myToolbar.setTitle("Latest Posts");
        setSupportActionBar(myToolbar);

        // Initializing the list of posts.
        posts = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.posts_recyclerView);
        gridLayout = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayout); // Setting the layout style.
        adapter = new PostsAdapter(this, posts); // Initializing the adapter.

        recyclerView.setAdapter(adapter); // Setting the adapter.

        // Getting latest 5 posts.
        getLatest5Posts();


        // On scroll in posts it will check if the last item is visible
        // and if it is it will update ui thread with 5 new posts.
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (gridLayout.findLastVisibleItemPosition() == posts.size() - 1) {
                    updateWith5Posts();
                }

            }
        });

    }

    private void getLatest5Posts() {

        // This thread is getting the latest 5 posts from the server
        // And it is updating them to the UI thread.
        Latest5Posts getLatestPosts = new Latest5Posts(this, recyclerView);
        getLatestPosts.execute(LATEST_POSTS_URL);
    }

    private void updateWith5Posts() {

        // This thread is getting the next 5 posts after the
        // last post. And it is doing this by passing the id of the last seen post.
        Update5Posts update5Posts = new Update5Posts(this, recyclerView);
        update5Posts.execute(UPDATE_5POSTS_URL, "" + posts.get(posts.size()-1).getId());
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

        // Posts are cleared.
        posts.clear();

        // And getting the latest 5 posts.
        getLatest5Posts();
    }

}