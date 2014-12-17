package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "database.db";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context) {
		// CursorFactory����Ϊnull,ʹ��Ĭ��ֵ
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// ���ݿ��һ�α�����ʱonCreate�ᱻ����
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS vertex"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,mindex INTEGER, uid VARCHAR, cid INTEGER, pid INTEGER, x REAL, y REAL, z REAL, name VARCHAR);");
		db.execSQL("create table if not exists edge"
				+"(_id integer primary key autoincrement, mindex integer ,rname varchar, sindex integer, eindex integer, start integer , end integer , pid integer ,rid integer,ecnumber VARCHAR);");
		db.execSQL("create table if not exists pathway"
				+"(_id integer primary key autoincrement, uid varchar , pid integer , x real ,y real ,z real , name varchar);");
	}

	// ���DATABASE_VERSIONֵ����Ϊ2,ϵͳ�����������ݿ�汾��ͬ,�������onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("ALTER TABLE Vertex ADD COLUMN other STRING");
	}
}
