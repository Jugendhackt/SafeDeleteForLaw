package frontend;

public class Subparagraphs {
	private String Number;
	private RequiredBy[] requiredby;

	public RequiredBy[] getRequiredby() {
		return requiredby;
	}

	public void setRequiredby(RequiredBy[] requiredby) {
		this.requiredby = requiredby;
	}

	public String getNumber() {
		return Number;
	}

	public void setNumber(String Number) {
		this.Number = Number;
	}
	
	public String toString() {
		if(getNumber() != null) {
			return "§§ " + getNumber();
		} else {
			return "Keine Einträge vorhanden!";
		}
	}
}
