package www.funsumer.net.constants;

public class ContentsInfo {
	private String pid;
	private String pname;
	private String ppic;
	
	private String mypid;
	private String mypname;
	private String myppic;

	// FIRST
	public String getpid() {
		return pid;
	}

	public void setpid(String pid) {
		this.pid = pid;
	}

	public String getpname() {
		return pname;
	}

	public void setpname(String pname) {
		this.pname = pname;
	}

	public String getppic() {
		return ppic;
	}

	public void setppic(String ppic) {
		this.ppic = "http://funsumer.net/" + ppic;
	}
	
	// MY
	public String getmypid() {
		return mypid;
	}

	public void setmypid(String mypid) {
		this.mypid = mypid;
	}

	public String getmypname() {
		return mypname;
	}

	public void setmypname(String mypname) {
		this.mypname = mypname;
	}

	public String getmyppic() {
		return myppic;
	}

	public void setmyppic(String myppic) {
		this.myppic = "http://funsumer.net/" + myppic;
	}

}
