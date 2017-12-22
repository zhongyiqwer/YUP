package com.owo.utils.util_db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.owo.model.User;

import java.util.ArrayList;


/**
 *
 *
 */
public class DBServer {

    private DBHelper dbHelper;

    public DBServer(Context context)

    {

        this.dbHelper = new DBHelper(context);
    }

    // //////////////USER/////////////////////
    public void addUser(User user) {
        SQLiteDatabase localSQLiteDatabase = this.dbHelper
                .getWritableDatabase();

        Object[] arrayOfObject = new Object[3];
        arrayOfObject[0] = user.getId();
        arrayOfObject[1] = user.getPhoneNumber();
        arrayOfObject[2] = user.getPassWord();

        localSQLiteDatabase.execSQL("insert into user(user_id,phone,login_edittext_drawable_left_password) values(?,?,?)",
                arrayOfObject);
        localSQLiteDatabase.close();
    }

    /**
     * 查询本地数据库中的所有用户
     *
     * @return 用户容器
     */

    public ArrayList<User> findUser() {
        ArrayList<User> user = new ArrayList<>();
        SQLiteDatabase localSQLiteDatabase = this.dbHelper
                .getWritableDatabase();
        Cursor localCursor = localSQLiteDatabase.rawQuery("select *  from user", null);

        while (localCursor.moveToNext()) {
            User temp = new User();
            temp.setId(localCursor.getInt(localCursor
                    .getColumnIndex("user_id")));
            temp.setPhoneNumber(localCursor.getString(localCursor
                    .getColumnIndex("phone")));
            temp.setPassWord(localCursor.getString(localCursor
                    .getColumnIndex("login_edittext_drawable_left_password")));
            user.add(temp);
        }
        localSQLiteDatabase.close();
        return user;
    }

    public void updateUser(String phone, String password) {
        SQLiteDatabase localSQLiteDatabase = this.dbHelper
                .getWritableDatabase();
        localSQLiteDatabase.execSQL("update user set phone='" + phone + "',login_edittext_drawable_left_password='" + password + "'");
        localSQLiteDatabase.close();
    }

    public void updateUser(int userId, String phone, String password) {
        SQLiteDatabase localSQLiteDatabase = this.dbHelper
                .getWritableDatabase();
        localSQLiteDatabase.execSQL("update user set user_id=" + userId + " ,phone='" + phone + "',login_edittext_drawable_left_password='" + password + "'");
        localSQLiteDatabase.close();
    }


    /**
     * delTable
     */
    public void delTable() {
        SQLiteDatabase localSQLiteDatabase = this.dbHelper
                .getWritableDatabase();
        localSQLiteDatabase.execSQL("delete from  user");
        localSQLiteDatabase.close();
    }

}
