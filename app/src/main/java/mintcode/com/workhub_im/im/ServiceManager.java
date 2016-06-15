package mintcode.com.workhub_im.im;

import android.content.Context;
import android.text.TextUtils;

import java.net.URI;
import java.util.ArrayList;

import mintcode.com.workhub_im.App;
import mintcode.com.workhub_im.beans.UserPrefer;
import mintcode.com.workhub_im.im.codebutler.WebSocketClient;
import mintcode.com.workhub_im.im.pojo.IMSessionResponse;
import mintcode.com.workhub_im.im.pojo.Message;
import mintcode.com.workhub_im.im.pojo.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mark on 16-6-14.
 */
public class ServiceManager {

    private Context context;
    private WebSocketClient socketClient;
    private WebSocketClient.Listener listener;
    private String IP;
    private static ServiceManager serviceManager;
    private static int start = 0;
    private static int end = 200;
    private ArrayList<Session> sessions;

    private ServiceManager(Context context) {
        this.context = context;
    }

    public static ServiceManager getInstance() {
        if (serviceManager == null) {
            serviceManager = new ServiceManager(App.getGlobalContext());
        }
        return serviceManager;
    }

    public void socketConnect() {
        if (TextUtils.isEmpty(IP)) {
            IP = UserPrefer.getIpAddress();
        }

        if (TextUtils.isEmpty(IP)) {
            return;
        }

        URI uri = URI.create(IP);
        if (socketClient != null) {
            synchronized (socketClient) {
                socketClient.disconnect();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                socketClient.setListener(null);
                socketClient = null;
                socketClient = new WebSocketClient(context, uri, listener, null);
                socketClient.setListener(listener);
                socketClient.connect();
            }
        } else {
            socketClient = new WebSocketClient(context, uri, listener, null);
        }
        socketClient.setListener(listener);
        socketClient.connect();
    }

    public void connect() {
        IMAPIProvider.getUnreadSession(start, end, new Callback<IMSessionResponse>() {
            @Override
            public void onResponse(Call<IMSessionResponse> call, Response<IMSessionResponse> response) {
                sessions = response.body().getSessions();
                updateSessions(sessions);
            }

            @Override
            public void onFailure(Call<IMSessionResponse> call, Throwable t) {

            }
        });
    }

    private void updateSessions(ArrayList<Session> sessions) {
        for (Session session : sessions) {
            if (session == null || session.getMessage() == null) {
                continue;
            }
            Message message = session.getMessage();


        }
    }
}
