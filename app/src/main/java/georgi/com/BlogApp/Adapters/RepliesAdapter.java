package georgi.com.BlogApp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import georgi.com.BlogApp.POJO.Reply;
import georgi.com.BlogApp.POJO.User;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Account.AuthenticatedUser;
import georgi.com.BlogApp.Threads.Posts.ReplyOnComment;

import static georgi.com.BlogApp.Configs.ServerURLs.DEFAULT_USER_IMG;
import static georgi.com.BlogApp.Configs.ServerURLs.USER_IMAGES_URL;

public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.MyViewHolder> {

    private Context context;

    private List<Reply> replies;

    // This is the id of the comment that we will show replies on.
    private Long commentId;

    // This the id of the post that belongs these replies.
    private Long postId;

    public RepliesAdapter(Context context, List<Reply> replies, Long postId, Long commentId) {
        this.context = context;
        this.replies = replies;
        this.postId = postId;
        this.commentId = commentId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        // Getting current post from the list.
        // And getting the author from that post.
        Reply currReply = replies.get(position);
        User author = currReply.getAuthor();

        // Loading the author profile picture URL into ImageView.
        Glide.with(context)
                .load(author.getProfPicUrl())
                .override(160, 160)
                .into(holder.profilePic);

        holder.fullName.setText(author.getFullName());
        holder.reply.setText(currReply.getReply());


        holder.showNewReplyRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.newReplyRow.getVisibility() == View.GONE) {
                    holder.newReplyRow.setVisibility(View.VISIBLE);
                    // This thread is setting the authenticated user profile picture in ImageView.
                    AuthenticatedUser authenticatedUser =
                            new AuthenticatedUser(context, holder.newReplyAuthorPic, null, null, null);
                    authenticatedUser.execute();
                }

                else holder.newReplyRow.setVisibility(View.GONE);
            }
        });

        holder.newReplyBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ReplyOnComment replyOnComment = new ReplyOnComment(context, postId);
                replyOnComment.execute(holder.newReply.getText().toString(), "" + commentId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView profilePic, newReplyAuthorPic;
        private TextView fullName, reply, showNewReplyRow;
        private EditText newReply;
        private Button newReplyBut;
        private LinearLayout newReplyRow;

        public MyViewHolder(View itemView) {
            super(itemView);

            profilePic = (ImageView) itemView.findViewById(R.id.reply_row_profPic);
            newReplyAuthorPic = (ImageView) itemView.findViewById(R.id.reply_row_newReply_profPic);

            showNewReplyRow = (TextView) itemView.findViewById(R.id.reply_row_showNewReplyRow);
            fullName = (TextView) itemView.findViewById(R.id.reply_row_fullName);
            reply = (TextView) itemView.findViewById(R.id.reply_row_reply);

            newReplyRow = (LinearLayout) itemView.findViewById(R.id.reply_row_newReply_row);
            newReply = (EditText) itemView.findViewById(R.id.reply_row_newReply);
            newReplyBut = (Button) itemView.findViewById(R.id.reply_row_newReplyBut);
        }
    }
}
