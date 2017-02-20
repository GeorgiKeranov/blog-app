package georgi.com.BlogApp.Threads.Posts;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    private ImageView postImage;

    public PostById(Context context, TextView date, TextView title,
                    TextView description, ImageView postImage) {
        this.context = context;
        this.date = date;
        this.title = title;
        this.description = description;
        this.postImage = postImage;
    }

    @Override
    protected Post doInBackground(Long... longs) {

        try {

            // Creating the request.
            HttpRequest request = new HttpRequest(POSTS_URL + longs[0],
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

        date.setText(post.getDate());

        Glide.with(context)
                .load(post.getPictureUrl())
                .override(800, 800)
                .into(postImage);

        title.setText(post.getTitle());

        description.setText(post.getDescription());
    }

}
