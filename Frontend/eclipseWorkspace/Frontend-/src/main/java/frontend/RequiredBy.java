package frontend;

public class RequiredBy {
	private String Shorthand;
	private String Paragraph;
	private String Subparagraph;
	
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
		if(getParagraph() != null) {

			if(Character.isDigit(getSubparagraph().charAt(0))) {
				if(getSubparagraph() != null) {
					return "(" + getShorthand() + ") § " + getParagraph() + " §§ " + getSubparagraph(); 
				} else {
					return "(" + getShorthand() + ") § " + getParagraph();
				}
			} else {
				return "(" + getShorthand() + ") " + getParagraph();
			}
		} else {
			return "Keine Einträge vorhanden!";
		}
	}
}
