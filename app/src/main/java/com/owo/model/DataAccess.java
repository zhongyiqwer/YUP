package com.owo.model;

import android.content.Context;

import com.owo.utils.util_db.DBServer;

public class DataAccess {
	public void initDatabase(Context context) {
		DBServer dbServer = new DBServer(context);
		User user = new User();
		user.setId(-1);
		user.setPhoneNumber("");
		user.setPassWord("");
		dbServer.addUser(user);
	}
}
