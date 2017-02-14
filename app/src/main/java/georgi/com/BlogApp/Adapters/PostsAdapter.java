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

import georgi.com.BlogApp.Activities.Posts.EditPostActivity;
import georgi.com.BlogApp.Activities.Posts.PostActivity;
import georgi.com.BlogApp.POJO.Post;
import georgi.com.BlogApp.R;

import static georgi.com.BlogApp.Configs.ServerURLs.DEFAULT_POST_IMG;
import static georgi.com.BlogApp.Configs.ServerURLs.POSTS_IMAGES_URL;


public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder>{

    private Context context;

    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    // This method is used when you need to get current posts from adapter.
    public List<Post> getPosts() { return posts; }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // Getting current post by position.
        Post curPost = posts.get(position);

        holder.post_id = curPost.getId();

        String imageUrl = curPost.getIcon();

        // Checks if there is image.
        // There is not so changing it to default.
        if(imageUrl.equals("no")) imageUrl = DEFAULT_POST_IMG;
        // There is so creating the needed url.
        else imageUrl = POSTS_IMAGES_URL + imageUrl;

        // Glide loads given image by url into the ImageView.
        Glide.with(context)
                .load(imageUrl)
                .override(400, 400)
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

        private Long post_id;
        private ImageView postImage;
        private TextView title, description, date;


        public MyViewHolder(View itemView) {
            super(itemView);

            postImage = (ImageView) itemView.findViewById(R.id.post_row_picture);
            title = (TextView) itemView.findViewById(R.id.post_row_summaryTitle);
            description = (TextView) itemView.findViewById(R.id.post_row_summaryDesc);
            date = (TextView) itemView.findViewById(R.id.post_row_date) ;

            // When a item is clicked in the recyclerView :
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Starting PostActivity with extra param :
                    Intent intent = new Intent(context, PostActivity.class);

                    // post_id - id of the post that is clicked.
                    intent.putExtra("post_id", post_id);

                    context.startActivity(intent);
                }
            });
        }
    }

}
