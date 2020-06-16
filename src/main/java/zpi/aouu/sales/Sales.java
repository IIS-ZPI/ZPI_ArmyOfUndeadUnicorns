package zpi.aouu.sales;

import com.google.gson.JsonObject;

public class Sales {
    private Sales() {
        throw new IllegalStateException("Sales class");
    }

    public static ProductSaleData getProductSaleData(String productName,
                                                     String productDescription,
                                                     String stateName,
                                                     String category,
                                                     double basePrice,
                                                     int quantity,
                                                     double tax,
                                                     double preferredFinalPrice,
                                                     double logisticCost) {
        basePrice = quantityPriceModifier(basePrice, quantity, 10, 99, 0.1, 0.95);
        double noTaxPrice = calculateProductNoTaxPrice(tax, preferredFinalPrice);
        return new ProductSaleData(
                productName,
                productDescription,
                stateName,
                category,
                basePrice,
                quantity,
                tax,
                calculateProductTaxValue(preferredFinalPrice, noTaxPrice),
                logisticCost,
                noTaxPrice,
                preferredFinalPrice,
                calculateProfit(noTaxPrice, logisticCost, basePrice));
    }

    public static ProductSaleData getProductSaleData(JsonObject productData, double preferredFinalPrice, double logisticCost) {
        int quantity;
        if(productData.get("quantity").toString().equals("\"\"")) {
            quantity = -1;
        } else {
            quantity = productData.get("quantity").getAsInt();
        }
        return getProductSaleData(
                productData.get("productName").getAsString(),
                productData.get("productDescription").getAsString(),
                productData.get("stateName").getAsString(),
                productData.get("category").getAsString(),
                productData.get("basePrice").getAsDouble(),
                quantity,
                productData.get("tax").getAsDouble(),
                preferredFinalPrice,
                logisticCost
        );
    }

    public static ProductSaleAbroadData getProductSaleAbroadData(ProductBasic productData, CountryData countryData, double preferredFinalPrice, double logisticCost) {

        /*int quantity;
        if(productData.quantity.equals("\"\"")) {
            quantity = -1;
        } else {
            quantity = Integer.parseInt(productData.quantity);
        }
        return new ProductSaleAbroadData(
                productData.productName,
                productData.productDescription,
                countryData.name,
                countryData.code,
                productData.category,
                productData.basePrice,
                quantity,
                countryData.importTariff,
                calculateImportTariffValue(),
                countryData.transportFee,
                logisticCost,
                calculateProductNoTaxPrice(productData)
        );*/
        return null;
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

    public static double quantityPriceModifier(double previousPrice, int quantity, int minQuantity, int maxQuantity, double minModifier, double maxModifier) {
        if(quantity <= maxQuantity && quantity >= minQuantity) {
            return previousPrice * (1 +
                    (((1 - (double)(quantity - minQuantity) / (maxQuantity - minQuantity)) *
                            (maxModifier - minModifier)) + minModifier));
        } else if(quantity < minQuantity && quantity >= 0) {
            return previousPrice * (1 + maxModifier);
        } else {
            return previousPrice;
        }
    }
}
