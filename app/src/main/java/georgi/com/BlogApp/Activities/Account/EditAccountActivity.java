package georgi.com.BlogApp.Activities.Account;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import georgi.com.BlogApp.Activities.Posts.CreateNewPostActivity;
import georgi.com.BlogApp.Activities.Posts.LatestPostsActivity;
import georgi.com.BlogApp.Activities.Posts.YourPostsActivity;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Account.AuthenticatedUser;
import georgi.com.BlogApp.Threads.Account.EditAccount;
import georgi.com.BlogApp.Threads.Security.Logout;


public class EditAccountActivity extends AppCompatActivity {

    private Button setNewPicture, editTheAccount;
    private ProgressBar profilePicProgressBar;
    private ImageView profilePic;
    private EditText firstName, lastName, email, newPassword, confirmPass, currentPassword;

    // This variable is if the user select new picture from phone for the account.
    private Uri picturePath;

    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_edit_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_account_toolbar);
        toolbar.setTitle("Edit Your Account");
        setSupportActionBar(toolbar);

        setNewPicture = (Button) findViewById(R.id.edit_account_newPicBut);
        editTheAccount = (Button) findViewById(R.id.edit_account_saveBut);

        profilePicProgressBar = (ProgressBar) findViewById(R.id.edit_account_profile_pic_progress_bar);

        profilePic = (ImageView) findViewById(R.id.edit_account_profilePic);

        firstName = (EditText) findViewById(R.id.edit_account_firstName);
        lastName = (EditText) findViewById(R.id.edit_account_lastName);
        email = (EditText) findViewById(R.id.edit_account_email);
        newPassword = (EditText) findViewById(R.id.edit_account_newPass);
        confirmPass = (EditText) findViewById(R.id.edit_account_confirmPass);
        currentPassword = (EditText) findViewById(R.id.edit_account_currPassword);

        // Sending request to server and fill in the EditTexts with server's response.
        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(this, profilePicProgressBar , profilePic, firstName, lastName, email);
        authenticatedUser.execute();


        // This button is to change the image in ImageView.
        setNewPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Starting new activity to choose image from phone.
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Choose an app to select image"), 1);
            }
        });


        editTheAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String curPass = currentPassword.getText().toString();
                String newPass = newPassword.getText().toString();
                String confirmNewPass = confirmPass.getText().toString();

                // Checking if currentPassword's text is empty and creating alertDialog if it is.
                if(curPass.equals("")) {

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(context);

                    builder.setTitle("Error");
                    builder.setMessage("Current password field is required!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.create().show();
                }

                // Checking if newPassword's text and confirmPassword's text are not
                // the same and empty. And creating alertDialog if this is true.
                else if(!newPass.equals("") && !(newPass.equals(confirmNewPass))) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Error");
                    builder.setMessage("Your new password and confirm the new password rows are not the same");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            newPassword.setText("");
                            confirmPass.setText("");
                            dialogInterface.dismiss();
                        }
                    });
                    builder.create().show();
                }

                // And finally if they are no errors.
                else {
                    // Sending request who edits the authenticated user details to the server.
                    EditAccount editAccount = new EditAccount(context, picturePath);
                    editAccount.execute(firstName.getText().toString(),
                            lastName.getText().toString(),
                            email.getText().toString(),
                            newPassword.getText().toString(),
                            currentPassword.getText().toString());
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK) {

            // requestCode == 1 : activity that is used to get Uri of image from phone.
            if(requestCode == 1) {

                picturePath = data.getData();

                // Loading the new picture in ImageView.
                Glide.with(this)
                        .load(picturePath)
                        .override(600, 600)
                        .into(profilePic);
            }

        }

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
