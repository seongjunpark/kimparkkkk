package www.funsumer.net.constants;

public class MemberInfo {
	private String Party_Mem_Pic;
	private String Party_Mem_Name;
	private String Party_Mem_Party;
	private String Party_Mem_ID;
	private String Fstat;

	// PICTURE
	public String getParty_Mem_Pic() {
		return Party_Mem_Pic;
	}

	public void setParty_Mem_Pic(String Party_Mem_Pic) {
		this.Party_Mem_Pic = "http://funsumer.net/" + Party_Mem_Pic + "50";
	}

	// TEXT
	public String getParty_Mem_Name() {
		return Party_Mem_Name;
	}

	public void setParty_Mem_Name(String Party_Mem_Name) {
		this.Party_Mem_Name = Party_Mem_Name;
	}
	
	public String getParty_Mem_Party() {
		return Party_Mem_Party;
	}

	public void setParty_Mem_Party(String Party_Mem_Party) {
		this.Party_Mem_Party = Party_Mem_Party;
	}
	
	// ID
	public String getParty_Mem_ID() {
		return Party_Mem_ID;
	}

	public void setParty_Mem_ID(String Party_Mem_ID) {
		this.Party_Mem_ID = Party_Mem_ID;
	}
	
	public String getFstat() {
		return Fstat;
	}

	public void setFstat(String Fstat) {
		this.Fstat = Fstat;
	}
	
}
