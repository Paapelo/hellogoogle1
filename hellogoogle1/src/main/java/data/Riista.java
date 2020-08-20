package data;

//import javax.persistence.Entity;
//
//import javax.persistence.Id;

//@Entity

public class Riista {
//	@Id
	private int id;
	private String laji;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLaji() {
		return laji;
	}
	public void setLaji(String laji) {
		this.laji = laji;
	}
	public String toString() {
		return id+" "+laji+"\n";
	}
}
