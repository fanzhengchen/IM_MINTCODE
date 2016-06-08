package mintcode.com.workhub_im.Http;

import android.util.Log;

import java.io.IOException;

import mintcode.com.workhub_im.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by mark on 16-6-8.
 */
public class LogJsonInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Response response = chain.proceed(request);
        String rawJson = response.body().string();

        Log.d(BuildConfig.APPLICATION_ID, String.format("raw JSON response is: %s", rawJson));

// Re-create the response before returning it because body can be read only once
        return response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), rawJson)).build();
    }

}
