package zpi.aouu.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.Request;
import spark.Response;
import zpi.aouu.database.DatabaseQuery;
import zpi.aouu.sales.CountryData;
import zpi.aouu.sales.ProductBasic;
import zpi.aouu.sales.ProductSaleAbroadData;

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

        ProductBasic product = new Gson().fromJson(
                    DatabaseQuery.query(String.format(QUERY_PRODUCT, req.params("productName"))).get(0),
                    ProductBasic.class);

        System.out.println("Logisitc cost: <" + req.params("logisticCost") + ">");

        List<ProductSaleAbroadData> result = new ArrayList<>();
        for(CountryData country : countries) {
            result.add(new ProductSaleAbroadData(
                    req.params("productName"),
                    product.productDescription,
                    country.name,
                    country.currency,
                    product.category,
                    product.basePrice,
                    Integer.parseInt(product.quantity),
                    country.importTariff,
                    country.importTariff * product.basePrice,
                    country.transportFee,
                    Double.parseDouble(req.params("logisticCost")),
                    -1,
                    -1,
                    -1
            ));
        };

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