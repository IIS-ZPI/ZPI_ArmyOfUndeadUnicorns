package zpi.aouu.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.Request;
import spark.Response;
import zpi.aouu.apiFixerio.ExchangeRatio;
import zpi.aouu.database.DatabaseQuery;
import zpi.aouu.sales.CountryData;
import zpi.aouu.sales.ProductBasic;
import zpi.aouu.sales.ProductSaleAbroadData;
import zpi.aouu.sales.Sales;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PriceAbroad {
    private static final String QUERY_PRODUCT =
            "SELECT\n" +
            "p.name AS \"productName\",\n" +
            "p.description AS \"productDescription\",\n" +
            "c.name AS \"category\",\n" +
            "p.base_price::money::numeric::float8 AS \"basePrice\",\n" +
            "p.quantity AS \"quantity\"\n" +
            "FROM products p\n" +
            "JOIN categories c ON p.category_id = c.id\n" +
            "WHERE p.name = '%s';";

    private static final String QUERY_COUNTRY = "SELECT name as \"name\", code as \"code\", currency as \"currency\"," +
            " transport_fee as \"transportFee\", import_tariff_other as \"importTariff\" from countries WHERE id IN (";

    public static String calculate(Request req, Response res) {
        res.type("application/json");
        if (req.body().isEmpty()) {
            return null;
        }

        List<CountryData> countries = getSelectedCountries(req);

        ExchangeRatio exchangeRatio = new ExchangeRatio(countries, "USD");
        ProductBasic product = new Gson().fromJson(
                DatabaseQuery.query(String.format(QUERY_PRODUCT, req.params("productName"))).get(0),
                ProductBasic.class);

        List<ProductSaleAbroadData> result = new ArrayList<>();
        for(CountryData country : countries) {
            int quantity = Integer.parseInt(product.quantity);
            double basePrice = Sales.quantityPriceModifier(product.basePrice, quantity, 10, 99, 0.1, 0.95) * exchangeRatio.getCountryDataAndExchangeList().get(country);
            double finalPrice = Double.parseDouble(req.params("finalPrice")) * exchangeRatio.getCountryDataAndExchangeList().get(country);
            double logisticCost = country.transportFee * exchangeRatio.getCountryDataAndExchangeList().get(country);
            double noTaxPrice = Sales.calculateProductNoTaxPrice(country.importTariff, finalPrice);
            result.add(new ProductSaleAbroadData(
                    req.params("productName"),
                    product.productDescription,
                    country.name,
                    country.currency,
                    product.category,
                    basePrice,
                    quantity,
                    country.importTariff,
                    country.importTariff * basePrice,
                    logisticCost,
                    noTaxPrice,
                    finalPrice,
                    Sales.calculateProfit(
                            noTaxPrice,
                            logisticCost,
                            basePrice)
            ));
        }

        System.out.println(product);

        return new Gson().toJson(result);
    }

    private static List<CountryData> getSelectedCountries(Request req) {
        List<String> countriesId;
        Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        countriesId = new Gson().fromJson(req.body(), listType);

        StringBuilder rangeBuilder = new StringBuilder();
        String prefix = "";
        for(String country : countriesId) {
            rangeBuilder.append(prefix);
            prefix = ",";
            rangeBuilder.append(country);
        }
        rangeBuilder.append(");");
        String range = rangeBuilder.toString();

        Type listCountryType = new TypeToken<ArrayList<CountryData>>(){}.getType();

        return new Gson().fromJson(DatabaseQuery.query(QUERY_COUNTRY + range), listCountryType);
    }
}
