package www.funsumer.net.constants;

public class SearchInfoFR {
	private String name;
	private String id;
	private String pic;
	private String pcount;
	private String fstat;

	

	public String getname() {
		return name;
	}

	public void setname(String name) {
		this.name = name;
	}

	public String getid() {
		return id;
	}

	public void setid(String id) {
		this.id = id;
	}
	public String getpic() {
		return pic;
	}

	public void setpic(String pic) {
		this.pic = "http://funsumer.net/" + pic + "38";
	}
	public String getpcount() {
		return pcount;
	}

	public void setpcount(String pcount) {
		this.pcount = pcount;
	}
	public String getfstat() {
		return fstat;
	}

	public void setfstat(String fstat) {
		this.fstat = fstat;
	}
	
}
