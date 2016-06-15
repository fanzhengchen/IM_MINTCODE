package com.mintcode.launchr.contact;

import java.util.List;

import com.mintcode.launchr.pojo.entity.UserDetailEntity;

public interface OnSelectUserListener {
	
	void onSelectUser(List<UserDetailEntity> userList, List<Integer> positionList);
	
}
