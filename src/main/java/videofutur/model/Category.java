package videofutur.model;

public enum Category {

	HORREUR("horreur"),
    COMEDIE("comedie"),
    ACTION("action");
	
	private String name;

	private Category(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
}
