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
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Comment;
import georgi.com.BlogApp.POJO.User;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Posts.DeleteComment;

import static georgi.com.BlogApp.Configs.ServerURLs.DEFAULT_USER_IMG;
import static georgi.com.BlogApp.Configs.ServerURLs.USER_IMAGES_URL;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder>{

    private Context context;

    // That is the id of the post that the list of comments belongs.
    private Long postId;

    private List<Comment> comments;

    // Here we are holding the userUrl of the authenticated user.
    private String authUserUrl;

    public CommentsAdapter(Context context, List<Comment> comments, Long postId) {
        this.context = context;
        this.comments = comments;
        this.postId = postId;

        // Getting the userUrl of the authenticated user.
        authUserUrl = new PreferencesHelper(context).getCustomKeyValue("userUrl");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row, parent, false);
        return new CommentsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        // Getting the current comment.
        Comment curComment = comments.get(position);

        // Setting the commentId with the current comment id.
        holder.commentId = curComment.getId();

        // Getting the author of the comment.
        User comAuthor = curComment.getAuthor();

        // Setting the image with Glide library direct from the url.
        Glide.with(context)
                .load(comAuthor.getProfPicUrl())
                .override(160, 160)
                .into(holder.profilePicture);

        // Setting the userUrl of the author of comment.
        holder.userUrl = comAuthor.getUserUrl();
        // Setting the fullName of the author of comment.
        holder.fullName.setText(comAuthor.getFullName());
        holder.comment.setText(curComment.getComment());


        // If the authenticated userUrl and the author userUrl are the
        // same we are setting the deleteComment ImageView to visible.
        if(authUserUrl.equals(holder.userUrl)) {
            holder.deleteComment.setVisibility(View.VISIBLE);
        }

        holder.deleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DeleteComment deleteComment = new DeleteComment(context, postId);
                deleteComment.execute(holder.commentId);
            }
        });

        // Initializing the repliesAdapter.
        holder.repliesAdapter = new RepliesAdapter(context, curComment.getReplies(), postId, holder.commentId);

        // Setting the adapter.
        holder.replies.setAdapter(holder.repliesAdapter);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        // That is the url of the author of comment.
        private String userUrl;

        private Long commentId;

        private ImageView profilePicture, deleteComment;
        private TextView fullName, comment;

        private RecyclerView replies;
        private RepliesAdapter repliesAdapter;
        private RecyclerView.LayoutManager layoutManager;

        public MyViewHolder(View itemView) {
            super(itemView);

            profilePicture = (ImageView) itemView.findViewById(R.id.comment_userProfilePic);
            deleteComment = (ImageView) itemView.findViewById(R.id.comment_deleteImg);
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
