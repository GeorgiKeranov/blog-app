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

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder>{

    private Context context;

    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    public List<Post> getPosts() { return posts; }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Post curPost = posts.get(position);

        Glide.with(context)
                .load(POSTS_IMAGES_URL + curPost.getIcon())
                .override(400, 400)
                .into(holder.postImage);

        holder.title.setText(curPost.getSummaryTitle());
        holder.description.setText(curPost.getSummaryDesc());
        holder.post_id = curPost.getId();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Long post_id;
        ImageView postImage;
        TextView title, description;


        public MyViewHolder(View itemView) {
            super(itemView);

            postImage = (ImageView) itemView.findViewById(R.id.post_picture);
            title = (TextView) itemView.findViewById(R.id.post_summaryTitle);
            description = (TextView) itemView.findViewById(R.id.post_summaryDesc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PostActivity.class);
                    intent.putExtra("post_id", post_id);
                    context.startActivity(intent);
                }
            });
        }
    }

}
