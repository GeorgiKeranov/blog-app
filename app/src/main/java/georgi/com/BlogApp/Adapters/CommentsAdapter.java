package georgi.com.BlogApp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import georgi.com.BlogApp.Activities.Account.ViewOtherUserActivity;
import georgi.com.BlogApp.Helpers.PreferencesHelper;
import georgi.com.BlogApp.POJO.Comment;
import georgi.com.BlogApp.POJO.User;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Account.AuthenticatedUser;
import georgi.com.BlogApp.Threads.Posts.DeleteComment;
import georgi.com.BlogApp.Threads.Posts.ReplyOnComment;

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

    public List<Comment> getComments() {
        return this.comments;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row, parent, false);
        return new CommentsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        // Getting the current comment.
        final Comment curComment = comments.get(position);

        // Setting the commentId with the current comment id.
        holder.commentId = curComment.getId();

        // Getting the author of the comment.
        User comAuthor = curComment.getAuthor();

        // Setting the image with Glide library direct from the url.
        Glide.with(context)
                .load(comAuthor.getProfPicUrl())
                .override(160, 160)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.profilePicProgressBar.setVisibility(View.GONE);
                        holder.profilePicture.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.profilePicProgressBar.setVisibility(View.GONE);
                        holder.profilePicture.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
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

                DeleteComment deleteComment = new DeleteComment(context, holder.commentId, postId);
                deleteComment.execute();
            }
        });

        holder.showNewReplyRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.newReplyRow.getVisibility() == View.GONE) {
                    holder.newReplyRow.setVisibility(View.VISIBLE);
                    // This thread is setting the authenticated user profile picture in ImageView.
                    AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                            context, holder.newReplyAuthorPicProgressBar,
                            holder.newReplyAuthorPic, null, null, null);

                    authenticatedUser.execute();
                }

                else holder.newReplyRow.setVisibility(View.GONE);
            }
        });

        holder.newReplyBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplyOnComment replyOnComment = new ReplyOnComment(context, postId);
                replyOnComment.execute(holder.newReply.getText().toString(), "" + curComment.getId());
            }
        });

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
        private String userUrl;

        private Long commentId;

        private ProgressBar profilePicProgressBar, newReplyAuthorPicProgressBar;
        private ImageView profilePicture, newReplyAuthorPic, deleteComment;
        private TextView fullName, comment, showNewReplyRow;

        private EditText newReply;
        private Button newReplyBut;
        private LinearLayout newReplyRow;

        private RecyclerView replies;
        private RepliesAdapter repliesAdapter;
        private RecyclerView.LayoutManager layoutManager;

        public MyViewHolder(View itemView) {
            super(itemView);

            profilePicProgressBar = (ProgressBar) itemView.findViewById(R.id.comment_user_profile_pic_progress_bar);
            newReplyAuthorPicProgressBar = (ProgressBar) itemView.findViewById(R.id.comment_newReply_prof_pic_progress_bar);

            profilePicture = (ImageView) itemView.findViewById(R.id.comment_userProfilePic);
            newReplyAuthorPic = (ImageView) itemView.findViewById(R.id.comment_newReply_profPic);
            deleteComment = (ImageView) itemView.findViewById(R.id.comment_deleteImg);

            fullName = (TextView) itemView.findViewById(R.id.comment_fullName);
            comment = (TextView) itemView.findViewById(R.id.comment_comment);
            showNewReplyRow = (TextView) itemView.findViewById(R.id.comment_show_new_reply);

            replies = (RecyclerView) itemView.findViewById(R.id.comment_replyRecyclerView);
            layoutManager = new LinearLayoutManager(context);
            replies.setLayoutManager(layoutManager);

            newReplyRow = (LinearLayout) itemView.findViewById(R.id.comment_newReply_row);
            newReply = (EditText) itemView.findViewById(R.id.comment_newReply);
            newReplyBut = (Button) itemView.findViewById(R.id.comment_newReplyBut);

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
