package mintcode.com.workhub_im.activities;

import android.app.Activity;
import android.os.Bundle;

import com.mintcode.imkit.pojo.LoginResultPOJO;

import mintcode.com.workhub_im.App;
import mintcode.com.workhub_im.Http.IMService;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.beans.UserPrefer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mark on 16-6-8.
 */
public class SessionActivity extends Activity {


    private IMService imService;
    private String showId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_list);
        imService = App.getImService();
        showId = UserPrefer.getShowId();
        login();
    }

    private void login() {
      imService.IMLogin("IM DEMO",
                "verify-code",
                showId,
                App.getDeviceUUID(this),
                App.getDeviceName(),
                "android",
                App.getOsVer(),
                App.getAppVer(this)).enqueue(new Callback<LoginResultPOJO>() {
          @Override
          public void onResponse(Call<LoginResultPOJO> call, Response<LoginResultPOJO> response) {

          }

          @Override
          public void onFailure(Call<LoginResultPOJO> call, Throwable t) {

          }
      });

    }
}
