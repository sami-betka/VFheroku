package videofutur.model;

public enum Delivery {

	HOME("home"),
    RELAY("relay");
	
	private String name;

	private Delivery(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
}