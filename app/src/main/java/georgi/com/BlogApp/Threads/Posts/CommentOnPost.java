package georgi.com.BlogApp.Threads.Posts;


import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;

import georgi.com.BlogApp.Adapters.CommentsAdapter;
import georgi.com.BlogApp.Helpers.HttpRequest;
import georgi.com.BlogApp.Helpers.PreferencesHelper;

import static georgi.com.BlogApp.Configs.ServerURLs.POST_URL;

public class CommentOnPost extends AsyncTask<String, Void, Void>{

    private Context context;
    private RecyclerView recyclerView;
    private CommentsAdapter commentsAdapter;
    private Long id;

    public CommentOnPost(Context context, Long id, RecyclerView recyclerView, CommentsAdapter commentsAdapter) {
        this.context = context;
        this.id = id;
        this.recyclerView = recyclerView;
        this.commentsAdapter = commentsAdapter;
    }

    @Override
    protected Void doInBackground(String... strings) {

        try {
            HttpRequest httpRequest =
                    new HttpRequest(POST_URL + id,
                            new PreferencesHelper(context).getCookie(), "POST");

            httpRequest.addStringField("comment", strings[0]);

            String response = httpRequest.sendTheRequest();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {

        CommentsOnPost commentsOnPost =
                new CommentsOnPost(context, recyclerView, commentsAdapter);
        commentsOnPost.execute(id);

    }
}
