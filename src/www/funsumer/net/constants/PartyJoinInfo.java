package www.funsumer.net.constants;

public class PartyJoinInfo {
	private String mid;
	private String mname;
	private String mpic;

	public String getmid() {
		return mid;
	}

	public void setmid(String mid) {
		this.mid = mid;
	}

	public String getmname() {
		return mname;
	}

	public void setmname(String mname) {
		this.mname = mname;
	}

	public String getmpic() {
		return mpic;
	}

	public void setmpic(String mpic) {
		this.mpic = "http://funsumer.net/" + mpic;
	}

}
