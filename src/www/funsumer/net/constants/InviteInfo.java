package www.funsumer.net.constants;

public class InviteInfo {
	private String invme_id;
	private String invme_name;
	private String invme_pic;
	private String invparty_id;
	private String invparty_name;
	private String invtime;

	

	public String getinvme_id() {
		return invme_id;
	}

	public void setinvme_id(String invme_id) {
		this.invme_id = invme_id;
	}

	public String getinvme_name() {
		return invme_name;
	}

	public void setinvme_name(String invme_name) {
		this.invme_name = invme_name;
	}
	public String getinvme_pic() {
		return invme_pic;
	}

	public void setinvme_pic(String invme_pic) {
		this.invme_pic = "http://funsumer.net/" + invme_pic;
	}
	public String getinvparty_id() {
		return invparty_id;
	}

	public void setinvparty_id(String invparty_id) {
		this.invparty_id = invparty_id;
	}
	public String getinvparty_name() {
		return invparty_name;
	}

	public void setinvparty_name(String invparty_name) {
		this.invparty_name = invparty_name;
	}
	public String getinvtime() {
		return invtime;
	}

	public void setinvtime(String invtime) {
		this.invtime = invtime;
	}
	
}
