package georgi.com.BlogApp.Threads.Posts;


import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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

import georgi.com.BlogApp.Adapters.CommentsAdapter;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Comment;
import georgi.com.BlogApp.POJO.Post;
import georgi.com.BlogApp.POJO.Reply;
import georgi.com.BlogApp.POJO.User;

import static georgi.com.BlogApp.Configs.ServerURLs.POSTS_IMAGES_URL;
import static georgi.com.BlogApp.Configs.ServerURLs.POST_URL;

public class GetPostThread extends AsyncTask<Long, Void, Post> {

    private Context context;

    private TextView title, description;
    private ImageView postImage;

    private RecyclerView comments;
    private CommentsAdapter commentsAdapter;

    public GetPostThread(Context context, TextView title, TextView description, ImageView postImage, RecyclerView comments, CommentsAdapter commentsAdapter) {
        this.context = context;
        this.title = title;
        this.description = description;
        this.postImage = postImage;
        this.comments = comments;
        this.commentsAdapter = commentsAdapter;
    }

    @Override
    protected Post doInBackground(Long... longs) {

        try {
            URL url = new URL(POST_URL + longs[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Cookie", new PreferencesHelper(context).getCookie());

            InputStreamReader is = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(is);

            String line;
            StringBuilder builder = new StringBuilder();

            while((line = reader.readLine()) != null) {
                builder.append(line);
            }

            reader.close();
            is.close();

            JSONObject response = new JSONObject(builder.toString());

            return convertToObject(response);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Post post) {

        title.setText(post.getTitle());
        title.setVisibility(View.VISIBLE);

        Glide.with(context)
                .load(POSTS_IMAGES_URL + post.getIcon())
                .override(800, 800)
                .into(postImage);

        description.setText(post.getDescription());
        description.setVisibility(View.VISIBLE);

        commentsAdapter = new CommentsAdapter(context, post.getComments());
        comments.setAdapter(commentsAdapter);
    }


    private Post convertToObject(JSONObject jsonPost) throws JSONException {

        Post post = new Post();
        post.setId(jsonPost.getLong("id"));
        post.setTitle(jsonPost.getString("title"));
        post.setIcon(jsonPost.getString("icon"));
        post.setDescription(jsonPost.getString("description"));

        JSONObject curAuthor = jsonPost.getJSONObject("author");
        post.setAuthor(createAuthor(curAuthor));

        post.setDate(jsonPost.getString("date"));

        JSONArray comments = jsonPost.getJSONArray("comments");

        List<Comment> commentsList = new ArrayList<>();
        for (int y = 0; y<comments.length(); y++){
            JSONObject curComment = comments.getJSONObject(y);

            Comment newComment = new Comment();
            newComment.setId(curComment.getLong("id"));
            newComment.setComment(curComment.getString("comment"));
            newComment.setDate(curComment.getString("date"));

            JSONObject comAuthor = curComment.getJSONObject("author");
            newComment.setAuthor(createAuthor(comAuthor));

            JSONArray replies = curComment.getJSONArray("replies");

            List<Reply> repliesList = new ArrayList<>();
            for(int x = 0; x<replies.length(); x++){
                JSONObject curReply = replies.getJSONObject(x);

                Reply newReply = new Reply();
                newReply.setId(curReply.getLong("id"));
                newReply.setReply(curReply.getString("reply"));
                newReply.setDate(curReply.getString("date"));

                JSONObject replyAuthor = curReply.getJSONObject("author");
                newReply.setAuthor(createAuthor(replyAuthor));

                repliesList.add(newReply);
            }

            newComment.setReplies(repliesList);

            commentsList.add(newComment);
        }

        post.setComments(commentsList);

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
