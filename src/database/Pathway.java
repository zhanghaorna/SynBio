package database;

public class Pathway {
	public Pathway() {
		super();
	}
	public Pathway(String uid, int pid, double x, double y, double z,
			String name) {
		super();
		this.uid = uid;
		this.pid = pid;
		this.x = x;
		this.y = y;
		this.z = z;
		this.name = name;
	}
	public String uid;
	public int pid;
	public double x;
	public double y;
	public double z;
	public String name;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
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
		return "Pathway [uid=" + uid + ", pid=" + pid + ", x=" + x + ", y=" + y
				+ ", z=" + z + ", name=" + name + "]";
	}
	

}
