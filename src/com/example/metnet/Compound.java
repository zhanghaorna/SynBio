package com.example.metnet;

import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;

public class Compound extends Object3D {
	private String UID;
	private int CID;
	private int PID;
	private int Index;
	private String Name;
	private RGBColor Color;
	private SimpleVector Position;

	public Compound(Object3D $Object3d,boolean reuseMesh) {
		super($Object3d,reuseMesh);
		// TODO Auto-generated constructor stub
	}

	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	public int getCID() {
		return CID;
	}

	public void setCID(int cID) {
		CID = cID;
	}

	public int getPID() {
		return PID;
	}

	public void setPID(int pID) {
		PID = pID;
	}

	public int getIndex() {
		return Index;
	}

	public void setIndex(int index) {
		this.Index = index;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public RGBColor getColor() {
		return Color;
	}

	public void setColor(RGBColor color) {
		this.Color = color;
		super.setAdditionalColor(color);
	}


	public void setPosition(SimpleVector position) {
		this.Position=position;
		super.setOrigin(position);
	}
	public SimpleVector getPosition()
	{
		return Position;
	}

	@Override
	public String toString() {
		return "Compound [UID=" + UID + ", CID=" + CID + ", PID=" + PID
				+ ", Index=" + Index + ", Name=" + Name + ", Position="
				 + ", Color=" + Color + "]";
	}

}
