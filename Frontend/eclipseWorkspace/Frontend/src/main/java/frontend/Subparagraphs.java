package frontend;

public class Subparagraphs {
	private String Number;
	private RequiredBy[] requiredby;

	public RequiredBy[] getrequiredby() {
		return requiredby;
	}

	public void setrequiredby(RequiredBy[] requiredby) {
		this.requiredby = requiredby;
	}

	public String getNumber() {
		return Number;
	}

	public void setNumber(String Number) {
		this.Number = Number;
	}
	
	public String toString() {
		if(Character.isDigit(getNumber().charAt(0)))
			return "§§ " + getNumber();
		else
			return getNumber();
	}
}
