package georgi.com.BlogApp.Adapters;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

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

        String profilePic = comAuthor.getProfile_picture();

        // Check if there is profile picture.
        // If it is not, profilePic is set to default profile picture.
        if(profilePic.equals("no"))
            profilePic = DEFAULT_USER_IMG;
        else  profilePic = USER_IMAGES_URL + comAuthor.getUserUrl() + "/" + profilePic;

        Glide.with(context)
                .load(USER_IMAGES_URL + comAuthor.getUserUrl() + "/" + comAuthor.getProfile_picture())
                .override(160, 160)
                .into(holder.profilePicture);

        holder.fullName.setText(comAuthor.getFullName());
        holder.comment.setText(curComment.getComment());

        holder.repliesAdapter = new RepliesAdapter(context, curComment.getReplies());
        holder.replies.setAdapter(holder.repliesAdapter);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

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
        }
    }

}
