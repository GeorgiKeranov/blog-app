package georgi.com.BlogApp.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import georgi.com.BlogApp.Activities.Posts.PostActivity;
import georgi.com.BlogApp.POJO.Post;
import georgi.com.BlogApp.R;

import static georgi.com.BlogApp.Configs.ServerURLs.POSTS_IMAGES_URL;

public class YourPostsAdapter extends RecyclerView.Adapter<YourPostsAdapter.MyViewHolder> {

    private Context context;
    private List<Post> posts;

    public YourPostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.your_post_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Post curPost = posts.get(position);

        holder.postId = curPost.getId();

        Glide.with(context)
                .load(POSTS_IMAGES_URL + curPost.getIcon())
                .override(400, 400)
                .into(holder.postImage);

        holder.title.setText(curPost.getTitle());
        holder.description.setText(curPost.getDescription());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView postImage;
        private TextView title, description;

        private Long postId;

        public MyViewHolder(View itemView) {
            super(itemView);

            postImage = (ImageView) itemView.findViewById(R.id.your_post_image);
            title = (TextView) itemView.findViewById(R.id.your_post_title);
            description = (TextView) itemView.findViewById(R.id.your_post_desc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, PostActivity.class);
                    intent.putExtra("post_id", postId);
                    context.startActivity(intent);
                }
            });
        }
    }

}
