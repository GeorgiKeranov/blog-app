package georgi.com.BlogApp.Activities.Posts;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import georgi.com.BlogApp.Adapters.CommentsAdapter;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Account.AccountDetails;
import georgi.com.BlogApp.Threads.Posts.CommentOnPost;
import georgi.com.BlogApp.Threads.Posts.CommentsOnPost;
import georgi.com.BlogApp.Threads.Posts.PostById;

public class PostActivity extends AppCompatActivity{

    private Long postId;

    private TextView title, description;
    private ImageView postImage, commentImage;
    private Button butComment;
    private EditText comment;

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

        postId = getIntent().getLongExtra("post_id", -1);
        if(postId == -1) finish();

        title = (TextView) findViewById(R.id.createPost_title);
        description = (TextView) findViewById(R.id.post_description);
        postImage = (ImageView) findViewById(R.id.post_image);
        commentImage = (ImageView) findViewById(R.id.post_commentImage);
        butComment = (Button) findViewById(R.id.post_butComment);
        comment = (EditText) findViewById(R.id.post_comment);


        comments = (RecyclerView) findViewById(R.id.post_recyclerView);
        layoutManager = new LinearLayoutManager(this);
        comments.setLayoutManager(layoutManager);

        // Thread to get post from the server and
        // set tile, description and the image of the selected post.
        PostById getPostsThreadById =
                new PostById(this, title, description, postImage);
        getPostsThreadById.execute(postId);

        // Setting the user details for creating a new comment.
        AccountDetails accountDetails =
                new AccountDetails(this, commentImage, null, null, null);
        accountDetails.execute();


        // Thread to get comments from the server and to set them on the UI.
        CommentsOnPost commentsOnPost =
                new CommentsOnPost(this, comments, commentsAdapter);
        commentsOnPost.execute(postId);

        butComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Commenting on the post and send comment on the server
                // Then it is refreshing the new comments.
                CommentOnPost commentOnPost =
                        new CommentOnPost(getApplicationContext(),
                                postId, comments, commentsAdapter);

                commentOnPost.execute(comment.getText().toString());

                // Deleting the text from the EditText for new comment.
                comment.setText("");
            }
        });
    }
}
