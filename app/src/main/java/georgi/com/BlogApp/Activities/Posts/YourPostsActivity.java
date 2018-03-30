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
import georgi.com.BlogApp.Threads.Posts.PostsOnPage;
import georgi.com.BlogApp.Threads.Security.Logout;

import static georgi.com.BlogApp.Configs.ServerURLs.POSTS_AUTH_USER_URL;


public class YourPostsActivity extends AppCompatActivity{

    private String className = this.getClass().getSimpleName();

    private GridLayoutManager gridLayout;
    private RecyclerView recyclerView;
    private YourPostsAdapter adapter;

    private int page = 0;

    private List<Post> userPosts;

    public boolean morePages = true;

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
        getPostsByPage();

        // On scroll in posts it will check if the last item is visible
        // and if it is it will update ui thread with 5 new posts.
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (gridLayout.findLastVisibleItemPosition() == userPosts.size() - 1) {
                    getPostsByPage();
                }
            }
        });

    }


    private void getPostsByPage() {

        if(morePages) {
            PostsOnPage postsOnPage = new PostsOnPage(this, recyclerView, className);
            postsOnPage.execute(POSTS_AUTH_USER_URL + "?page=" + page);

            page++;
        }

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

    public void deletePostById(Long id) {

        for(int i = 0; i<userPosts.size(); i++)
            if(userPosts.get(i).getId() == id) {
                // Removing the post.
                userPosts.remove(i);
                // Updating the adapter.
                recyclerView.getAdapter().notifyDataSetChanged();

                break;
            }
    }
}
