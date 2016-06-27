package mintcode.com.workhub_im.im;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import mintcode.com.workhub_im.activities.ChatActivity;
import mintcode.com.workhub_im.db.MessageItem;
import mintcode.com.workhub_im.im.pojo.AttachDetail;
import mintcode.com.workhub_im.widget.upload.ResultActionEntity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by mark on 16-6-27.
 */
public class Uploader {
    public final static String TAG = "UploadUtil";

    public static interface WriteCallback {
        void write(OutputStream out) throws IOException;
    }

    public static byte[] getWrappedBuffer(String text) {
        byte[] bytes = null;
        try {
            String json = new String(text.getBytes(), "UTF-8");
            bytes = json.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int len = bytes.length;
        byte[] buffer = new byte[len + 4];
        buffer[0] = (byte) (len & 0xFF);
        buffer[1] = (byte) ((len >>> 8) & 0xFF);
        buffer[2] = (byte) ((len >>> 16) & 0xFF);
        buffer[3] = (byte) ((len >>> 24) & 0xFF);
        System.arraycopy(bytes, 0, buffer, 4, len);
        return buffer;


    }

    /**
     * 上传图片 聊天
     *
     * @param
     * @param
     * @param file
     * @return
     */
    //TODO 头像分包上传
    public static String uploadPic(String url, AttachDetail detail, final File file) {
        detail.setSrcOffset(0);
//		detail.setFileUrl("");
        detail.setFileStatus(0);
        ResultActionEntity entity = uploadPart(url, file, detail);
//		String s = uploadPart(url, detail, null,file);
        String result = JSON.toJSONString(entity);
        return result;
    }


    public static ResultActionEntity uploadPart(String urlSpec, File file, AttachDetail detail) {
        long filePosition = detail.getSrcOffset();

        byte[] tmp = getByteByPosition(file, 0);
        ResultActionEntity pojo = null;
        while (tmp.length > 0) {
            pojo = null;
            if (tmp.length < BUFFER_SIZE) {  // the last upload stream
                detail.setFileStatus(1);
            }
            try {
                Logger.json(JSON.toJSONString(detail));
                pojo = uploadPart(urlSpec, detail, tmp);
                if (pojo == null) {
                    return null;
                }
                int resCode = pojo.getCode();
                if (resCode == 2000) {
                    long offset = pojo.getFileSize();
                    String filePath = pojo.getFileUrl();
                    detail.setSrcOffset((int) offset);
                    detail.setFileUrl(filePath);
                }
                filePosition += tmp.length;
                tmp = getByteByPosition(file, filePosition);
                // 发送进度消息
//                Message msg = Message.obtain();
//                msg.what = ChatActivity.HandleMsgType.TYPE_FILE_UPLOAD;
//                msg.obj = item;
                long percent = (pojo.getFileSize() * 100) / file.length();
//                item.setPercent(percent + "");
//                handler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return pojo;
    }


    public static int BUFFER_SIZE = 100 * 1024;

    private static byte[] getByteByPosition(File file, long pos) {
        byte[] tmp = null;
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            FileChannel channel = raf.getChannel();
            long fileSize = raf.length();
            raf.seek(pos);
            long position = channel.position();
            int bufferSize = (int) ((fileSize - position < BUFFER_SIZE) ? (fileSize - position) : BUFFER_SIZE);
            tmp = new byte[bufferSize];
            raf.read(tmp);
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return tmp;
    }

    private static ResultActionEntity uploadPart(String urlSpec, final AttachDetail detail,
                                                 final byte[] block) {
        ResultActionEntity pojo = null;
        pojo = upload(urlSpec, new WriteCallback() {

            @Override
            public void write(OutputStream out) throws IOException {
                String jsonStr = JSON.toJSONString(detail);
//                Log.i("infoss", "-------" + "====" + jsonStr);
//				System.out.println(jsonStr);
                out.write(getWrappedBuffer(jsonStr));
                out.write(block);

            }
        });

        return pojo;
    }

    public static ResultActionEntity upload(String urlSpec, final WriteCallback callback) {
//        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain",callback.))
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.parse("text/plain; charset=utf-8");
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                callback.write(sink.outputStream());
            }
        };

        Request request = new Request.Builder()
                .url(urlSpec)
                .addHeader("Charset", "UTF-8")
                .addHeader("Accept-Charset", "UTF-8")
                .addHeader("Accept", "application/json")
                .post(requestBody)
                .build();
        ResultActionEntity resultActionEntity = null;
        try {
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            Logger.json(body);
            resultActionEntity = JSON.parseObject(body, ResultActionEntity.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultActionEntity;
    }
}
