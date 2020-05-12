package zpi.aouu.sales;

import com.google.gson.JsonObject;

public class Sales {
    public static ProductSaleData getProductSaleData(String productName, String productDescription, String stateName, String category, double basePrice, double tax, double preferredFinalPrice, double logisticCost) {
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

    public static ProductSaleData getProductSaleData(JsonObject productData, double preferredFinalPrice, double logisticCost) {
        return getProductSaleData(
                productData.get("productName").getAsString(),
                productData.get("productDescription").getAsString(),
                productData.get("stateName").getAsString(),
                productData.get("category").getAsString(),
                productData.get("basePrice").getAsDouble(),
                productData.get("tax").getAsDouble(),
                preferredFinalPrice,
                logisticCost
        );
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
