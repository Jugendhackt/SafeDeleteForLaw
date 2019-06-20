package frontend;

public class Statues {
	private String Shorthand;
	private String Fullname;
	private Paragraphs[] Paragraphs;
	private String errorMsg;
	
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
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
		if(errorMsg == null)
			return getFullname() + " (" + getShorthand() + ")";
		else
			return errorMsg;
	}
}
