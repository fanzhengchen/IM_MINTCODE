package mintcode.com.workhub_im.Http;

import com.mintcode.imkit.pojo.LoginResultPOJO;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by fanzhengchen on 6/12/16.
 */
public interface IMService {

    public static final String IM_LOGIN = "/api/login";

    @POST(IM_LOGIN)
    public Call<LoginResultPOJO> IMLogin(
            @Query("appName") String appName,
            @Query("appToken") String appToken,
            @Query("userName") String userName,
            @Query("deviceUUID") String deviceUUID,
            @Query("deviceName") String deviceName,
            @Query("os") String os,
            @Query("osVer") String osVer,
            @Query("appVer") String appVer
    );
}
