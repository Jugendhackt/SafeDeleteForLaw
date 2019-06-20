package frontend;

public class Subparagraphs {
	private String Number;
	private RequiredBy[] requiredby;
	private String errorMsg;
	
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
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
		if(errorMsg == null && getNumber() != null) {
			return "§§ " + getNumber();
		} else if(getNumber() == null)  {
			return "Keine Einträge vorhanden!";
		} else {
			return errorMsg;
		}
	}
}
