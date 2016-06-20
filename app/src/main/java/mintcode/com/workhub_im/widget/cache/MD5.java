package mintcode.com.workhub_im.widget.cache;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	private static final String _MD5 = "MD5";
	private static final String _UTF8 = "UTF-8";
	
	public static String getMD5Str(String str) {  
        MessageDigest messageDigest = null;  
        if(str == null){
        	return null;
        }
        try {  
            messageDigest = MessageDigest.getInstance(_MD5);  
            messageDigest.reset();  
            messageDigest.update(str.getBytes(_UTF8));  
        } catch (NoSuchAlgorithmException e) {  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
  
        byte[] byteArray = messageDigest.digest();  
  
        StringBuffer md5StrBuff = new StringBuffer();  
  
        for (int i = 0; i < byteArray.length; i++) {              
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
            else  
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
        }  
  
        return md5StrBuff.toString();  
    }  
}
