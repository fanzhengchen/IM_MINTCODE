package mintcode.com.workhub_im.Http;

import java.io.File;

import mintcode.com.workhub_im.App;
import mintcode.com.workhub_im.AppConsts;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fanzhengchen on 6/12/16.
 */
public class HttpProvider {


    private static final String SERVER_PATH = "http://api.mintcode.com";
    private static final String ATTACH_PATH = "http://a.mintcode.com";
    private static final String ip = "ws://imws.mintcode.com:20000";
    private static final String httpIp = "http://imhttp.mintcode.com/launchr/";
    private static final String SEPARATOR = "/";
    private APIService apiService;
    private IMService imService;
    private static HttpProvider httpProvider = new HttpProvider();


    public static HttpProvider getInstance() {
        if (httpProvider == null) {
            httpProvider = new HttpProvider();
        }
        return httpProvider;
    }

    private HttpProvider() {
        apiService = createAPIService();
        imService = createIMService();
    }


    private APIService createAPIService() {
        return new Retrofit.Builder()
                .baseUrl(AppConsts.SERVER_PATH + SEPARATOR)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIService.class);
    }

    private IMService createIMService() {
        return new Retrofit.Builder()
                .baseUrl(AppConsts.httpIp + SEPARATOR + AppConsts.LAUNCHR + SEPARATOR)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IMService.class);
    }

    public IMService getImService() {
        if (imService == null) {
            imService = createIMService();
        }
        return imService;
    }

    public APIService getApiService() {
        if (apiService == null) {
            apiService = createAPIService();
        }
        return apiService;

    }
}
