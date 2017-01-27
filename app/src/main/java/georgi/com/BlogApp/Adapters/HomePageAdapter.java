package georgi.com.BlogApp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import georgi.com.BlogApp.POJO.Post;
import georgi.com.BlogApp.R;
import georgi.com.BlogApp.Threads.Images.SetImageThread;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.MyViewHolder>{

    private List<Post> posts;

    public HomePageAdapter(List<Post> posts){
        this.posts = posts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Post curPost = posts.get(position);

        SetImageThread setImage = new SetImageThread(holder.postImage);
        setImage.execute(curPost.getIcon());

        holder.title.setText(curPost.getSummaryTitle());
        holder.description.setText(curPost.getSummaryDesc());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView postImage;
        TextView title, description;


        public MyViewHolder(View itemView) {
            super(itemView);

            postImage = (ImageView) itemView.findViewById(R.id.post_picture);
            title = (TextView) itemView.findViewById(R.id.post_summaryTitle);
            description = (TextView) itemView.findViewById(R.id.post_summaryDesc);

        }
    }

}
