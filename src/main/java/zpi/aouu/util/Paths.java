package zpi.aouu.util;

public enum Paths {
	STATES("/states"),
	PRODUCTS("/products"),
	CALCULATE_PRICE("/price/:productName/:finalPrice");

	public final String path;

	Paths(String path) {
		this.path = path;
	}

}