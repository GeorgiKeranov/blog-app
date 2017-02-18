package georgi.com.BlogApp.Activities.Posts;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import georgi.com.BlogApp.Activities.Account.AccountActivity;
import georgi.com.BlogApp.Activities.Account.EditAccountActivity;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Posts.CreatePost;
import georgi.com.BlogApp.Threads.Security.Logout;


public class CreateNewPostActivity extends AppCompatActivity{

    private ImageView image;
    private EditText title, description;
    private Button butAddImage, butCreatePost;
    private Uri selectedPic;

    // That is used in anonymous functions.
    private Context context = this;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_new_post_toolbar);
        toolbar.setTitle("Create New Post");
        setSupportActionBar(toolbar);

        image = (ImageView) findViewById(R.id.create_new_post_image);
        title = (EditText) findViewById(R.id.create_new_post_title);
        description = (EditText) findViewById(R.id.create_new_post_desc);
        butAddImage = (Button) findViewById(R.id.create_new_post_addImageBut);
        butCreatePost = (Button) findViewById(R.id.create_new_post_createBut);

        // If the API level is more than 15
        // It checks for runtime permission.
        if(android.os.Build.VERSION.SDK_INT > 15) {

            // Requesting permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);

            boolean permission = checkForPermission();

            // If the permission is not granted
            // This activity is finishing.
            if (!permission) finish();
        }


        butAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Starting new application to choose one image from phone.
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent,"Choose an app to select a image"), 1);
            }
        });


        butCreatePost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Starting new thread that is sending details from EditTexts
                // and uri of image (if image is selected) to the server. And server
                // is creating new post.
                CreatePost createPost = new CreatePost(context, selectedPic);
                createPost.execute(title.getText().toString(), description.getText().toString());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {

            // Request code 1 means that image is selected.
            if(requestCode == 1) {
                selectedPic = data.getData();

                // Set the new selected image.
                Glide.with(this)
                        .load(selectedPic)
                        .override(800, 800)
                        .into(image);
            }
        }
    }

    // Checks if read external storage permission is granted.
    private boolean checkForPermission() {

        int permissionCode = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if(permissionCode == PackageManager.PERMISSION_GRANTED) return true;
        else return false;
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
}
