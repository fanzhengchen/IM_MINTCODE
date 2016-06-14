package mintcode.com.workhub_im.activities;

import android.app.Activity;
import android.os.Bundle;

import mintcode.com.workhub_im.Http.IMService;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.im.IMAPIProvider;
import mintcode.com.workhub_im.im.pojo.IMLoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mark on 16-6-8.
 */
public class SessionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_list);
    }


}
