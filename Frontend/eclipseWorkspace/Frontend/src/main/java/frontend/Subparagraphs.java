package frontend;

public class Subparagraphs {
	private String Number;
	private RequiredBy[] Required;

	public RequiredBy[] getRequired() {
		return Required;
	}

	public void setRequired(RequiredBy[] required) {
		Required = required;
	}

	public String getNumber() {
		return Number;
	}

	public void setNumber(String Number) {
		this.Number = Number;
	}
	
	public String toString() {
		return Number;
	}
}
