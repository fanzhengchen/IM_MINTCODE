package com.mintcode.im.util;

import java.text.DecimalFormat;

import com.mintcode.im.database.KeyValueDBService;
import com.mintcode.launchr.R;

public class IMConst {

	public static final boolean DBGALL = true;

	public static final String KEY_AUTH_TOKEN = "KEY_IM_AUTH_TOKEN";
	public static final String KEY_TIME_GAP = "KEY_IM_TIME_GAP";
	public static final String KEY_MSG = "KEY_MSG";
	
	public static final String AUDIO_MEDIA_TYPE = ".amr";

	public static long getCurrentTime() {
		String timeGap = KeyValueDBService.getInstance().find(Keys.TIME_GAP);
		if (timeGap == null) {
			return System.currentTimeMillis();
		}
		return System.currentTimeMillis() + Long.valueOf(timeGap);
	}

	public static String FormetFileSize(int fileSize) {
		DecimalFormat df = new DecimalFormat("#0.0");
		String fileSizeString = "";
		String wrongSize = "0B";
		if (fileSize == 0) {
			return wrongSize;
		}
		if (fileSize < 1048576) {
			fileSizeString = df.format((double) fileSize / 1024) + "KB";
		} else if (fileSize < 1073741824) {
			fileSizeString = df.format((double) fileSize / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileSize / 1073741824) + "GB";
		}
		return fileSizeString;
	}
	
	public static int getFileIcon(String fileName){
		String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
		if("doc".equals(prefix) || "docx".equals(prefix) || "DOC".equals(prefix) || "DOCX".equals(prefix)){
			return R.drawable.icon_chat_file_doc;
		}else if("html".equals(prefix) || "HTML".equals(prefix)){
			return R.drawable.icon_chat_file_html5;
		}else if("folder".equals(prefix) || "FOLDER".equals(prefix)){
			return R.drawable.icon_chat_file_folder;
		}else if("png".equals(prefix) || "jpg".equals(prefix) || "jpeg".equals(prefix)){
			return R.drawable.icon_chat_file_image;
		}else if("pdf".equals(prefix) || "PDF".equals(prefix)){
			return R.drawable.icon_chat_file_pdf;
		}else if("ppt".equals(prefix) || "pptx".equals(prefix) || "PPT".equals(prefix) || "PPTX".equals(prefix)){
			return R.drawable.icon_chat_file_ppt;
		}else if("rar".equals(prefix) || "zip".equals(prefix) || "RAR".equals(prefix) || "ZIP".equals(prefix)){
			return R.drawable.icon_chat_file_rar;
		}else if("txt".equals(prefix) || "TXT".equals(prefix)){
			return R.drawable.icon_chat_file_txt;
		}else if("xml".equals(prefix) || "XML".equals(prefix)){
			return R.drawable.icon_chat_file_xml;
		}else if("xsl".equals(prefix) || "xslx".equals(prefix) || "XSL".equals(prefix) || "XSLX".equals(prefix)){
			return R.drawable.icon_chat_file_xsl;
		}else{
			return R.drawable.icon_chat_file_unknown;
		}
	}

}
