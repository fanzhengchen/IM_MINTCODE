package com.mintcode.im.crypto;

import android.content.Context;
import android.content.SharedPreferences;

import com.mintcode.im.database.KeyValueDBService;
import com.mintcode.im.util.IMConst;
import com.mintcode.im.util.Keys;


public class AuthTokenUtil {
	
	private static final int AUTH_TOKEN_LENGTH = 16;
	private static String token;
	
	public AuthTokenUtil() {
		token = KeyValueDBService.getInstance().find(Keys.TOKEN);
	}
	
	
	public static String getEncryptedAuthToken() {
		return AESUtil.EncryptClientAuthToken(getClientAuthToken());
	}
	
	public static String getClientAuthToken() {
		
		return getClientAuthToken(AESUtil.DecryptServerAuthToken(token));
	}

	public static String getEncryptedAuthToken(Context c){
		if(token != null && !token.equals("")){
			return token;
		}else{
			SharedPreferences sp = c.getSharedPreferences(c.getPackageName(), Context.MODE_PRIVATE);
			return sp.getString(IMConst.KEY_AUTH_TOKEN, null);
		}
	}
	
	public static String getClientAuthToken(String encryptedAuthToken) {
		char[] server = encryptedAuthToken.toCharArray();
		if (server == null || server.length != AUTH_TOKEN_LENGTH) {
			return null;
		}
		
		char[] client = new char[AUTH_TOKEN_LENGTH];
		char[] time = new char[6];
		char[] token = new char[10];
		
		//
		// parse the serverAuthToken, get the token & time
		//
		
		token[0] = server[0];
		token[2] = server[1];
		token[4] = server[2];
		token[6] = server[3];
		token[8] = server[4];
		
		time[1] = server[5];
		time[3] = server[6];
		time[5] = server[7];
		
		token[1] = server[8];
		token[3] = server[9];
		token[5] = server[10];
		token[7] = server[11];
		token[9] = server[12];
		
		time[0] = server[13];
		time[2] = server[14];
		time[4] = server[15];
		
		//
		// generate clien auth token
		//

		client[0] = time[1];
		client[1] = time[3];
		client[2] = time[5];

		client[3] = token[0];
		client[4] = token[2];
		client[5] = token[4];
		client[6] = token[6];
		client[7] = token[8];

		client[8] = time[0];
		client[9] = time[2];
		client[10] = time[4];

		client[11] = token[1];
		client[12] = token[3];
		client[13] = token[5];
		client[14] = token[7];
		client[15] = token[9];
		
		return new String(client);
	}
}
