package com.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class UsersDAO extends DAOBase {
	  public static final String USER_KEY = "id";
	  public static final String USER_LOGIN = "login";
	  public static final String USER_PASSWORD = "password";
	  public static final String USERS_TABLE_NAME = "users";
	  public static final String USERS_TABLE_CREATE =
			    "CREATE TABLE " + USERS_TABLE_NAME + " (" +
			      USER_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			      USER_LOGIN + " TEXT, " +
			      USER_PASSWORD + " TEXT);";
	  public static final String USERS_TABLE_DROP = "DROP TABLE IF EXISTS " + USERS_TABLE_NAME + ";";
	public UsersDAO(Context pContext) {
		super(pContext);
		// TODO Auto-generated constructor stub
	}
   
	/** 
	 * Add a new user into the database 
	 * @param user
	 */
 public void add(User user){
	ContentValues value = new ContentValues();
	value.put(UsersDAO.USER_LOGIN, user.getLogin());
	value.put(UsersDAO.USER_PASSWORD, user.getPassword());
	mDb.insert(UsersDAO.USERS_TABLE_NAME, null, value);
    mDb.insert(USERS_TABLE_NAME, null,value);
 }
 
 /**delete a user giving his Id
  * 
  * @param id
  */
 public void delete(String login) {
	  mDb.delete(USERS_TABLE_NAME, USER_LOGIN + " = ?", new String[] {String.valueOf(login)});
	}
 
 public void modifyPassword(User user){
	 ContentValues value = new ContentValues();
	 value.put(USER_PASSWORD, user.getPassword());
	 mDb.update(USERS_TABLE_NAME, value, USER_KEY  + " = ?", new String[] {String.valueOf(user.getId())});
 }
 /**
  * Check if the login an password exist in the database 
  * @param user
  * @return
  */
 public boolean checkIfExist(User user){
	 Cursor c = mDb.rawQuery("select " + USER_LOGIN + " from " + USERS_TABLE_NAME + " where login =  ?"+ "and password = ?" ,  new String[]{user.getLogin(),user.getPassword()});
	 if( c.getCount() == 0 ){ return false;}
	 else { return true ;}
 }
 /**
  * 
  */
 public boolean checkLoginTaken(String login){
	 Cursor c = mDb.rawQuery("select " + USER_LOGIN + " from " + USERS_TABLE_NAME + " where login =  ?" ,  new String[]{login});
	 
	 if( c.getCount() == 0 ){ // the login is not taken
		 return false;}
	 else { return true ;}
 }
}
