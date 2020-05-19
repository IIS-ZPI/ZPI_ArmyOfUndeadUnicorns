package zpi.aouu.util;

public enum Paths {
	START_PAGE("/"),
	STATES("/states"),
	PRODUCTS("/products"),
	CALCULATE_PRICE("/price/:productName/:finalPrice/:logisticCost"),
	STATES_FOR_PRODUCT("/availableStatesForProduct");

	public final String path;

	private Paths(String path) {
		this.path = path;
	}

}