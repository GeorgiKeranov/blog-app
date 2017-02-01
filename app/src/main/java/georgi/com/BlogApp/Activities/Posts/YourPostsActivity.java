package georgi.com.BlogApp.Activities.Posts;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import georgi.com.BlogApp.Adapters.YourPostsAdapter;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Posts.AuthUserPosts;

public class YourPostsActivity extends AppCompatActivity{

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private YourPostsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_posts);

        recyclerView = (RecyclerView) findViewById(R.id.your_posts_recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        AuthUserPosts authUserPostsThread =
                new AuthUserPosts(this, recyclerView, adapter);

        authUserPostsThread.execute();

    }
}
