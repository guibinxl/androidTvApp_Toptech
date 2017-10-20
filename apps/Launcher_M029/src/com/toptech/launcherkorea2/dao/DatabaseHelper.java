package com.toptech.launcherkorea2.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * sqlite数据库基础类
 * @author calvin
 * 2013-01-09
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	
	//数据库名称
	private static final String DBNAME = "db_app";
	//数据库版本号
	private static final int VERSION = 1;
	
	//在SQLiteOepnHelper的子类当中，必须有该构�?函数
	public DatabaseHelper(Context context, String name, CursorFactory factory,int version) {
		//必须通过super调用父类当中的构造函�?
		super(context, name, factory, version);
	}
	
	public DatabaseHelper(Context context){
		this(context,DBNAME,VERSION);
	}
	
	public DatabaseHelper(Context context,String name,int version){
		this(context, name,null,version);
	}

	//该函数是在第一次创建数据库的时候执行,实际上是在第一次得到SQLiteDatabse对象的时候，才会调用这个方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table t_app(aid integer primary key autoincrement,pkg text);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS t_app");
		onCreate(db);
	}

}