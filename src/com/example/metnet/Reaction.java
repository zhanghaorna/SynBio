package com.example.metnet;

import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;

public class Reaction extends Object3D{
	public Reaction(Object3D obj) {
		super(obj);
		// TODO Auto-generated constructor stub
	}
	private int Index;
	private String Name;
	private int Rid;
	private int Pid;
	private int Start;
	private int End;
	private int Sindex;
	private int Eindex;
	private int Weight;
	private RGBColor Color;
	public String ecnumber;
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getRid() {
		return Rid;
	}
	public void setRid(int rid) {
		Rid = rid;
	}
	public int getPid() {
		return Pid;
	}
	public void setPid(int pid) {
		Pid = pid;
	}
	public int getStart() {
		return Start;
	}
	public void setStart(int start) {
		Start = start;
	}
	public int getEnd() {
		return End;
	}
	public void setEnd(int end) {
		End = end;
	}
	public int getSindex() {
		return Sindex;
	}
	public void setSindex(int sindex) {
		Sindex = sindex;
	}
	public int getEindex() {
		return Eindex;
	}
	public void setEindex(int eindex) {
		Eindex = eindex;
	}
	public int getWeight() {
		return Weight;
	}
	public void setWeight(int weight) {
		Weight = weight;
	}
	public RGBColor getColor() {
		return Color;
	}
	public void setColor(RGBColor color) {
		Color = color;
		super.setAdditionalColor(color);
	}
	@Override
	public String toString() {
		return "Reaction [Name=" + Name + ", Rid=" + Rid + ", Pid=" + Pid
				+ ", Start=" + Start + ", End=" + End + ", Sindex=" + Sindex
				+ ", Eindex=" + Eindex + ", Weight=" + Weight + ", Color="
				+ Color + "]";
	}
	public int getIndex() {
		return Index;
	}
	public void setIndex(int index) {
		Index = index;
	}

}
