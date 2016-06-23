package mintcode.com.workhub_im.im;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mark on 16-6-16.
 */
public class Command {
    /***********************************************消息状态***********************************************************/
    /**
     * 发送成功
     */
    public static final int SEND_SUCCESS = 1;
    /**
     * 发送失败
     */
    public static final int SEND_FAILED = 0;
    /**
     * 消息状态表中 标志消息正在发送的状态
     */
    public static final int STATE_SEND = 2;
    /**
     * 已读
     */
    public static final int READ = 1;
    /**
     * 未读
     */
    public static final int UNREAD = 0;
    /**
     * 收到回执
     */
    public static final int REV_RESP = 1;
    /**
     * 没有收到回执
     */
    public static final int NOT_REV_RESP = 0;

    /***************************** 消息类型码 ********************************************************************/

    /**
     * 文本
     */
    public static final String TEXT = "Text";

    /**
     * 语音
     */
    public static final String AUDIO = "Audio";

    /**
     * 图片
     */
    public static final String IMAGE = "Image";

    /**
     * 小视频
     */
    public static final String VIDEO = "Video";

    /**
     * 系统消息
     */
    public static final String ALERT = "Alert";

    /**
     * 事件消息
     */
    public static final String EVENT = "Event";

    /**
     * 登出
     */
    public static final String LOGINOUT = "LoginOut";

    /**
     * 文件
     */
    public static final String FILE = "File";

    /**
     * 消息已读推送
     */
    public static final String READ_SESSION = "Read";

    /**
     * 消息撤回重发
     */
    public static final String RESEND = "ReSend";

    /**
     * 消息回执
     */
    public static final String REV = "Rev";

    /**
     * 消息转发
     */
    public static final String MERGE = "MergeMessage";


    public static final Set<String> normalMessageTypeSet;

    static {
        normalMessageTypeSet = new HashSet<>();
        normalMessageTypeSet.add(TEXT);
        normalMessageTypeSet.add(AUDIO);
        normalMessageTypeSet.add(IMAGE);
        normalMessageTypeSet.add(ALERT);
        normalMessageTypeSet.add(VIDEO);
        normalMessageTypeSet.add(READ_SESSION);
        normalMessageTypeSet.add(EVENT);
        normalMessageTypeSet.add(FILE);
        normalMessageTypeSet.add(RESEND);
        normalMessageTypeSet.add(MERGE);
    }

    public static boolean isNormalMessage(String messageType) {
        return normalMessageTypeSet.contains(messageType);
    }

    public static final String CLEAR = "Clear";


    public static final String CMD = "Cmd";

    public static final String OPEN = "Open";

    public static final String MARK = "Mark";

    public static final String CANCEL_MARK = "CancelMark";

    public static final String REMOVE = "Remove";


    /********************************* 公共部分 *********************************************************/
    /**
     * 系统消息，登录
     */
    public static final String LOGIN = "Login";

    /**
     * 心跳指令
     */
    public static final String LOGIN_KEEP = "LoginKeep";

    /**
     * 登录到IM服务器成功
     */
    public static final String LOGIN_SUCCESS = "LoginIn";

    /**
     * 登录失败或者被注销
     */
    public static final String LOGIN_OUT = "LoginOut";


    /**
     * 系统消息，登出
     */
    public static final int OUT = 10002;
    /**
     * 服务器收到消息回执 登录回执
     */
    public static final int LOGIN_RETURN = 10004;
    /**
     * 消息回执 语音&文本
     */
    public static final int MSG_RETURN = 10000;

    public static final int RCV = 10008;
    /**
     * 消息设置 状态 on off
     */
    public static final int MSG_OPEN = 1;
    public static final int MSG_OFF = 0;

}
