package mintcode.com.workhub_im.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.R;

/**
 * Created by mark on 16-6-22.
 */
public class PhotoActivity extends Activity {

    private static final int SPAN = 3;
    @BindView(R.id.photo_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void scannerPic() {

    }
}
