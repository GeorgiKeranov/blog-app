package georgi.com.BlogApp.Activities.Posts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Posts.PostById;
import georgi.com.BlogApp.Threads.Posts.EditPost;


public class EditPostActivity extends AppCompatActivity{

    private ImageView edit_post_image;
    private EditText title, description;

    private Button editPostBut;

    // This variable is used to save the current post's id.
    private Long id;

    // Selected image uri is saved here.
    private Uri fileLocation;

    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        // Getting the id from intent extras.
        id = getIntent().getLongExtra("id", -1);
        if(id == -1) finish();

        edit_post_image = (ImageView) findViewById(R.id.edit_post_image);
        title = (EditText) findViewById(R.id.edit_post_title);
        description = (EditText) findViewById(R.id.edit_post_desc);

        // Thread to get the post details from the server.
        PostById postById = new PostById(this, title, description, edit_post_image);
        postById.execute(id);

        editPostBut = (Button) findViewById(R.id.edit_post_button);

        edit_post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Starting new application for getting image from the phone.
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, 1);
            }
        });

        editPostBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Sending the updated post to the server.
                EditPost editPost = new EditPost(context, fileLocation);
                editPost.execute("" + id, title.getText().toString(), description.getText().toString());
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1 && resultCode == RESULT_OK) {

            // Handling the image that is selected.

            // Setting the image uri.
            fileLocation = data.getData();

            // Changing current image with the selected.
            Glide.with(this)
                    .load(fileLocation)
                    .override(600, 600)
                    .into(edit_post_image);
        }
    }
}
