package www.funsumer.net.constants;

public class SearchInfoPT {
	private String pname;
	private String pid;
	private String ppic;
	private String padmin;
	private String pcount;

	

	public String getpname() {
		return pname;
	}

	public void setpname(String pname) {
		this.pname = pname;
	}

	public String getpid() {
		return pid;
	}

	public void setpid(String pid) {
		this.pid = pid;
	}
	public String getppic() {
		return ppic;
	}

	public void setppic(String ppic) {
		this.ppic = "http://funsumer.net/" + ppic + "_s";
	}
	public String getpadmin() {
		return padmin;
	}

	public void setpadmin(String padmin) {
		this.padmin = padmin;
	}
	public String getpcount() {
		return pcount;
	}

	public void setpcount(String pcount) {
		this.pcount = pcount;
	}
	
}
