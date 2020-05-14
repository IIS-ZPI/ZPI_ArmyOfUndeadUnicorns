package zpi.aouu.sales;

public class ProductSaleData {
    public final String productName;
    public final String productDescription;
    public final String stateName;
    public final String category;
    public final double basePrice;
    public final double tax;    // = (tax in $) / (price without tax)
    public final double taxValue;  // In $
    public final double logisticCost;
    public final double noTaxPrice;
    public final double finalPrice;
    public final double profit;

    ProductSaleData(String product, String productDescription, String state, String category, double basePrice, double tax, double taxValue, double logisticCost, double noTaxPrice, double finalPrice, double profit) {
        this.productName = product;
        this.productDescription = productDescription;
        this.stateName = state;
        this.category = category;
        this.basePrice = basePrice;
        this.tax = tax;
        this.taxValue = taxValue;
        this.logisticCost = logisticCost;
        this.noTaxPrice = noTaxPrice;
        this.finalPrice = finalPrice;
        this.profit = profit;
    }

    @Override
    public String toString() {
        return "ProductSaleData {" +
                "PRODUCT_NAME='" + productName + '\'' +
                ", PRODUCT_DESCRIPTION='" + productDescription + '\'' +
                ", STATE_NAME='" + stateName + '\'' +
                ", CATEGORY='" + category + '\'' +
                ", BASE_PRICE=" + basePrice +
                ", TAX=" + tax +
                ", TAX_VALUE=" + taxValue +
                ", LOGISTIC_COST=" + logisticCost +
                ", NO_TAX_PRICE=" + noTaxPrice +
                ", FINAL_PRICE=" + finalPrice +
                ", PROFIT=" + profit +
                '}';
    }
}
