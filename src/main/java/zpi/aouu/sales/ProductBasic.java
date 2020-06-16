package zpi.aouu.sales;

public class ProductBasic {
    final public String productName;
    final public String productDescription;
    final public String category;
    final public double basePrice;
    final public String quantity;

    public ProductBasic(String productName,
                        String productDescription,
                        String category,
                        double basePrice,
                        String quantity) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.category = category;
        this.basePrice = basePrice;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ProductBasic {" +
                "productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", category='" + category + '\'' +
                ", basePrice=" + basePrice +
                ", quantity='" + quantity +
                "'}";
    }
}
