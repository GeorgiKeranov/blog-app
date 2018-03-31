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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import georgi.com.BlogApp.Activities.Account.AccountActivity;
import georgi.com.BlogApp.Activities.Account.EditAccountActivity;
import georgi.com.BlogApp.Activities.Account.ViewOtherUserActivity;
import georgi.com.BlogApp.Adapters.CommentsAdapter;
import georgi.com.BlogApp.POJO.Comment;
import georgi.com.BlogApp.POJO.Reply;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Account.AuthenticatedUser;
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
    private ProgressBar authorImageProgressBar, postImageProgressBar, commentImageProgressBar;
    private Button butComment;
    private EditText comment;
    private LinearLayout authorRow;

    private LayoutManager layoutManager;

    private RecyclerView commentsRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        commentImage = (ImageView) findViewById(R.id.post_comment_image);

        authorImageProgressBar = (ProgressBar) findViewById(R.id.post_author_image_progress_bar);
        postImageProgressBar = (ProgressBar) findViewById(R.id.post_image_progress_bar);
        commentImageProgressBar = (ProgressBar) findViewById(R.id.post_comment_image_progress_bar);

        butComment = (Button) findViewById(R.id.post_but_comment);
        comment = (EditText) findViewById(R.id.post_comment);

        authorRow = (LinearLayout) findViewById(R.id.post_author_row);

        commentsRecyclerView = (RecyclerView) findViewById(R.id.post_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        commentsRecyclerView.setLayoutManager(layoutManager);


        // Thread to get first name, last name and the profile picture of author of post
        // by id and set them in the UI thread(this thread). Also it is getting the userUrl of the author.
        PostAuthor postAuthor = new PostAuthor(this, authorName, authorImageProgressBar, authorImage);
        postAuthor.execute(postId);

        // Thread to get post from the server and
        // set tile, description and the image of the selected post.
        PostById getPostByIdThread = new PostById(this, date, title, description, postImageProgressBar, postImage);
        getPostByIdThread.execute(postId);

        // Setting the authenticated user image for creating a new comment.
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                this, commentImageProgressBar, commentImage, null, null, null
        );
        authenticatedUser.execute();

        // Thread to get commentsRecyclerView from the server and to set them on the recycler view.
        CommentsOnPost commentsOnPost = new CommentsOnPost(this, commentsRecyclerView);
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
                // Then it is refreshing the new commentsRecyclerView.
                CommentOnPost commentOnPost =
                        new CommentOnPost(getApplicationContext(),
                                postId, commentsRecyclerView);

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

    // Deleting comment from the UI.
    public void deleteCommentById(Long id) {

        CommentsAdapter commentsAdapter = (CommentsAdapter) commentsRecyclerView.getAdapter();

        List<Comment> comments = commentsAdapter.getComments();

        for(int i = 0; i<comments.size(); i++)
            if(comments.get(i).getId() == id)
                comments.remove(i);

        commentsAdapter.notifyDataSetChanged();
    }

    // Delete reply from the UI.
    public void deleteReplyById(Long id) {

        CommentsAdapter commentsAdapter = (CommentsAdapter) commentsRecyclerView.getAdapter();

        List<Comment> comments = commentsAdapter.getComments();

        for(int i = 0; i<comments.size(); i++) {

            List<Reply> currReplies = comments.get(i).getReplies();

            boolean replyDeleted = false;

            for(int y = 0; i<currReplies.size(); i++)
                if(currReplies.get(i).getId() == id) {

                    currReplies.remove(i);
                    replyDeleted = true;

                    break;
                }

            if(replyDeleted)
                break;
        }

        commentsAdapter.notifyDataSetChanged();

    }
}
