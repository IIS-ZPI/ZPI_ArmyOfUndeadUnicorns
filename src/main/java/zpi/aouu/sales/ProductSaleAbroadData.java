package zpi.aouu.sales;

public class ProductSaleAbroadData {
    String product;
    String productDescription;
    String country;
    String currencyCode;
    String category;
    double basePrice;
    int quantity;
    double importTariff;
    double importTariffValue;
    double transportFee;
    double logisticCost;
    double noTaxPrice;
    double finalPrice;
    double profit;

    public ProductSaleAbroadData(String product,
                                 String productDescription,
                                 String country,
                                 String currencyCode,
                                 String category,
                                 double basePrice,
                                 int quantity,
                                 double importTariff,
                                 double importTariffValue,
                                 double transportFee,
                                 double logisticCost,
                                 double noTaxPrice,
                                 double finalPrice,
                                 double profit) {
        this.product = product;
        this.productDescription = productDescription;
        this.country = country;
        this.currencyCode = currencyCode;
        this.category = category;
        this.basePrice = basePrice;
        this.quantity = quantity;
        this.importTariff = importTariff;
        this.importTariffValue = importTariffValue;
        this.transportFee = transportFee;
        this.logisticCost = logisticCost;
        this.noTaxPrice = noTaxPrice;
        this.finalPrice = finalPrice;
        this.profit = profit;
    }
}
