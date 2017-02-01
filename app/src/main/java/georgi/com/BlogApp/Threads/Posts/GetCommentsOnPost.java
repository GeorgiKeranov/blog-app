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

import georgi.com.BlogApp.Adapters.CommentsAdapter;
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Comment;
import georgi.com.BlogApp.POJO.Reply;
import georgi.com.BlogApp.POJO.User;

import static georgi.com.BlogApp.Configs.ServerURLs.POST_URL;

public class GetCommentsOnPost extends AsyncTask<Long, Void, List<Comment>> {

    private Context context;
    private RecyclerView recyclerView;
    private CommentsAdapter commentsAdapter;

    public GetCommentsOnPost(Context context, RecyclerView recyclerView, CommentsAdapter commentsAdapter) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.commentsAdapter = commentsAdapter;
    }

    @Override
    protected List<Comment> doInBackground(Long... longs) {

        try {
            HttpRequest httpRequest =
                    new HttpRequest(POST_URL + longs[0] + "/comments",
                            new PreferencesHelper(context).getCookie(),
                            "POST");

            JSONArray jsonArray = new JSONArray(httpRequest.sendTheRequest());

            return convertJSONtoList(jsonArray);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Comment> comments) {

        commentsAdapter = new CommentsAdapter(context, comments);
        recyclerView.setAdapter(commentsAdapter);

    }


    private List<Comment> convertJSONtoList(JSONArray jsonArray) throws JSONException {

        List<Comment> commentsList = new ArrayList<>();
        for (int y = 0; y < jsonArray.length(); y++) {
            JSONObject curComment = jsonArray.getJSONObject(y);

            Comment newComment = new Comment();
            newComment.setId(curComment.getLong("id"));
            newComment.setComment(curComment.getString("comment"));
            newComment.setDate(curComment.getString("date"));

            JSONObject comAuthor = curComment.getJSONObject("author");
            newComment.setAuthor(createAuthor(comAuthor));

            JSONArray replies = curComment.getJSONArray("replies");

            List<Reply> repliesList = new ArrayList<>();
            for (int x = 0; x < replies.length(); x++) {
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
        return commentsList;
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
