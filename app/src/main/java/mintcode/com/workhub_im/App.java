package mintcode.com.workhub_im;

import android.app.Application;

import mintcode.com.workhub_im.Http.APIService;
import mintcode.com.workhub_im.Http.LogJsonInterceptor;
import mintcode.com.workhub_im.beans.UserPrefer;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mark on 16-6-8.
 */
public class App extends Application {
    private static final String SERVER_PATH = "http://api.mintcode.com";
    private static final String ATTACH_PATH = "http://a.mintcode.com";
    private static final String ip = "ws://imws.mintcode.com:20000";
    private static final String httpIp = "http://imhttp.mintcode.com";

    private static Retrofit retrofitClient = null;
    private static String baseUrl = SERVER_PATH;
    private static OkHttpClient httpClient = null;
    private static APIService apiService = null;

    @Override
    public void onCreate() {
        super.onCreate();
        UserPrefer.init(getApplicationContext());
    }

    public static Retrofit getRetrofit() {
        if (retrofitClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new LogJsonInterceptor())
                    .build();
//            httpClient.interceptors().add(new LogJsonInterceptor());
            retrofitClient = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return retrofitClient;
    }

    public static APIService getApiService() {
        if (apiService == null) {
            apiService = getRetrofit().create(APIService.class);
        }
        return apiService;
    }
}
