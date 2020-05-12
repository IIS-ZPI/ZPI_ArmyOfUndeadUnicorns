package zpi.aouu.sales;

import com.google.gson.JsonObject;

public class ProductSaleData {
    public final String PRODUCT_NAME;
    public final String PRODUCT_DESCRIPTION;
    public final String STATE_NAME;
    public final String CATEGORY;
    public final double BASE_PRICE;
    public final double TAX;    // = (tax in $) / (price without tax)
    public final double TAX_VALUE;  // In $
    public final double LOGISTIC_COST;
    public final double NO_TAX_PRICE;
    public final double FINAL_PRICE;
    public final double PROFIT;

    ProductSaleData(String product, String productDescription, String state, String category, double basePrice, double tax, double taxValue, double logisticCost, double noTaxPrice, double finalPrice, double profit) {
        this.PRODUCT_NAME = product;
        this.PRODUCT_DESCRIPTION = productDescription;
        this.STATE_NAME = state;
        this.CATEGORY = category;
        this.BASE_PRICE = basePrice;
        this.TAX = tax;
        this.TAX_VALUE = taxValue;
        this.LOGISTIC_COST = logisticCost;
        this.NO_TAX_PRICE = noTaxPrice;
        this.FINAL_PRICE = finalPrice;
        this.PROFIT = profit;
    }

    @Override
    public String toString() {
        return "ProductSaleData {" +
                "PRODUCT_NAME='" + PRODUCT_NAME + '\'' +
                ", PRODUCT_DESCRIPTION='" + PRODUCT_DESCRIPTION + '\'' +
                ", STATE_NAME='" + STATE_NAME + '\'' +
                ", CATEGORY='" + CATEGORY + '\'' +
                ", BASE_PRICE=" + BASE_PRICE +
                ", TAX=" + TAX +
                ", TAX_VALUE=" + TAX_VALUE +
                ", LOGISTIC_COST=" + LOGISTIC_COST +
                ", NO_TAX_PRICE=" + NO_TAX_PRICE +
                ", FINAL_PRICE=" + FINAL_PRICE +
                ", PROFIT=" + PROFIT +
                '}';
    }
}
