package www.funsumer.net.constants;

public class FriendInfo {
	private String Fname;
	private String Fpic;
	private String Fid;
	private String Fparty;
	private String Fvote;

	// PICTURE
	public String getFpic() {
		return Fpic;
	}

	public void setFpic(String Fpic) {
		this.Fpic = "http://funsumer.net/" + Fpic + "50";
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
	
	public String getFvote() {
		return Fvote;
	}

	public void setFvote(String Fvote) {
		this.Fvote = Fvote;
	}
	
}
