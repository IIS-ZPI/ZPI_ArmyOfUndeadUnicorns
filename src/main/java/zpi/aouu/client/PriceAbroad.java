package zpi.aouu.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import spark.Request;
import spark.Response;
import zpi.aouu.database.DatabaseQuery;
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

    public static String calculate(Request req, Response res) {
        res.type("application/json");
        if (req.body().isEmpty()) {
            return null;
        }

        List<String> countries = getSelectedCountries(req);
        for(String country : countries) {
            System.out.println(country);
        }

        ProductBasic product = new Gson().fromJson(
                    DatabaseQuery.query(String.format(QUERY_PRODUCT, req.params("productName"))).get(0),
                    ProductBasic.class);

        System.out.println("Logisitc cost: <" + req.params("logisticCost") + ">");

        List<ProductSaleAbroadData> result = new ArrayList<>();
        for(String country : countries) {
            result.add(new ProductSaleAbroadData(
                    req.params("productName"),
                    product.productDescription,
                    country,
                    "[Currency]",
                    "[Category]",
                    product.basePrice,
                    Integer.parseInt(product.quantity),
                    -1,
                    -1,
                    -1,
                    Double.parseDouble(req.params("logisticCost")),
                    -1,
                    -1,
                    -1
            ));
        };

        System.out.println(product);

        return new Gson().toJson(result);
    }

    private static List<String> getSelectedCountries(Request req) {
        List<String> countries;
        Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        countries = new Gson().fromJson(req.body(), listType);
        return countries;
    }
}
