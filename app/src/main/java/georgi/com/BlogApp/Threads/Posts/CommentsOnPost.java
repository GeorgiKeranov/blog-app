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


// This thread is sending request to get comments for some post
// and handling comments and replies then setting them in recyclerView.
public class CommentsOnPost extends AsyncTask<Long, Void, List<Comment>> {

    private Context context;

    // This recyclerView is used for the comments.
    private RecyclerView commentsRecyclerView;

    public CommentsOnPost(Context context, RecyclerView commentsRecyclerView) {
        this.context = context;
        this.commentsRecyclerView = commentsRecyclerView;
    }

    @Override
    protected List<Comment> doInBackground(Long... longs) {

        try {

            // Sending the request to get comments on post with id - longs[0].
            HttpRequest httpRequest =
                    new HttpRequest(POST_URL + longs[0] + "/comments",
                            new PreferencesHelper(context).getCookie(),
                            "POST");

            // Sending the request and converting the response to JSONArray.
            JSONArray jsonArray = new JSONArray(httpRequest.sendTheRequest());

            // Converting the JSONArray to the List of comments and returning it.
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

        // Creating the adapter for the list of comments.
        CommentsAdapter commentsAdapter = new CommentsAdapter(context, comments);

        // Setting the adapter to the recyclerView.
        commentsRecyclerView.setAdapter(commentsAdapter);
    }


    // This method is used to convert JSONArray to List of comments.
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

    // This method is used to convert JSONObject with details for user to User object.
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
