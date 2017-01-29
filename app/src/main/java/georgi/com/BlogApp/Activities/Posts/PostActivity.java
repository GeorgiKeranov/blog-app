package georgi.com.BlogApp.Activities.Posts;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Posts.GetPostThread;

public class PostActivity extends AppCompatActivity{

    private Long post_id;

    private TextView title, description;
    private ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar newToolbar = (Toolbar) findViewById(R.id.post_toolbar);
        newToolbar.setTitle("Post");
        setSupportActionBar(newToolbar);

        post_id = getIntent().getLongExtra("post_id", -1);
        if(post_id == -1) finish();

        title = (TextView) findViewById(R.id.post_title);
        description = (TextView) findViewById(R.id.post_description);
        image = (ImageView) findViewById(R.id.post_image);

        GetPostThread getPostsThread = new GetPostThread(this, title, description, image);
        getPostsThread.execute(post_id);
    }
}
