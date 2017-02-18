package georgi.com.BlogApp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import georgi.com.BlogApp.Activities.Account.ViewOtherUserActivity;
import georgi.com.BlogApp.POJO.Comment;
import georgi.com.BlogApp.POJO.User;
import georgi.com.BlogApp.R;

import static georgi.com.BlogApp.Configs.ServerURLs.DEFAULT_USER_IMG;
import static georgi.com.BlogApp.Configs.ServerURLs.USER_IMAGES_URL;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder>{

    private Context context;

    private List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row, parent, false);
        return new CommentsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Comment curComment = comments.get(position);

        User comAuthor = curComment.getAuthor();

        // Setting the userUrl of the author of comment.
        holder.userUrl = comAuthor.getUserUrl();

        // Setting the image with Glide library direct from the url.
        Glide.with(context)
                .load(comAuthor.getProfPicUrl())
                .override(160, 160)
                .into(holder.profilePicture);

        holder.fullName.setText(comAuthor.getFullName());
        holder.comment.setText(curComment.getComment());

        // Initializing the repliesAdapter.
        holder.repliesAdapter = new RepliesAdapter(context, curComment.getReplies());

        // Setting the adapter.
        holder.replies.setAdapter(holder.repliesAdapter);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        // That is the url of the author of comment.
        String userUrl;

        ImageView profilePicture;
        TextView fullName, comment;

        RecyclerView replies;
        RepliesAdapter repliesAdapter;
        RecyclerView.LayoutManager layoutManager;

        public MyViewHolder(View itemView) {
            super(itemView);

            profilePicture = (ImageView) itemView.findViewById(R.id.comment_userProfilePic);
            fullName = (TextView) itemView.findViewById(R.id.comment_fullName);
            comment = (TextView) itemView.findViewById(R.id.comment_comment);

            replies = (RecyclerView) itemView.findViewById(R.id.comment_replyRecyclerView);
            layoutManager = new LinearLayoutManager(context);
            replies.setLayoutManager(layoutManager);

            profilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Starting new activity to view the clicked comment author account.
                    // And adding extra String "userUrl" of the author.
                    Intent intent = new Intent(context, ViewOtherUserActivity.class);
                    intent.putExtra("userUrl", userUrl);
                    context.startActivity(intent);
                }
            });
        }
    }

}
