package frontend;

public class Paragraphs {
	private String Number;
	private boolean HasSubparagraphs;
	private Subparagraphs[] Subparagraphs;
	
	public String getNumber() {
		return Number;
	}
	
	public void setNumber(String Number) {
		this.Number = Number;
	}
	
	public boolean hasSubparagraphs() {
		return HasSubparagraphs;
	}
	
	public void setHasSubparagraphs(boolean HasSubparagraphs) {
		this.HasSubparagraphs = HasSubparagraphs;
	}


	public Subparagraphs[] getSubparagraphs() {
		return Subparagraphs;
	}


	public void setSubparagraphs(Subparagraphs[] subparagraphs) {
		Subparagraphs = subparagraphs;
	}
	
	public String toString() {
		if(getNumber() != null) {
			
			if(Character.isDigit(getNumber().charAt(0))) {
				return "§ " + getNumber();
			} else {
				return getNumber();
			}
			
		} else {
			return "Keine Einträge vorhanden!";
		}
	}
}
