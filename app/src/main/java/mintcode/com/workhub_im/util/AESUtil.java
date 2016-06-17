package mintcode.com.workhub_im.util;

import com.mintcode.imkit.crypto.Base64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by mark on 16-6-17.
 */
public class AESUtil {
    private static String keyParams = "zeus-2013perfect";

    private static byte[] ivbyteParams = {5, 4, 0xF, 7, 9, 0xC, 1, 0xB, 3, 91, 0xD, 0x17, 1, 0xA, 6, 8};

    private static final String keyServer = "Mintcode.Launchr";

    private static byte[] ivbyteServer = {0xC, 1, 0xB, 3, 0x5B, 0xD, 5, 4, 0xF, 7, 9, 0x17, 1, 0xA, 6, 8};

    private static final String keyClient = "zeusmint2013code";

    private static byte[] ivbyteClient = {0x38, 0x31, 0x37, 0x34, 0x36, 0x33, 0x35, 0x33, 0x32, 0x31, 0x34, 0x38, 0x37, 0x36, 0x35, 0x32};


    public static String EncryptParams(String sSrc) {
        return Encrypt(sSrc, keyParams, ivbyteParams);
    }

    public static String DecryptParams(String sSrc) {
        return Decrypt(sSrc, keyParams, ivbyteParams);
    }

    public static String DecryptServerAuthToken(String authToken) {
        return Decrypt(authToken, keyServer, ivbyteServer);
    }

    public static String EncryptClientAuthToken(String authToken) {
        return Encrypt(authToken, keyClient, ivbyteClient);
    }


    public static String EncryptIM(String content, String key) {
        return Encrypt(content, key, ivbyteServer);
    }

    public static String DecryptIM(String json, String key) {
        return Decrypt(json, key, ivbyteServer);
    }

    // 加密
    public static String Encrypt(String sSrc, String key, byte[] ivbyte) {
        if (key == null) {
            return null;
        }
        // 判断Key是否为16位
        if (key.length() != 16) {
            return null;
        }

        String rlt = null;
        try {
            byte[] raw = key.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");// "算法/模式/补码方式"
            IvParameterSpec iv = new IvParameterSpec(ivbyte);// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));
            rlt = Base64Encoder.encode(encrypted);// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return rlt;
    }

    // 解密
    public static String Decrypt(String sSrc, String key, byte[] ivbyte) {
        // 判断Key是否正确
        if (key == null) {
            return null;
        }
        // 判断Key是否为16位
        if (key.length() != 16) {
            return null;
        }

        try {
            byte[] raw = key.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            IvParameterSpec iv = new IvParameterSpec(ivbyte);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64Decoder.decodeToBytes(sSrc);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "UTF-8");
            return originalString;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
