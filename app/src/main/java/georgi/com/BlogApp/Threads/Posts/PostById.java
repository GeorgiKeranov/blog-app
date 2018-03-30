package georgi.com.BlogApp.Threads.Posts;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import java.io.IOException;

import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Post;

import static georgi.com.BlogApp.Configs.ServerURLs.POSTS_URL;


// This thread is sending GET request to server to get a post
// by id and then sets the UI thread elements with the response information.
public class PostById extends AsyncTask<Long, Void, Post> {

    private Context context;

    private TextView date, title, description;
    private ProgressBar postImageProgressBar;
    private ImageView postImage;

    public PostById(Context context, TextView date, TextView title,
                    TextView description, ProgressBar postImageProgressBar, ImageView postImage) {
        this.context = context;
        this.date = date;
        this.title = title;
        this.description = description;
        this.postImageProgressBar = postImageProgressBar;
        this.postImage = postImage;
    }

    @Override
    protected Post doInBackground(Long... longs) {

        try {

            // Creating the request.
            HttpRequest request = new HttpRequest(POSTS_URL + "/" + longs[0],
                    new PreferencesHelper(context).getCookie(), "GET");

            // Sending the request and getting the response.
            String response = request.sendTheRequest();

            Gson gson = new Gson();

            // Converting the response from JSON object to Post object;
            return gson.fromJson(response, Post.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Post post) {

        // If no post is returned close the PostActivity.
        if(post == null)
            ((Activity) context).finish();

        date.setText(post.getDate());

        Glide.with(context)
                .load(post.getPictureUrl())
                .override(800, 800)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        postImageProgressBar.setVisibility(View.GONE);
                        postImage.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        postImageProgressBar.setVisibility(View.GONE);
                        postImage.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(postImage);

        title.setText(post.getTitle());

        description.setText(post.getDescription());
    }

}
