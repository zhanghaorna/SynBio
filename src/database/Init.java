package database;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.zhr.synbio.R;

import android.content.Context;



public class Init extends Thread {
	private Context mContext;
	private List<Pathway> pathways=null;
	private List<Edge>edges=null;
	private List<Vertex>vertexs=null;
	private DBManager mDbManager=null;

	public Init(Context _Context)
	{
		this.mContext=_Context;
	}
	
	public void run() {
		mDbManager=new DBManager(mContext);
		InputStream inputStream = mContext.getResources().openRawResource(
				R.raw.data);
		byte[] data=null;
		try {
			data = Util.StreamRead(inputStream);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		pathways = new ArrayList<Pathway>();
		try {
			pathways = Util.LoadPathway(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mDbManager.addpathway(pathways);
		pathways=null;
		
		edges=new ArrayList<Edge>();
		try {
			edges=Util.LoadEdge(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mDbManager.addedge(edges);
		edges=null;
		vertexs=new ArrayList<Vertex>();
		try {
			vertexs=Util.LoadVertex(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mDbManager.addvertex(vertexs);
		vertexs=null;
		mDbManager.closeDB();
		try {
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data=null;
		System.out.println("data providing finished");
		//this.destroy();
	}
}
