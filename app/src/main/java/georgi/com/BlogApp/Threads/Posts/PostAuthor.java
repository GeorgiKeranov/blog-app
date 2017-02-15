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

import static georgi.com.BlogApp.Configs.ServerURLs.DEFAULT_USER_IMG;
import static georgi.com.BlogApp.Configs.ServerURLs.POST_URL;
import static georgi.com.BlogApp.Configs.ServerURLs.USER_IMAGES_URL;

// Sending request to server with some id of post and
// server is returning the author of that post. Then this thread
// is setting the UI thread elements with this author.
public class PostAuthor extends AsyncTask<Long, Void, JSONObject> {

    public interface Listener {
        void setUserUrl(String userUrl);
    }

    private Listener listener;

    private Context context;

    private TextView authorName;
    private ImageView authorImage;

    public PostAuthor(Context context, TextView authorName, ImageView authorImage) {
        this.context = context;
        this.authorName = authorName;
        this.authorImage = authorImage;

        listener = (Listener) context;
    }

    @Override
    protected JSONObject doInBackground(Long... longs) {

        try {

            // Creating the request.
            // longs[0] : this is the id of the post from which that we need the author.
            HttpRequest httpRequest = new HttpRequest(POST_URL + longs[0] + "/author",
                    new PreferencesHelper(context).getCookie(), "GET");

            // Sending the request and getting the response into String.
            String response = httpRequest.sendTheRequest();

            return new JSONObject(response);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {

        // If the json object is null stop the method.
        if(jsonObject == null) return;

        // Checking if the response is correct with one of the params that
        // server is returning for User when all it's okay.
        if(jsonObject.has("userUrl")) {

            try {

                String fullName = jsonObject.getString("firstName") + " " + jsonObject.getString("lastName");
                authorName.setText(fullName);

                String profilePicture = jsonObject.getString("profile_picture");

                // "no" means that there is not profile picture.
                if(profilePicture.equals("no")) profilePicture = DEFAULT_USER_IMG;
                // else if there is picture we are creating the needed url for it.
                // Url is created like that : /res/images/{HERE IS USER URL}/{HERE PROFILE PICTURE NAME}.
                else profilePicture = USER_IMAGES_URL + jsonObject.getString("userUrl") + profilePicture;

                // Setting the ImageView : authorImage directly from the created above url.
                Glide.with(context)
                        .load(profilePicture)
                        .override(800, 800)
                        .into(authorImage);

                listener.setUserUrl(jsonObject.getString("userUrl"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
