package com.mintcode.chat.user;

import java.util.List;

public class NDNManager {

	public static NDNManager sInstance;
	
	private static List<GroupInfo> NDNList;
	
	private NDNManager(){
		NDNList = GroupInfoDBService.getIntance().getDND();
	};
	
	public static NDNManager getInstance(){
		if (sInstance == null) {
			sInstance = new NDNManager();
		}
		return sInstance;
	}
	
	public void updateNDNList(){
		NDNList = GroupInfoDBService.getIntance().getDND();
	}
	
	
	public List<GroupInfo> getNDNList(){
		return NDNList;
	}
}
