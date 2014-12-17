package database;

import java.io.Serializable;

public class Edge implements Serializable {
	public Edge() {
		super();
	}
	public Edge(int index, String rname, int sindex, int eindex, int start,
			int end, int pid, int rid) {
		super();
		this.index = index;
		this.rname = rname;
		this.sindex = sindex;
		this.eindex = eindex;
		this.start = start;
		this.end = end;
		this.pid = pid;
		this.rid = rid;
	}
	public int index;
	public String rname;
	public int sindex;
	public int eindex;
	public int start;
	public int end;
	public int pid;
	public int rid;
	public String ecnumber;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getRname() {
		return rname;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public int getSindex() {
		return sindex;
	}
	public void setSindex(int sindex) {
		this.sindex = sindex;
	}
	public int getEindex() {
		return eindex;
	}
	public void setEindex(int eindex) {
		this.eindex = eindex;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	@Override
	public String toString() {
		return "Edge [index=" + index + ", rname=" + rname + ", sindex="
				+ sindex + ", eindex=" + eindex + ", start=" + start + ", end="
				+ end + ", pid=" + pid + ", rid=" + rid + "]";
	}

}
