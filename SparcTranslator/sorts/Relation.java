package sorts;

public enum Relation {
	EQUAL("="), GREATEROREQUAL(">="), GREATER(">"), SMALLER("<"), SMALLEROREQUAL(
			"<="), NOTEQUAL("!=");
	private String image;

	Relation(String image) {
		this.image = image;
	}
    
	public String toString() {
		return image;
	}
}
