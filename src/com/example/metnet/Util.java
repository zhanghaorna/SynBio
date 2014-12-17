package com.example.metnet;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import org.json.JSONArray;
import org.json.JSONObject;

import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;

import android.app.Activity;
import android.graphics.Canvas.VertexMode;


public final class Util {

	public static List<Compound> LoadCompound(InputStream inputStream,int pid) throws Exception
	{
		List<Compound> List_Compound=new ArrayList<Compound>();
        byte[] data=null;
    	data = StreamRead(inputStream);
    	String json = new String(data);
		JSONObject jsonobj=new JSONObject(json);		
        JSONArray mJsonArray=jsonobj.getJSONArray("vs") ;
    	Object3D vertex=Object3D_Vertex.getVertex(27);
        for(int i=0;i<mJsonArray.length();i++)
        {
        	JSONObject mJsonObject=mJsonArray.getJSONObject(i);
        	int Pid=mJsonObject.getInt("pid");
			if (Pid == pid) {
				Compound mCompound = new Compound(vertex, true);
				double posx = mJsonObject.getDouble("x") / 10;
				double posy = mJsonObject.getDouble("y") / 10;
				double posz = mJsonObject.getDouble("z") / 10;
				SimpleVector position = new SimpleVector(posx, posy, posz);
				mCompound.setOrigin(position);
				mCompound.setCID(mJsonObject.getInt("cid"));
				mCompound.setColor(getRandomColor());
				List_Compound.add(mCompound); 
			}
        }
		return List_Compound;
	}

	public static List<Reaction> LoadReaction(InputStream inputStream,int pid,List<Compound> list) 
	{
		
		return null;
	}
	
	public static List<PathWay> LoadPathWay(InputStream inputStream) throws Exception
	{
		List<PathWay> List_PathWays=new ArrayList<PathWay>();
        byte[] data=null;
    	data = StreamRead(inputStream);
    	String json = new String(data);
		JSONObject jsonobj=new JSONObject(json);		
        JSONArray mJsonArray=jsonobj.getJSONArray("ps") ;
        System.out.println("length of the list_pathway:"+mJsonArray.length());
        Object3D vertex=Object3D_Vertex.getVertex(27);
	    for(int i=0;i<mJsonArray.length();i++)
	    {
	    	JSONObject mJsonObject=mJsonArray.getJSONObject(i);
	    	double posx=mJsonObject.getDouble("x")/10;
	    	double posy=mJsonObject.getDouble("y")/10;
	    	double posz=mJsonObject.getDouble("z")/10;
	    	SimpleVector position=new SimpleVector(posx,posy,posz);
	    	String name=mJsonObject.getString("name");
	    	int pid=mJsonObject.getInt("pid");
	    	PathWay mPathWay=new PathWay(vertex,true);
	    	mPathWay.setOrigin(position);
	    	mPathWay.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
	    	mPathWay.setColor(getRandomColor());
	    	mPathWay.setPid(pid);
	    	mPathWay.setName(name);
	    	mPathWay.forceGeometryIndices(true);
	    	if(i!=0)
	    		mPathWay.shareCompiledData(List_PathWays.get(0));
	    	List_PathWays.add(mPathWay);
	    	System.out.println("have load pathway:"+i);
	    }
	    data=null;
	    mJsonArray=null;
		return List_PathWays;
	}

	private static byte[] StreamRead(InputStream inStream) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outputStream.write(buffer);

		}
		inStream.close();
		return outputStream.toByteArray();
	}
	private static RGBColor getRandomColor()
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
