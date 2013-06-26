package www.funsumer.net.constants;

public class EventInfo3 {
	private String Fname;
	private String Fpic;
	private String Fid;
	private String Fparty;

	// PICTURE
	public String getFpic() {
		return Fpic;
	}

	public void setFpic(String Fpic) {
		this.Fpic = "http://funsumer.net/" + Fpic;
	}
	
	// TEXT
	public String getFname() {
		return Fname;
	}

	public void setFname(String Fname) {
		this.Fname = Fname;
	}
	
	public String getFparty() {
		return Fparty;
	}

	public void setFparty(String Fparty) {
		this.Fparty = Fparty;
	}
	
	// ID
	public String getFid() {
		return Fid;
	}

	public void setFid(String Fid) {
		this.Fid = Fid;
	}
	
}
