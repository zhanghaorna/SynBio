package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "database.db";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context) {
		// CursorFactory设置为null,使用默认值
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// 数据库第一次被创建时onCreate会被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS vertex"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,mindex INTEGER, uid VARCHAR, cid INTEGER, pid INTEGER, x REAL, y REAL, z REAL, name VARCHAR);");
		db.execSQL("create table if not exists edge"
				+"(_id integer primary key autoincrement, mindex integer ,rname varchar, sindex integer, eindex integer, start integer , end integer , pid integer ,rid integer,ecnumber VARCHAR);");
		db.execSQL("create table if not exists pathway"
				+"(_id integer primary key autoincrement, uid varchar , pid integer , x real ,y real ,z real , name varchar);");
	}

	// 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("ALTER TABLE Vertex ADD COLUMN other STRING");
	}
}
