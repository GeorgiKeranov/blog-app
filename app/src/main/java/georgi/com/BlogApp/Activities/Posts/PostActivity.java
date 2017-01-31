package georgi.com.BlogApp.Activities.Posts;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import georgi.com.BlogApp.Adapters.CommentsAdapter;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Posts.GetPostThread;

public class PostActivity extends AppCompatActivity{

    private Long post_id;

    private TextView title, description;
    private ImageView image;

    private LayoutManager layoutManager;

    private CommentsAdapter commentsAdapter;
    private RecyclerView comments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar newToolbar = (Toolbar) findViewById(R.id.post_toolbar);
        newToolbar.setTitle("Post");
        setSupportActionBar(newToolbar);

        post_id = getIntent().getLongExtra("post_id", -1);
        if(post_id == -1) finish();

        title = (TextView) findViewById(R.id.createPost_title);
        description = (TextView) findViewById(R.id.post_description);
        image = (ImageView) findViewById(R.id.post_image);

        comments = (RecyclerView) findViewById(R.id.post_recyclerView);
        layoutManager = new LinearLayoutManager(this);
        comments.setLayoutManager(layoutManager);


        GetPostThread getPostsThread =
                new GetPostThread(this, title, description, image, comments, commentsAdapter);

        getPostsThread.execute(post_id);


    }
}
