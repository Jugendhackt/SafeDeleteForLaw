package frontend;

public class Paragraphs {
	private String Number;
	private boolean HasSubparagraphs;
	private Subparagraphs[] Subparagraphs;
	private String errorMsg;
	
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
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
		if(errorMsg == null && getNumber() != null) {
			
			if(Character.isDigit(getNumber().charAt(0))) {
				return "§ " + getNumber();
			} else {
				return getNumber();
			}
			
		} else if(getNumber() == null) {
			return "Keine Einträge vorhanden!";
		} else {
			return errorMsg;
		}
	}
}
