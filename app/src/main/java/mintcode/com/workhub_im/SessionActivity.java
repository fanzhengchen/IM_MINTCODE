package mintcode.com.workhub_im;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;

/**
 * Created by mark on 16-6-8.
 */
public class SessionActivity extends Activity {

    @BindView(R.id.session_list)
    RecyclerView sessionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);
    }
}
