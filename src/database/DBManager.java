package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.metnet.Compound;
import com.example.metnet.Object3D_Line;
import com.example.metnet.Object3D_Vertex;
import com.example.metnet.PathWay;
import com.example.metnet.Reaction;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.zhr.synbio.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class DBManager {
	private DBHelper helper;
	private SQLiteDatabase db;
	private Context mContext;
	private String PACKAGE_NAME = "com.zhr.synbio";
	private String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME;
	private String DB_NAME = "database.db";

	public DBManager(Context context) {
		this.mContext = context;
	
		this.db = this.openDatabase(DB_PATH + "/" + DB_NAME);
		// helper = new DBHelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		// db = helper.getWritableDatabase();
	}

	private SQLiteDatabase openDatabase(String FileName) {
		try {
			
			if (!(new File(FileName).exists())) {
				
				InputStream is = this.mContext.getResources().openRawResource(
						R.raw.database);
				
				FileOutputStream fos = new FileOutputStream(FileName);
				byte[] buffer = new byte[4096];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			
			
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(FileName,
					null);
			return db;
		} catch (FileNotFoundException e) {
			Log.e("Database", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Database", "IO exception");
			e.printStackTrace();
		}
		return null;
	}

	public void addvertex(List<Vertex> vertexs) {
		db.beginTransaction(); // 开始事务
		try {
			for (Vertex vertex : vertexs) {
				db.execSQL(
						"INSERT INTO Vertex VALUES(null, ?, ?, ?,?,?,?,?,?)",
						new Object[] { vertex.index, vertex.uid, vertex.cid,
								vertex.pid, vertex.x, vertex.y, vertex.z,
								vertex.name });
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void addpathway(List<Pathway> pathways) {
		db.beginTransaction(); // 开始事务
		try {
			for (Pathway pathway : pathways) {
				db.execSQL(
						"INSERT INTO pathway VALUES(null, ?, ?, ?, ?, ? ,?)",
						new Object[] { pathway.uid, pathway.pid, pathway.x,
								pathway.y, pathway.z, pathway.name });
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void addedge(List<Edge> edges) {
		db.beginTransaction(); // 开始事务
		try {
			for (Edge edge : edges) {
				db.execSQL(
						"INSERT INTO edge VALUES(null, ?, ?, ?, ?, ? ,? ,? ,?,?)",
						new Object[] { edge.index, edge.rname, edge.sindex,
								edge.eindex, edge.start, edge.end, edge.pid,
								edge.rid, edge.ecnumber });
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public List<Compound> queryforvertex(int pid) {
		List<Compound> vertexs = new ArrayList<Compound>();
		Cursor c = db.rawQuery("SELECT * FROM Vertex where pid=" + pid, null);
		Object3D vertex = Object3D_Vertex.getVertex(5);
		int i = 0;
		while (c.moveToNext()) {
			Compound mCompound = new Compound(vertex, true);
			double x = c.getDouble(c.getColumnIndex("x")) / 5;
			double y = c.getDouble(c.getColumnIndex("y")) / 5;
			double z = c.getDouble(c.getColumnIndex("z")) / 5;
			mCompound.setPosition(new SimpleVector(x, y, z));
			mCompound.setPID(c.getInt(c.getColumnIndex("pid")));
			mCompound.setName(c.getString(c.getColumnIndex("name")));
			mCompound.setUID(c.getString(c.getColumnIndex("uid")));
			mCompound.setColor(Util.getRandomColor());
			mCompound.setIndex(c.getInt(c.getColumnIndex("mindex")));
			mCompound.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
			mCompound.forceGeometryIndices(true);
			if (i != 0)
				mCompound.shareCompiledData(vertexs.get(0));
			i++;
			vertexs.add(mCompound);
		}
		c.close();
		return vertexs;
	}
	
	public List<String> queryforpathwayID()
	{
		List<String> pathwayID = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select uid from pathway", null);
		while (cursor.moveToNext()) {
			
			String uid =  cursor.getString(cursor.getColumnIndexOrThrow("uid"));
			pathwayID.add(uid);
			
		}
		cursor.close();
		return pathwayID;
	}
	
	public Pathway QueryforPathwayByName(String name)
	{
		Pathway mPathway = new Pathway();
		Log.d("DB", "before find");
		Cursor c = db.rawQuery("select * from pathway where name=?", new String[]{name});
		Log.d("DB", "after find");
		
		while (c.moveToNext())
		{
			mPathway.pid = c.getInt(c.getColumnIndex("pid"));
			mPathway.x = c.getDouble(c.getColumnIndex("x")) / 10;
			mPathway.y = c.getDouble(c.getColumnIndex("y")) / 10;
			mPathway.z = c.getDouble(c.getColumnIndex("z")) / 10;
			mPathway.name = c.getString(c.getColumnIndex("name"));
			mPathway.uid = c.getString(c.getColumnIndex("uid"));
			
		}
		c.close();
		return mPathway;
	}
	
	public Pathway QueryforPathwayByUid(String uid)
	{
		Pathway mPathway = new Pathway();
		Log.d("DB", uid);
		Cursor c = db.rawQuery("select * from pathway where uid=?", new String[]{uid});

		while (c.moveToNext())
		{
			mPathway.pid = c.getInt(c.getColumnIndex("pid"));
			mPathway.x = c.getDouble(c.getColumnIndex("x")) / 10;
			mPathway.y = c.getDouble(c.getColumnIndex("y")) / 10;
			mPathway.z = c.getDouble(c.getColumnIndex("z")) / 10;
			mPathway.name = c.getString(c.getColumnIndex("name"));
			mPathway.uid = c.getString(c.getColumnIndex("uid"));
			
		}
		c.close();
		return mPathway;
	}
	
	

	public List<PathWay> queryforpathway() {
		List<PathWay> pathways = new ArrayList<PathWay>();
		Cursor c = db.rawQuery("select * from pathway", null);
		Object3D vertex = Object3D_Vertex.getVertex(27);
		int i = 0;
		while (c.moveToNext()) {
			PathWay mPathWay = new PathWay(vertex, true);
			double x = c.getDouble(c.getColumnIndex("x")) / 10;
			double y = c.getDouble(c.getColumnIndex("y")) / 10;
			double z = c.getDouble(c.getColumnIndex("z")) / 10;
			mPathWay.setPid(c.getInt(c.getColumnIndex("pid")));
			mPathWay.setName(c.getString(c.getColumnIndex("name")));
			mPathWay.setPos(new SimpleVector(x, y, z));
			mPathWay.setColor(Util.getRandomColor());
			mPathWay.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
			mPathWay.forceGeometryIndices(true);
			if (i != 0)
				mPathWay.shareCompiledData(pathways.get(0));
			pathways.add(mPathWay);
			i++;
			System.out.println(" posx:" + x + " posy: " + y + " posz: " + z);
			System.out.println("num:   " + i);
		}
		c.close();
		return pathways;
	}

	public List<Reaction> queryforedge(int pid, List<Compound> vertexs) {
		List<Reaction> reactions = new ArrayList<Reaction>();
		Cursor c = db.rawQuery("select * from edge where pid=" + pid, null);
		while (c.moveToNext()) {
			int sindex = c.getInt(c.getColumnIndex("sindex"));
			int eindex = c.getInt(c.getColumnIndex("eindex"));
			SimpleVector svertex = null;
			SimpleVector evertex = null;
			for (Compound mcompound : vertexs) {
				if (mcompound.getIndex() == sindex) {
					svertex = mcompound.getPosition();
				}
				if (mcompound.getIndex() == eindex) {
					evertex = mcompound.getPosition();
				}
			}
			Object3D mLine = Object3D_Line.getLine(svertex, evertex, 1.0f);
			Reaction reaction = new Reaction(mLine);
			reaction.setColor(Util.getRandomColor());
			reaction.setIndex(c.getInt(c.getColumnIndex("mindex")));
			reaction.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
			reaction.ecnumber = c.getString(c.getColumnIndex("ecnumber"));
			reactions.add(reaction);
			// System.out.println("number of line: "+reactions.size());
		}
		c.close();
		return reactions;
	}

	public List<String> QueryForPathwayName() {
		List<String> mList = new ArrayList<String>();
		Cursor c = db.rawQuery("select name from pathway", null);
		while (c.moveToNext()) {
			String iname = c.getString(c.getColumnIndex("name"));
			mList.add(iname);
		}
		c.close();
		return mList;
	}

	public Pathway QueryForPathwayByIndex(int index) {
		Pathway mPathway = new Pathway();
		Cursor c = db
				.rawQuery("select * from pathway where _id=" + index, null);
		while (c.moveToNext()) {
			// System.out.println("the _id is :" +index);
			mPathway.pid = c.getInt(c.getColumnIndex("pid"));
			mPathway.x = c.getDouble(c.getColumnIndex("x")) / 10;
			mPathway.y = c.getDouble(c.getColumnIndex("y")) / 10;
			mPathway.z = c.getDouble(c.getColumnIndex("z")) / 10;
			mPathway.name = c.getString(c.getColumnIndex("name"));

		}
		c.close();
		return mPathway;
	}
	
	
	
	public List<String> QueryForCompoundId()
	{
		List<String> CompoundId = new ArrayList<String>();
		Cursor c = db.rawQuery("select uid from vertex", null);
		while(c.moveToNext())
		{
			String uid = c.getString(c.getColumnIndex("uid"));
			CompoundId.add(uid);
		}
		
		
		return CompoundId;
	}
	
	public List<String> QueryForPathwayNameByCompoundID(String uid)
	{
		List<String> pathwayName = new ArrayList<String>();
		Cursor c = db.rawQuery("select pid from vertex where uid=?", new String[]{uid});
		while(c.moveToNext())
		{
			String pid = c.getString(c.getColumnIndex("pid"));
			Log.d("DB", pid);
			Cursor c2 = db.rawQuery("select name from pathway where pid=?",new String[]{pid});
			while(c2.moveToNext())
			{
				String pathway = c2.getString(c2.getColumnIndex("name"));
				pathwayName.add(pathway);
			}
			c2.close();
			
			
		}
		c.close();
		return pathwayName;

	}
	
	public List<String> QueryForPathwayNameByCompoundName(String name)
	{
		List<String> pathwayName = new ArrayList<String>();
		Cursor c = db.rawQuery("select pid from vertex where name=?", new String[]{name});
		while(c.moveToNext())
		{
			String pid = c.getString(c.getColumnIndex("pid"));
			Log.d("DB", pid);
			Cursor c2 = db.rawQuery("select name from pathway where pid=?",new String[]{pid});
			while(c2.moveToNext())
			{
				String pathway = c2.getString(c2.getColumnIndex("name"));
				pathwayName.add(pathway);
			}
			c2.close();
			
			
		}
		c.close();
		return pathwayName;

	}
	
	
	

	public List<String> QueryForCompoundName() {
		List<String> CompoundName = new ArrayList<String>();
		Cursor c = db.rawQuery("select distinct name from vertex", null);
		while (c.moveToNext()) {
			String name = c.getString(c.getColumnIndex("name"));
			CompoundName.add(name);
		}
		return CompoundName;
	}

	public List<String> QueryForCompoundName_respectly(String rename)
	{
		List<String> result = new ArrayList<String>();
		Cursor c = db
				.rawQuery(
						"select distinct name from vertex where pid in(select pid from vertex where name=\""
								+ rename + "\")", null);
		while(c.moveToNext())
		{
			String name=c.getString(c.getColumnIndex("name"));
			result.add(name);
		}
		return result;
	}
	
	

	public List<Vertex> QueryForCompoundbyName(String name) {
		List<Vertex> vertexs = new ArrayList<Vertex>();
		Cursor c = db.rawQuery("select * from vertex where name=\"" + name
				+ "\"", null);
		while (c.moveToNext()) {
			Vertex v = new Vertex();
			v.index = c.getInt(c.getColumnIndex("mindex"));
			v.cid = c.getInt(c.getColumnIndex("cid"));
			v.name = c.getString(c.getColumnIndex("name"));
			v.pid = c.getInt(c.getColumnIndex("pid"));
			vertexs.add(v);
		}
		return vertexs;
	}

	public List<Edge> QueryForEdgesbypid(int pid) {
		List<Edge> edges = new ArrayList<Edge>();
		Cursor c = db.rawQuery("select * from edge where pid=" + pid, null);
		while (c.moveToNext()) {
			Edge e = new Edge();
			e.index = c.getInt(c.getColumnIndex("mindex"));
			e.sindex = c.getInt(c.getColumnIndex("sindex"));
			e.eindex = c.getInt(c.getColumnIndex("eindex"));
			edges.add(e);
		}
		return edges;
	}

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}
}
