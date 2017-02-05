package georgi.com.BlogApp.Threads.Posts;


import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import georgi.com.BlogApp.Adapters.YourPostsAdapter;
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Post;

import static georgi.com.BlogApp.Configs.ServerURLs.LATEST_USER_POSTS_URL;

public class Latest5UserPosts extends AsyncTask<Void, Void, List<Post>>{

    private Context context;

    private RecyclerView recyclerView;

    public Latest5UserPosts(Context context, RecyclerView recyclerView){
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    protected List<Post> doInBackground(Void... voids) {

        try {
            HttpRequest normalRequest =
                    new HttpRequest(LATEST_USER_POSTS_URL,
                            new PreferencesHelper(context).getCookie(),
                            "POST");

            String response = normalRequest.sendTheRequest();

            JSONArray responseObj = new JSONArray(response);

            return convertToListOfObjects(responseObj);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(List<Post> posts) {

        YourPostsAdapter adapter = new YourPostsAdapter(context, posts);
        recyclerView.setAdapter(adapter);
    }

    private List<Post> convertToListOfObjects(JSONArray jsonArray) throws JSONException {

        List<Post> postsResponse = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject curPost = jsonArray.getJSONObject(i);

            Post newPost = new Post();
            newPost.setId(curPost.getLong("id"));
            newPost.setTitle(curPost.getString("title"));
            newPost.setDescription(curPost.getString("description"));
            newPost.setIcon(curPost.getString("icon"));
            newPost.setDate(curPost.getString("date"));

            postsResponse.add(newPost);
        }

        return postsResponse;
    }
}
