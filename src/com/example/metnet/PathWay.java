package com.example.metnet;

import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;

public class PathWay extends Object3D{
	public PathWay(Object3D obj,boolean reuseMesh ) {
		super(obj,reuseMesh );
		// TODO Auto-generated constructor stub
	}
	private int Pid;
	private RGBColor Color;
	private String Name;
	private SimpleVector position;
	public int getPid() {
		return Pid;
	}
	public SimpleVector getPosition()
	{
		return position;
	}
	public void setPos(SimpleVector pos) {
		position=pos;
		super.setOrigin(pos);
	}
	public RGBColor getColor() {
		return Color;
	}
	public void setColor(RGBColor color) {
		Color = color;
		super.setAdditionalColor(color);
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public void setPid(int pid) {
		Pid = pid;
	}

	
	

}
