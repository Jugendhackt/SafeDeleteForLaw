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
	
	public boolean isHasSubparagraphs() {
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
		return Number; 
	}
}
