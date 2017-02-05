package georgi.com.BlogApp.Activities.Posts;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import georgi.com.BlogApp.Adapters.PostsAdapter;
import georgi.com.BlogApp.Adapters.YourPostsAdapter;
import georgi.com.BlogApp.POJO.Post;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Posts.Latest5UserPosts;
import georgi.com.BlogApp.Threads.Posts.Update5Posts;

import static georgi.com.BlogApp.Configs.ServerURLs.UPDATE_USER_5POSTS_URL;

public class YourPostsActivity extends AppCompatActivity{

    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private YourPostsAdapter adapter;

    private List<Post> userPosts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_posts);

        recyclerView = (RecyclerView) findViewById(R.id.your_posts_recyclerView);
        gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Getting the latest 5 user posts and setting the UI thread.
        Latest5UserPosts latest5UserPosts = new Latest5UserPosts(this, recyclerView);
        latest5UserPosts.execute();

        // On scroll in posts it will check if the last item is visible
        // and if it is it will update ui thread with 5 new posts.
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            boolean oneTimeOnly = true;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                // Getting reference of posts from PostsAdapter
                // to get the posts size below in function for updating
                // posts with new 5 on scroll.
                if(oneTimeOnly) {
                    adapter = (YourPostsAdapter) recyclerView.getAdapter();
                    userPosts = adapter.getPosts();

                    oneTimeOnly = false;
                }

                if (gridLayoutManager.findLastVisibleItemPosition() == userPosts.size() - 1) {
                    updateWith5Posts();
                }

            }
        });

    }


    private void updateWith5Posts() {

        Update5Posts update5Posts = new Update5Posts(this, recyclerView);
        update5Posts.execute(UPDATE_USER_5POSTS_URL,
                "" + userPosts.get(userPosts.size() - 1).getId());

    }
}
