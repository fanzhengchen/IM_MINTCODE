package com.mintcode.launchrnetwork;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.zip.GZIPInputStream;


public class MTHttpManager {
    public static String TAG = "MTHttpManager";


    /**
     * http的访问形式——get
     */
    public static final String HTTP_GET = "GET";

    /**
     * http的访问形式——post
     */
    public static final String HTTP_POST = "POST";

    /**
     * http返回200，一切正常~:D
     */
    private static final int HTTP_OK = 200;

    /**
     * 进行http的请求
     *
     * @param url        目标url
     * @param method     http的访问形式{@link #HTTP_GET}和{@link #HTTP_POST}
     * @param params     key-value的{@link MTHttpParameters}字典数组
     * @param getRawData 是否是流格式的
     * @return
     */
    public static Object openUrl(String url, String method, MTHttpParameters params, boolean getRawData) {
        Object result = "";
        HttpClient client = getNewHttpClient();
        HttpUriRequest request = null;
        try {
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, NetStateManager.getAPN());

            if (method.equalsIgnoreCase(HTTP_GET)) {
                url = url + MTURIUtil.encodeUrl(params);
                HttpGet get = new HttpGet(url);
                request = get;
            } else if (method.equalsIgnoreCase(HTTP_POST)) {
                HttpPost post = new HttpPost(url);
                post.setHeader("Content-Type", "application/json; charset=utf-8");
                byte[] data = null;
                String jsonData = params.toJson();
                data = jsonData.getBytes();
                ByteArrayEntity formEntity = new ByteArrayEntity(data);
                post.setEntity(formEntity);
                request = post;
            }
            //是否要带入操作系统和程序版本？？@RobinLin
//			request.addHeader("Platform", MTConst.OS);
//			request.addHeader("version",MTConst.VERSION);
//			request.addHeader("Accept", "application/json");
            request.addHeader("Accept", "text/html,application/xhtml+xml,application/json,application/xml,*/*;q=0.8");
            request.addHeader("Cache-Control", "max-age=0");
            request.addHeader("Accept-Encoding", "gzip,deflate,sdch");
            request.addHeader("Connection", "close");
//			request.addHeader("Host", "a.launchr.jp");
//			request.addHeader("Range", "bytes=0-0");/


            HttpResponse response = client.execute(request);

            StatusLine status = response.getStatusLine();

            int statusCode = status.getStatusCode();
            if (statusCode != HTTP_OK) {
                throw new MTException("HTTP:" + statusCode + " ; " + result.toString(), MTResultCode.INNER_NET_SERVER_FAILED);
            }
            if (getRawData) {
                result = getHttpResponseAsStream(response);
            } else {
                String responseStr = readHttpResponse(response);
                result = responseStr;
            }
//			client.getConnectionManager().closeExpiredConnections();
//			client.getConnectionManager().shutdown();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
//			client.getConnectionManager().closeExpiredConnections();
//			client.getConnectionManager().shutdown();
            throw new MTException(e.getMessage(), MTResultCode.INNER_NET_SERVER_FAILED);
        }
    }

    /**
     * 将response转成流形式
     *
     * @param response
     * @return
     * @throws IOException
     */
    private static InputStream getHttpResponseAsStream(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        InputStream inputStream;
        try {
            inputStream = entity.getContent();
            Header header = response.getFirstHeader("Content-Encoding");
            if (header != null && header.getValue().toLowerCase().contains("gzip")) {
                inputStream = new GZIPInputStream(inputStream);
            }
            return inputStream;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            throw e;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw e;
        }
    }

    /**
     * 将response转成string
     *
     * @param response
     * @return
     * @throws IOException
     */
    private static String readHttpResponse(HttpResponse response) throws IOException {
        String result = null;
        HttpEntity entity = response.getEntity();
        InputStream inputStream = null;
        BufferedReader br = null;
        try {
            inputStream = entity.getContent();
            Header header = response.getFirstHeader("Content-Encoding");
            if (header != null && header.getValue().toLowerCase().contains("gzip")) {
                inputStream = new GZIPInputStream(inputStream);
            }

            br = new BufferedReader(new InputStreamReader(inputStream));
            String output;
            StringBuffer retValue = new StringBuffer();
            while ((output = br.readLine()) != null) {
                retValue.append(output);
                retValue.append(" ");
            }
            result = retValue.toString();
        } catch (IllegalStateException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (br != null) {
                br.close();
            }
        }
        return result;
    }

    /**
     * 生成一个HttpClient
     *
     * @return
     */
    private static HttpClient getNewHttpClient() {
        try {
//			HttpParams params = new BasicHttpParams();
//			
//			HttpConnectionParams.setConnectionTimeout(params, MTConst.CONNECTION_TIMEOUT);
//			HttpConnectionParams.setSoTimeout(params, MTConst.SOCKET_TIMEOUT);
//	
//			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
//
//			HttpClient client = new DefaultHttpClient(params);
//			return client;
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
}
