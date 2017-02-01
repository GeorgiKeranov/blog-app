package georgi.com.BlogApp.Threads.Posts;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Comment;
import georgi.com.BlogApp.POJO.Post;
import georgi.com.BlogApp.POJO.Reply;
import georgi.com.BlogApp.POJO.User;

import static georgi.com.BlogApp.Configs.ServerURLs.POSTS_IMAGES_URL;
import static georgi.com.BlogApp.Configs.ServerURLs.POST_URL;

public class GetPostById extends AsyncTask<Long, Void, Post> {

    private Context context;

    private TextView title, description;
    private ImageView postImage;

    public GetPostById(Context context, TextView title, TextView description, ImageView postImage) {
        this.context = context;
        this.title = title;
        this.description = description;
        this.postImage = postImage;
    }

    @Override
    protected Post doInBackground(Long... longs) {

        try {

            HttpRequest request = new HttpRequest(POST_URL + longs[0],
                    new PreferencesHelper(context).getCookie(), "GET");

            String response = request.sendTheRequest();

            JSONObject jsonResponse = new JSONObject(response);

            return convertToObject(jsonResponse);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Post post) {

        Glide.with(context)
                .load(POSTS_IMAGES_URL + post.getIcon())
                .override(800, 800)
                .into(postImage);

        title.setText(post.getTitle());

        description.setText(post.getDescription());
    }


    private Post convertToObject(JSONObject jsonPost) throws JSONException {

        Post post = new Post();
        post.setId(jsonPost.getLong("id"));
        post.setTitle(jsonPost.getString("title"));
        post.setIcon(jsonPost.getString("icon"));
        post.setDescription(jsonPost.getString("description"));
        post.setDate(jsonPost.getString("date"));

        JSONObject curAuthor = jsonPost.getJSONObject("author");
        post.setAuthor(createAuthor(curAuthor));

        return post;

    }

    private User createAuthor(JSONObject author) throws JSONException {

        User newAuthor = new User();
        newAuthor.setUserUrl(author.getString("userUrl"));
        newAuthor.setFirstName(author.getString("firstName"));
        newAuthor.setLastName(author.getString("lastName"));
        newAuthor.setEmail(author.getString("email"));
        newAuthor.setProfile_picture(author.getString("profile_picture"));

        return newAuthor;
    }

}
