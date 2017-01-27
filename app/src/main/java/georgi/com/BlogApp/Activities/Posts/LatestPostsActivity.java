package georgi.com.BlogApp.Activities.Posts;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Posts.GetLatestPostsThread;

public class LatestPostsActivity extends Activity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView) findViewById(R.id.home_postRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        GetLatestPostsThread getLatestPosts = new GetLatestPostsThread(this, recyclerView, adapter);
        getLatestPosts.execute();

    }

}
