package database;

public class Vertex {
	
	public Vertex() {
		super();
	}
	public Vertex(int index, String uid, int cid, int pid, double x, double y,
			double z, String name) {
		super();
		this.index = index;
		this.uid = uid;
		this.cid = cid;
		this.pid = pid;
		this.x = x;
		this.y = y;
		this.z = z;
		this.name = name;
	}
	public int index;
	public String uid;
	public int cid;
	public int pid;
	public double x;
	public double y;
	public double z;
	public String name;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Vertex [index=" + index + ", uid=" + uid + ", cid=" + cid
				+ ", pid=" + pid + ", x=" + x + ", y=" + y + ", z=" + z
				+ ", name=" + name + "]";
	}
}
