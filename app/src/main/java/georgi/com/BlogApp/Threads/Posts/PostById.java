package georgi.com.BlogApp.Threads.Posts;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Post;
import georgi.com.BlogApp.POJO.User;

import static georgi.com.BlogApp.Configs.ServerURLs.DEFAULT_POST_IMG;
import static georgi.com.BlogApp.Configs.ServerURLs.DEFAULT_USER_IMG;
import static georgi.com.BlogApp.Configs.ServerURLs.POSTS_IMAGES_URL;
import static georgi.com.BlogApp.Configs.ServerURLs.POST_URL;
import static georgi.com.BlogApp.Configs.ServerURLs.USER_IMAGES_URL;


// This thread is sending GET request to server to get a post
// by id and then sets the UI thread elements with the response information.
public class PostById extends AsyncTask<Long, Void, Post> {

    private Context context;

    private TextView title, description;
    private ImageView postImage;

    public PostById(Context context, TextView title, TextView description, ImageView postImage) {
        this.context = context;
        this.title = title;
        this.description = description;
        this.postImage = postImage;
    }

    @Override
    protected Post doInBackground(Long... longs) {

        try {


            // Creating the request.
            HttpRequest request = new HttpRequest(POST_URL + longs[0],
                    new PreferencesHelper(context).getCookie(), "GET");

            // Sending the request and getting the response.
            String response = request.sendTheRequest();

            // Converting the response to JSONObject.
            JSONObject jsonResponse = new JSONObject(response);

            // Converting the JSONObject to Post object.
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

        String image = post.getIcon();

        // Checking if there is picture.
        if(image.equals("no"))
            // If there is not picture it is changing
            // the variable to the default post image url.
            image = DEFAULT_POST_IMG;

        // If there is picture it is changing to the correct url.
        else image = POSTS_IMAGES_URL + post.getIcon();

        Glide.with(context)
                .load(image)
                .override(800, 800)
                .into(postImage);

        title.setText(post.getTitle());

        description.setText(post.getDescription());
    }


    // This method is converting JSONObject to Post object.
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

    // This method is converting JSONObject to User object.
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
