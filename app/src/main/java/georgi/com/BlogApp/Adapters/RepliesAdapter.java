package georgi.com.BlogApp.Adapters;

import android.content.Context;
import android.content.Intent;
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
import georgi.com.BlogApp.POJO.Reply;
import georgi.com.BlogApp.POJO.User;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Account.AuthenticatedUser;
import georgi.com.BlogApp.Threads.Posts.DeleteReply;
import georgi.com.BlogApp.Threads.Posts.ReplyOnComment;

public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.MyViewHolder> {

    private Context context;

    // authUserUrl of the authenticated user.
    private String authUserUrl;

    private List<Reply> replies;

    public RepliesAdapter(Context context, List<Reply> replies) {
        this.context = context;
        this.replies = replies;

        this.authUserUrl = new PreferencesHelper(context).getCustomKeyValue("userUrl");
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
        final Reply currReply = replies.get(position);
        User author = currReply.getAuthor();

        // Loading the author profile picture URL into ImageView.
        Glide.with(context)
                .load(author.getProfPicUrl())
                .override(160, 160)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.profilePicProgressBar.setVisibility(View.GONE);
                        holder.profilePic.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.profilePicProgressBar.setVisibility(View.GONE);
                        holder.profilePic.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.profilePic);

        holder.fullName.setText(author.getFullName());
        holder.reply.setText(currReply.getReply());

        // If the author of the post userUrl and the authenticated userUrl
        // are the same setting the delete ImageView visibility to visible.
        if(author.getUserUrl().equals(authUserUrl))
            holder.deleteCurReply.setVisibility(View.VISIBLE);

        holder.deleteCurReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starting new thread to delete the reply from the server.
                DeleteReply deleteReply = new DeleteReply(context, currReply.getId());
                deleteReply.execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar profilePicProgressBar;
        private ImageView profilePic, deleteCurReply;
        private TextView fullName, reply;

        public MyViewHolder(View itemView) {
            super(itemView);

            profilePicProgressBar = (ProgressBar) itemView.findViewById(R.id.reply_row_prof_pic_progress_bar);

            profilePic = (ImageView) itemView.findViewById(R.id.reply_row_profPic);
            deleteCurReply = (ImageView) itemView.findViewById(R.id.reply_row_deleteReply);

            fullName = (TextView) itemView.findViewById(R.id.reply_row_fullName);
            reply = (TextView) itemView.findViewById(R.id.reply_row_reply);

            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Starting new activity to view the clicked comment author account.
                    // And adding extra String "userUrl" of the author.
                    Intent intent = new Intent(context, ViewOtherUserActivity.class);
                    intent.putExtra("userUrl", authUserUrl);
                    context.startActivity(intent);
                }
            });
        }
    }
}
