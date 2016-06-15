package com.mintcode.chat.user;

import java.util.List;

import org.litepal.crud.DataSupport;

public class GroupInfoDBService {

	private static GroupInfoDBService sInstance;

	private GroupInfoDBService() {
	};

	public static GroupInfoDBService getIntance() {
		if (sInstance == null) {
			sInstance = new GroupInfoDBService();
		}
		return sInstance;
	}

	public void insert(GroupInfo groupInfo) {
		groupInfo.save();
	}

	public void update(GroupInfo groupInfo) {
		int i = groupInfo.updateAll("userName = ?", groupInfo.getUserName());
	}

	public List<GroupInfo> getDND() {
		List<GroupInfo> DNDList = DataSupport.where("isDND = 1").find(
				GroupInfo.class);
		return DNDList;
	}
	
	public GroupInfo getGroupInfo(String userName){
		List<GroupInfo> list = DataSupport.where("userName = ?",userName).find(GroupInfo.class);
		if (list.size() > 0) {
			return list.get(0);
		}else{
			return null;
		}
	}

	public boolean exist(GroupInfo groupInfo) {
		List<GroupInfo> find = DataSupport.where("userName = ?",
				groupInfo.getUserName()).find(GroupInfo.class);
		if (find.size() > 0) {
//			groupInfo.setIsDND(find.get(0).getIsDND());
			return true;
		}else{
			return false;
		}
	}

	public void put(GroupInfo groupInfo) {
		if (exist(groupInfo)) {
			update(groupInfo);
		} else {
			insert(groupInfo);
		}
		NDNManager.getInstance().updateNDNList();
	}
}
