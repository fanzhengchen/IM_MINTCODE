package com.mintcode.launchrnetwork;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MTURIUtil {
	private static final String TAG = "URIUtil";
	private static final boolean DC = false;
	private static final boolean D = (DC);

	public static String create(String baseUrl, String[] paraName,
			Object[] paraValue, String authToken) {
		return create(baseUrl, paraName, paraValue, true, authToken);
	}

	public static String create(String baseUrl, List<String> paraName,
			List<String> paraValue, String authToken) {
		String[] paraNames = (String[]) paraName.toArray();
		String[] paraValues = (String[]) paraValue.toArray();
		return create(baseUrl, paraNames, paraValues, authToken);
	}

	/**
	 * 将参数字典组装参数字符串
	 * 
	 * @param baseUrl
	 * @param paraName
	 * @param paraValue
	 * @param isNeedToken
	 * @param authToken
	 * @return
	 */
	public static String create(String baseUrl, String[] paraName,
			Object[] paraValue, boolean isNeedToken, String authToken) {
		if (paraName == null || paraValue == null) {
			return null;
		}
		if (paraName.length != paraValue.length) {
			return null;
		}
		StringBuilder para = new StringBuilder("");
		if (paraName.length == 0)
			return baseUrl;
		para.append(paraName[0]).append("=").append(paraValue[0]);
		for (int i = 1; i < paraName.length; i++) {
			para.append("&").append(paraName[i]).append("=")
					.append(paraValue[i]);
		}
		if (isNeedToken) {
			if (authToken != null && !authToken.equals("")) {
				para.append("&token=").append(authToken);
			}
		}
		String url = baseUrl + para;

		return url;
	}

	/**
	 * only use in get method.
	 * 
	 * @param parameters
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeUrl(MTHttpParameters parameters)
			throws UnsupportedEncodingException {
		if (parameters == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		boolean first = true;
		Set<String> keySet = parameters.getKeySet();
		for (Iterator<String> ite = keySet.iterator(); ite.hasNext();) {
			String key = ite.next();
			Object rawValue = parameters.getParameter(key);
			if (rawValue == null) {
			} else {
				if (first) {
					sb.append("?");
					first = false;
				} else {
					sb.append("&");
				}
				String value = String.valueOf(rawValue);
				sb.append(URLEncoder.encode(key, "utf-8") + "="
						+ URLEncoder.encode(value, "utf-8"));
			}
		}
		return sb.toString();
	}
}
