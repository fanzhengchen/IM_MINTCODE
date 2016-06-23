package mintcode.com.workhub_im.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.R;

/**
 * Created by mark on 16-6-23.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.id_item_image)
    ImageView itemImageView;
    @BindView(R.id.id_item_select)
    ImageButton itemSelect;
    private Context context;

    public ImageViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    public void loadImage(String path) {
        Glide.with(context).load(path).into(itemImageView);
    }
}
