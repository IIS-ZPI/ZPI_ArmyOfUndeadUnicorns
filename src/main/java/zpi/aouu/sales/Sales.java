package zpi.aouu.sales;

public class Sales {
    public static ProductSaleData getProductSaleData(String productName, String productDescription, String stateName, String category, double tax, double basePrice, double preferredFinalPrice, double logisticCost) {
        double noTaxPrice = calculateProductNoTaxPrice(tax, preferredFinalPrice);
        return new ProductSaleData(
                productName,
                productDescription,
                stateName,
                category,
                basePrice,
                tax,
                calculateProductTaxValue(preferredFinalPrice, noTaxPrice),
                logisticCost,
                noTaxPrice,
                preferredFinalPrice,
                calculateProfit(noTaxPrice, logisticCost, basePrice));
    }

    private static double calculateProfit(double noTaxPrice, double logisticCost, double basePrice) {
        return noTaxPrice - logisticCost - basePrice;
    }

    private static double calculateProductTaxValue(double taxPrice, double noTaxPrice) {
        return taxPrice - noTaxPrice;
    }

    private static double calculateProductNoTaxPrice(double tax, double taxPrice) {
        return taxPrice / (1 + tax);
    }
}
