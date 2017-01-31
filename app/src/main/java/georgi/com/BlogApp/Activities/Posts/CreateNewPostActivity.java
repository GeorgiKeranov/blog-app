package georgi.com.BlogApp.Activities.Posts;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Posts.CreatePostThread;

public class CreateNewPostActivity extends AppCompatActivity{

    private ImageView image;
    private EditText title, description;
    private Button butCreatePost;
    private Uri selectedPic;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);

        image = (ImageView) findViewById(R.id.createPost_image);
        title = (EditText) findViewById(R.id.createPost_title);
        description = (EditText) findViewById(R.id.createPost_desc);
        butCreatePost = (Button) findViewById(R.id.createPost_button);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent,"Select Gallery"), 1);
            }
        });

        butCreatePost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                CreatePostThread createPostThread = new CreatePostThread(getApplicationContext(), selectedPic);
                createPostThread.execute(title.getText().toString(), description.getText().toString());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == 1) {
                selectedPic = data.getData();

                Glide.with(this)
                        .load(selectedPic)
                        .override(800, 800)
                        .into(image);
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setTitle("Create New Post");

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            //TODO
        }

        return super.onOptionsItemSelected(item);
    }
}
