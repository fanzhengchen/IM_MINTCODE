package mintcode.com.workhub_im.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.util.ImageFolder;
import mintcode.com.workhub_im.viewholder.ImageViewHolder;

/**
 * Created by mark on 16-6-23.
 */
public class PhotoAdapter extends RecyclerView.Adapter<ImageViewHolder> {


    private List<String> imagePaths;


    public PhotoAdapter(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.grid_item, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ImageViewHolder holder = new ImageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String path = imagePaths.get(position);
        holder.loadImage(path);
    }

    @Override
    public int getItemCount() {
        return (imagePaths == null) ? 0 : imagePaths.size();
    }
}
