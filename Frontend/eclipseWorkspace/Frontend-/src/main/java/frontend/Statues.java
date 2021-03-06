package frontend;

public class Statues {
	private String Shorthand;
	private String Fullname;
	private Paragraphs[] Paragraphs;
	
	public String getShorthand() {
		return Shorthand;
	}
	public void setShorthand(String Shorthand) {
		this.Shorthand = Shorthand;
	}
	public String getFullname() {
		return Fullname;
	}
	public void setFullname(String Fullname) {
		this.Fullname = Fullname;
	}
	public Paragraphs[] getParagraphs() {
		return Paragraphs;
	}
	public void setParagraphs(Paragraphs[] Paragraphs) {
		this.Paragraphs = Paragraphs;
	}
	
	public String toString() {
		if(getFullname() != null)
			return getFullname() + " (" + getShorthand() + ")";
		else
			return "Keine Eintr�ge vorhanden!";
	}
}
