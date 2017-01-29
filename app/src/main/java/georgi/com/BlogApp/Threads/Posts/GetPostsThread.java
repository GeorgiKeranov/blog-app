package georgi.com.BlogApp.Threads.Posts;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import georgi.com.BlogApp.Adapters.PostsAdapter;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Post;

import static georgi.com.BlogApp.Configs.ServerURLs.LATEST10POSTS_URL;


public class GetPostsThread extends AsyncTask<String, Void, List<Post>>{

    private String TAG = getClass().getSimpleName();

    private Context context;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    public GetPostsThread(Context context, RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
    }

    @Override
    protected List<Post> doInBackground(String... strings) {

        try {

            URL url = new URL(strings[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Cookie", new PreferencesHelper(context).getCookie());

            InputStreamReader iSReader = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(iSReader);

            StringBuilder builder = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null){
                builder.append(line);
            }
            reader.close();
            iSReader.close();

            JSONArray response = new JSONArray(builder.toString());

            return convertToListOfObjects(response);

        } catch(IOException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Post> posts) {

        adapter = new PostsAdapter(context, posts);
        recyclerView.setAdapter(adapter);
    }

    private List<Post> convertToListOfObjects(JSONArray jsonArray) throws JSONException {

        List<Post> postsResponse = new ArrayList<>();

        for(int i = 0; i<jsonArray.length(); i++) {

            JSONObject curPost = jsonArray.getJSONObject(i);

            Post newPost = new Post();
            newPost.setId(curPost.getLong("id"));
            newPost.setTitle(curPost.getString("title"));
            newPost.setDescription(curPost.getString("description"));
            newPost.setIcon(curPost.getString("icon"));
            newPost.setDate(curPost.getString("date"));

            postsResponse.add(newPost);
        }

        return  postsResponse;
    }
}
