package frontend;

public class RequiredBy {
	private String Shorthand;
	private String Paragraph;
	private String Subparagraph;
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
	public String getParagraph() {
		return Paragraph;
	}
	public void setParagraph(String Paragraph) {
		this.Paragraph = Paragraph;
	}
	public String getSubparagraph() {
		return Subparagraph;
	}
	public void setSubparagraph(String Subparagraph) {
		this.Subparagraph = Subparagraph;
	}
	
	public String toString() {
		if(errorMsg == null) {
			try {
				Integer.parseInt(getParagraph());
				
				if(getSubparagraph() != null)
					return "(" + getShorthand() + ") §" + getParagraph() + " §§" + getSubparagraph(); 
				else
					return "(" + getShorthand() + ") §" + getParagraph();
				
			} catch(Exception e) {
				return "(" + getShorthand() + ") " + getParagraph();
			}
			
		} else {
			return errorMsg;
		}
	}
}
