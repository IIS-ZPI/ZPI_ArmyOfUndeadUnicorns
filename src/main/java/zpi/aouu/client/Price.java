package zpi.aouu.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import spark.Request;
import spark.Response;
import zpi.aouu.database.DatabaseQuery;
import zpi.aouu.sales.Sales;

public class Price {
    static final String QUERY_CALC_BASE = "SELECT\n" +
            "\tp.name AS \"productName\",\n" +
            "\tp.description AS \"productDescription\",\n" +
            "\ts.name AS \"stateName\",\n" +
            "\tc.name AS \"category\",\n" +
            "\tp.base_price::money::numeric::float8 AS \"basePrice\",\n" +
            "\tp.quantity AS \"quantity\",\n" +
            "\tcs.tax AS \"tax\",\n" +
            "\ts.transport_fee::money::numeric::float8 AS \"logisticCost\"\n" +
            "\tFROM products p \n" +
            "\tJOIN categories c ON p.category_id = c.id\n" +
            "\tJOIN categories_by_states cs ON cs.category_id = c.id\n" +
            "\tJOIN states s ON cs.state_id = s.id\n" +
            "\tWHERE p.name = '%s' \n" +
            "\t\tAND cs.state_id IN (";    // Add needed state IDs to complete

    private Price() {
        throw new IllegalStateException("Price class");
    }

    public static JsonArray calculate(Request req, Response res) {
        res.type("application/json");

        JsonArray states;
        if (!req.body().isEmpty()) {
            states = new Gson().fromJson(req.body(), JsonArray.class);
        } else {
            return null;
        }

        JsonArray queryResultJson = DatabaseQuery.query(generateQuery(states, req.params("productName")));

        JsonArray result = new JsonArray();
        Gson gson = new Gson();
        for(JsonElement element : queryResultJson) {  // Do calculation and make JsonArray out of results
            result.add(gson.toJsonTree(Sales.getProductSaleData(
                    element.getAsJsonObject(),
                    Double.parseDouble(req.params("finalPrice"))
            )));
        }
        return result;
    }

    private static String generateQuery(JsonArray stateIDs, String productName) {
        StringBuilder query = new StringBuilder(String.format(QUERY_CALC_BASE, productName));
        for(JsonElement stateID : stateIDs) {
            query.append(stateID.getAsString());
            query.append(", ");
        }
        query.delete(query.length() - 2, query.length());
        query.append(");");
        return query.toString();
    }
}
