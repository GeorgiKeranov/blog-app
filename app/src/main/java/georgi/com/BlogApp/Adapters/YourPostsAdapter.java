package georgi.com.BlogApp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import georgi.com.BlogApp.Activities.Posts.EditPostActivity;
import georgi.com.BlogApp.Activities.Posts.PostActivity;
import georgi.com.BlogApp.POJO.Post;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Posts.DeleteMyPost;

import static georgi.com.BlogApp.Configs.ServerURLs.DEFAULT_POST_IMG;
import static georgi.com.BlogApp.Configs.ServerURLs.POSTS_IMAGES_URL;


public class YourPostsAdapter extends RecyclerView.Adapter<YourPostsAdapter.MyViewHolder> {

    private Context context;
    private List<Post> posts;

    public YourPostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    // This is used when you need to get posts from adapter.
    public List<Post> getPosts() { return posts; }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.your_post_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Post curPost = posts.get(position);

        holder.postId = curPost.getId();

        // Loading post picture url into the ImageView.
        Glide.with(context)
                .load(curPost.getPictureUrl())
                .override(400, 400)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.postImageProgressBar.setVisibility(View.GONE);
                        holder.postImage.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.postImageProgressBar.setVisibility(View.GONE);
                        holder.postImage.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.postImage);

        holder.title.setText(curPost.getSummaryTitle());
        holder.description.setText(curPost.getSummaryDesc());
        holder.date.setText(curPost.getDate());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar postImageProgressBar;
        private ImageView postImage, editPost, deletePost;
        private TextView title, description, date;

        private Long postId;

        public MyViewHolder(View itemView) {
            super(itemView);

            postImageProgressBar = (ProgressBar) itemView.findViewById(R.id.your_post_image_progress_bar);

            postImage = (ImageView) itemView.findViewById(R.id.your_post_image);
            editPost = (ImageView) itemView.findViewById(R.id.your_post_row_edit);
            deletePost = (ImageView) itemView.findViewById(R.id.your_post_row_delete);

            title = (TextView) itemView.findViewById(R.id.your_post_title);
            description = (TextView) itemView.findViewById(R.id.your_post_desc);
            date = (TextView) itemView.findViewById(R.id.your_post_row_date);

            editPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Starting EditPostActivity with the selected post id : postId.
                    Intent intent = new Intent(context, EditPostActivity.class);
                    intent.putExtra("id", postId);
                    context.startActivity(intent);
                }
            });

            // When delete imageView is clicked :
            deletePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Creating AlertDialog that asks "Are your sure you want to delete... post".
                    // When user clicks Yes the post will be deleted with AsyncThread.
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete Post");
                    builder.setMessage("Are you sure you want to delete " + title.getText().toString() + " ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // AsyncThread that sends to server
                            // request to delete post by given id.
                            DeleteMyPost deleteMyPost = new DeleteMyPost(context, postId);
                            deleteMyPost.execute(); // postId : id of the post selected.

                            dialogInterface.dismiss();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    builder.create().show();
                }
            });

            // When a post row is clicked :
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Starting new PostActivity with extra param.
                    Intent intent = new Intent(context, PostActivity.class);
                    intent.putExtra("post_id", postId); // postId : id of the clicked post.
                    context.startActivity(intent);
                }
            });
        }
    }

}
