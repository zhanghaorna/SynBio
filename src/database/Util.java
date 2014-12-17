package database;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import org.json.JSONArray;
import org.json.JSONObject;

import com.threed.jpct.RGBColor;



import android.app.Activity;
import android.graphics.Canvas.VertexMode;


public final class Util {

	
	public static List<Vertex> LoadVertex(byte[] data) throws Exception
	{
		List<Vertex> vertexs=new ArrayList<Vertex>();
    	String json = new String(data);
		JSONObject jsonobj=new JSONObject(json);		
        JSONArray mJsonArray=jsonobj.getJSONArray("vs") ;
        System.out.println("length of the jsonarray:vs is :"+ mJsonArray.length());
	    for(int i=0;i<mJsonArray.length();i++)
	    {
	    	JSONObject mJsonObject=mJsonArray.getJSONObject(i);
	    	Vertex v=new Vertex();
	    	v.index=mJsonObject.getInt("index");
	    	v.cid=mJsonObject.getInt("cid");
	    	v.uid=mJsonObject.getString("uid");
	    	v.pid=mJsonObject.getInt("pid");
	    	v.x=mJsonObject.getDouble("x");
	    	v.y=mJsonObject.getDouble("y");
	    	v.z=mJsonObject.getDouble("z");
	    	v.name=mJsonObject.getString("name");
	    	vertexs.add(v);
	    }
	    mJsonArray=null;
		return vertexs;
	}
	public static List<Pathway> LoadPathway(byte[] data)throws Exception
	{
		List<Pathway> pathways=new ArrayList<Pathway>();
    	String json = new String(data);
		JSONObject jsonobj=new JSONObject(json);		
        JSONArray mJsonArray=jsonobj.getJSONArray("ps") ;
        System.out.println("length of the jsonarray:ps is :"+ mJsonArray.length());
	    for(int i=0;i<mJsonArray.length();i++)
	    {
	    	JSONObject mJsonObject=mJsonArray.getJSONObject(i);
	    	Pathway p=new Pathway();
	    	p.uid=mJsonObject.getString("uid");
	    	p.pid=mJsonObject.getInt("pid");
	    	p.x=mJsonObject.getDouble("x");
	    	p.y=mJsonObject.getDouble("y");
	    	p.z=mJsonObject.getDouble("z");
	    	p.name=mJsonObject.getString("name");
	    	pathways.add(p);
	    }
	    mJsonArray=null;
		return pathways;
	}
	
	public static List<Edge> LoadEdge(byte[] data)throws Exception
	{
		List<Edge> edges=new ArrayList<Edge>();
    	String json = new String(data);
		JSONObject jsonobj=new JSONObject(json);		
        JSONArray mJsonArray=jsonobj.getJSONArray("es") ;
        System.out.println("length of the jsonarray:es is :"+ mJsonArray.length());
	    for(int i=0;i<mJsonArray.length();i++)
	    {
	    	JSONObject mJsonObject=mJsonArray.getJSONObject(i);
	    	Edge e=new Edge();
	    	e.index=mJsonObject.getInt("index");
	    	e.sindex=mJsonObject.getInt("sindex");
	    	e.eindex=mJsonObject.getInt("eindex");
	    	e.start=mJsonObject.getInt("start");
	    	e.end=mJsonObject.getInt("end");
	    	e.pid=mJsonObject.getInt("pid");
	    	e.rid=mJsonObject.getInt("rid");
	    	e.rname=mJsonObject.getString("rname");
	    	e.ecnumber=mJsonObject.getString("ecnumber");
	    	edges.add(e);
	    }
	    mJsonArray=null;
		return edges;
	}
	

	public static byte[] StreamRead(InputStream inStream) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outputStream.write(buffer);  
		}
		return outputStream.toByteArray();
	}
	
	public static RGBColor getRandomColor()
	{
		Random rnd = new Random();
		int r=rnd.nextInt(255);
		int g=rnd.nextInt(255);
		int b=rnd.nextInt(255);
		int a=rnd.nextInt(255);
		RGBColor color=new RGBColor(r, g, b, a);
		return color;
	}
}
