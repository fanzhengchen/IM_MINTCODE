package mintcode.com.workhub_im.im.codebutler;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.BasicNameValuePair;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.net.SocketFactory;
import javax.net.ssl.SSLException;

import mintcode.com.workhub_im.beans.UserPrefer;

/**
 * webSocket 客户端
 *
 * @author ChristLu
 */
public class WebSocketClient {
    private static final String TAG = "WebSocketClient";
    /**
     * 需要连接的服务器url
     */
    private URI mURI;
    private Listener mListener;
    private Socket mSocket;
    private Thread mThread;
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private List<BasicNameValuePair> mExtraHeaders;
    private HybiParser mParser;
    /**
     * 是否连接上
     */
    private boolean mConnected;

    private final Object mSendLock = new Object();

    private Context mContext;

    public WebSocketClient(Context context, URI uri, Listener listener,
                           List<BasicNameValuePair> extraHeaders) {
        mContext = context;
        mURI = uri;
        mListener = listener;
        mExtraHeaders = extraHeaders;
        mConnected = false;
        mParser = new HybiParser(this);

        mHandlerThread = new HandlerThread("websocket-handler-thread");
        Log.i("WebSocketClient", "====================websocket-handler-thread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    public Listener getListener() {
        return mListener;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    /**
     * 调用此方法，使得websocket连接上
     */
    public void connect() {
        if (mThread != null && mThread.isAlive()) {
            return;
        }

        mThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // 获得端口号，如果不能正常获得，则根据传输方式再做判断。如果为wss或者https则使用10000端口，否则使用80端口。
                    int port = (mURI.getPort() != -1) ? mURI.getPort() : ((mURI
                            .getScheme().equals("wss") || mURI.getScheme()
                            .equals("https")) ? 10000 : 80);// 10086

                    String path = TextUtils.isEmpty(mURI.getPath()) ? "/"
                            : mURI.getPath();
                    if (!TextUtils.isEmpty(mURI.getQuery())) {
                        path += "?" + mURI.getQuery();
                    }

                    String originScheme = mURI.getScheme().equals("wss") ? "https"
                            : "http";
                    URI origin = new URI(originScheme, "//" + mURI.getHost(),
                            null);

                    SocketFactory factory = SocketFactory.getDefault();

                    // TODO msocket is create 3-28 17:23
                    if (mSocket == null) {
                        mSocket = factory.createSocket(mURI.getHost(), port);
                        mSocket.setTcpNoDelay(true);
                        mSocket.setKeepAlive(true);
                    }
                    PrintWriter out = new PrintWriter(mSocket.getOutputStream());
                    String secretKey = createSecret();
                    out.print("GET " + path + " HTTP/1.1\r\n");
                    out.print("Upgrade: websocket\r\n");
                    out.print("Connection: Upgrade\r\n");
                    out.print("Host: " + mURI.getHost() + "\r\n");
                    out.print("Origin: " + origin.toString() + "\r\n");
                    out.print("Sec-WebSocket-Key: " + secretKey + "\r\n");
                    out.print("Sec-WebSocket-Version: 13\r\n");
                    out.print("Security: aes\r\n");
                    if (mExtraHeaders != null) {
                        for (NameValuePair pair : mExtraHeaders) {
                            out.print(String.format("%s: %s\r\n",
                                    pair.getName(), pair.getValue()));
                        }
                    }
                    out.print("\r\n");
                    out.flush();
                    // out.close();
                    HybiParser.HappyDataInputStream stream = new HybiParser.HappyDataInputStream(
                            mSocket.getInputStream());

                    // Read HTTP response status line.
                    StatusLine statusLine = parseStatusLine(readLine(stream));
                    if (statusLine == null) {
                        throw new HttpException(
                                "Received no reply from server.");
                    } else if (statusLine.getStatusCode() != HttpStatus.SC_SWITCHING_PROTOCOLS) {
                        throw new HttpResponseException(statusLine
                                .getStatusCode(), statusLine.getReasonPhrase());

                    }

                    // Read HTTP response headers.
                    String line = null;
                    while (!TextUtils.isEmpty(line = readLine(stream))) {

                        Header header = parseHeader(line);
                        if (header.getName().equals("Sec-WebSocket-Accept")) {
                            String expected = expectedKey(secretKey);
                            if (expected == null) {
                                throw new Exception("SHA-1 algorithm not found");
                            } else if (!expected.equals(header.getValue())) {
                                throw new Exception(
                                        "Invalid Sec-WebSocket-Accept, expected: "
                                                + expected + ", got: "
                                                + header.getValue());
                            }
                        } else if (header.getName().equals("Security-Guid")) {
                            String guid = header.getValue();
                            String base = null;
                            try {
                                base = Base64.encodeToString(guid.getBytes("UTF-8"), Base64.DEFAULT);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (base == null) {
                                base = Base64.encodeToString(guid.getBytes(), Base64.DEFAULT);
                            }
                            // TODO 先注释一下
                            Log.d(TAG, "run:guid +===" + guid);
//							KeyValueDBService.getInstance().put(Keys.AES_KEY,base.substring(0,16));
//                            System.out.println("fuck aes key");
                            UserPrefer.setAesKey(base.substring(0, 16));
                        }
                    }

                    if (mListener != null) {
                        mListener.onConnect();
                    }
                    mConnected = true;

                    // Now decode websocket frames.
                    mParser.start(stream);

                } catch (EOFException ex) {
                    Log.d(TAG, "WebSocket EOF!", ex);
                    if (mListener != null)
                        mListener.onDisconnect(0, "EOF");
                    mConnected = false;

                } catch (SSLException ex) {
                    // Connection reset by peer
                    Log.d(TAG, "Websocket SSL error!", ex);
                    if (mListener != null)
                        mListener.onDisconnect(0, "SSL");
                    mConnected = false;

                } catch (Exception ex) {
                    if (mListener != null)
                        mListener.onError(ex);
                }
            }
        }, "websocket-read-thread");
        mThread.start();
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        if (mSocket != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mSocket != null) {
                        try {
                            mSocket.close();
                        } catch (IOException ex) {
                            if (mListener != null)
                                mListener.onError(ex);
                        }
                    }
                    if (mHandlerThread != null) {
                        mHandlerThread.quit();
                        mHandlerThread = null;
                    }
                    mConnected = false;
                }
            });
        }
    }

    public void send(String data) {
        sendFrame(mParser.frame(data));
    }

    public void send(String data, int ping) {
        sendFrame(mParser.frame(data, ping));
    }

    public void send(byte[] data) {
        sendFrame(mParser.frame(data));
    }

    public boolean isConnected() {
        return mConnected;
    }

    private StatusLine parseStatusLine(String line) {
        if (TextUtils.isEmpty(line)) {
            return null;
        }
        return BasicLineParser.parseStatusLine(line, new BasicLineParser());
    }

    private Header parseHeader(String line) {
        return BasicLineParser.parseHeader(line, new BasicLineParser());
    }

    // Can't use BufferedReader because it buffers past the HTTP data.
    private String readLine(HybiParser.HappyDataInputStream reader)
            throws IOException {
        int readChar = reader.read();
        if (readChar == -1) {
            return null;
        }
        StringBuilder string = new StringBuilder("");
        while (readChar != '\n') {
            if (readChar != '\r') {
                string.append((char) readChar);
            }

            readChar = reader.read();
            if (readChar == -1) {
                return null;
            }
        }
        return string.toString();
    }

    private String expectedKey(String secret) {
        // concatenate, SHA1-hash, base64-encode
        try {
            final String GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
            final String secretGUID = secret + GUID;
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(secretGUID.getBytes());
            return Base64.encodeToString(digest, Base64.DEFAULT).trim();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private String createSecret() {
        byte[] nonce = new byte[16];
        for (int i = 0; i < 16; i++) {
            nonce[i] = (byte) (Math.random() * 256);
        }
        return Base64.encodeToString(nonce, Base64.DEFAULT).trim();
    }

    void sendFrame(final byte[] frame) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (mSendLock) {
                        OutputStream outputStream = mSocket.getOutputStream();
                        outputStream.write(frame);
                        Log.i("webSocket", "===============sendFrame");
                        outputStream.flush();
                    }
                } catch (IOException e) {
                    if (mListener != null)
                        mListener.onError(e);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    if (mListener != null)
                        mListener.onError(e);
                }
            }
        });
    }

    public interface Listener {
        /**
         * 第一次连接到服务器后回调此方法
         */
        void onConnect();

        void onMessage(String message);

        void onMessage(byte[] data);

        void onDisconnect(int code, String reason);

        void onError(Exception error);
    }

}
