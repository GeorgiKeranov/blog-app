package georgi.com.BlogApp.Activities.Posts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import georgi.com.BlogApp.Activities.Account.AccountActivity;
import georgi.com.BlogApp.Activities.Account.EditAccountActivity;
import georgi.com.BlogApp.Activities.Account.ViewOtherUserActivity;
import georgi.com.BlogApp.Adapters.CommentsAdapter;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Account.AccountDetails;
import georgi.com.BlogApp.Threads.Account.UserByUserUrl;
import georgi.com.BlogApp.Threads.Posts.CommentOnPost;
import georgi.com.BlogApp.Threads.Posts.CommentsOnPost;
import georgi.com.BlogApp.Threads.Posts.PostAuthor;
import georgi.com.BlogApp.Threads.Posts.PostById;
import georgi.com.BlogApp.Threads.Security.Logout;


public class PostActivity extends AppCompatActivity implements PostAuthor.Listener {

    private Long postId;
    private String authorUrl;

    private TextView authorName, date, title, description;
    private ImageView authorImage, postImage, commentImage;
    private Button butComment;
    private EditText comment;
    private LinearLayout authorRow;

    private LayoutManager layoutManager;

    private CommentsAdapter commentsAdapter;
    private RecyclerView comments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Checks if the activity is started correctly with
        // extra Long for the id of the post.
        postId = getIntent().getLongExtra("post_id", -1);
        if(postId == -1) finish();

        // Setting the toolbar.
        Toolbar newToolbar = (Toolbar) findViewById(R.id.post_toolbar);
        newToolbar.setTitle("Post");
        setSupportActionBar(newToolbar);

        authorName = (TextView) findViewById(R.id.post_author_name);
        date = (TextView) findViewById(R.id.post_date);
        title = (TextView) findViewById(R.id.post_title);
        description = (TextView) findViewById(R.id.post_description);

        authorImage = (ImageView) findViewById(R.id.post_author_image);
        postImage = (ImageView) findViewById(R.id.post_image);
        commentImage = (ImageView) findViewById(R.id.post_commentImage);

        butComment = (Button) findViewById(R.id.post_butComment);
        comment = (EditText) findViewById(R.id.post_comment);

        authorRow = (LinearLayout) findViewById(R.id.post_author_row);

        comments = (RecyclerView) findViewById(R.id.post_recyclerView);
        layoutManager = new LinearLayoutManager(this);
        comments.setLayoutManager(layoutManager);


        // Thread to get first name, last name and the profile picture of author of post
        // by id and set them in the UI thread(this thread). Also it is getting the userUrl of the author.
        PostAuthor postAuthor = new PostAuthor(this, authorName, authorImage);
        postAuthor.execute(postId);

        // Thread to get post from the server and
        // set tile, description and the image of the selected post.
        PostById getPostsThreadById =
                new PostById(this, date, title, description, postImage);
        getPostsThreadById.execute(postId);

        // Setting the user details for creating a new comment.
        AccountDetails accountDetails =
                new AccountDetails(this, commentImage, null, null, null);
        accountDetails.execute();


        // Thread to get comments from the server and to set them on the UI.
        CommentsOnPost commentsOnPost = new CommentsOnPost(this, comments);
        commentsOnPost.execute(postId);

        // When someone clicks on this it is starting
        // new activity to view the clicked user account.
        authorRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewOtherUserActivity.class);
                intent.putExtra("userUrl", authorUrl);
                startActivity(intent);
            }
        });

        butComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Commenting on the post and send comment on the server
                // Then it is refreshing the new comments.
                CommentOnPost commentOnPost =
                        new CommentOnPost(getApplicationContext(),
                                postId, comments);

                commentOnPost.execute(comment.getText().toString());

                // Deleting the text from the EditText for new comment.
                comment.setText("");
            }
        });
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
    public void setUserUrl(String userUrl) {
        authorUrl = userUrl;
    }
}
