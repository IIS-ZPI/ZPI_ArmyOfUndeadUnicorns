package zpi.aouu.webapp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import spark.Request;
import spark.Response;
import zpi.aouu.DatabaseConnection.DatabaseConnection;
import zpi.aouu.JSONService.ResultSetToJsonMapper;
import zpi.aouu.sales.Sales;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Price {
    final static String QUERY_CALC_BASE = "SELECT\n" +
            "\tp.name AS \"productName\",\n" +
            "\tp.description AS \"productDescription\",\n" +
            "\ts.name AS \"stateName\",\n" +
            "\tc.name AS \"category\",\n" +
            "\tcs.tax AS \"tax\",\n" +
            "\tp.base_price::money::numeric::float8 AS \"basePrice\"\n" +
            "\tFROM products p \n" +
            "\tJOIN categories c ON p.category_id = c.id\n" +
            "\tJOIN categories_by_states cs ON cs.category_id = c.id\n" +
            "\tJOIN states s ON cs.state_id = s.id\n" +
            "\tWHERE p.name = '%s' \n" +
            "\t\tAND cs.state_id IN (";    // Add needed state IDs to complete

    public static JsonArray calculate(Request req, Response res) {
        res.type("application/json");

        JsonArray states;
        if(!req.body().isEmpty()) {
            states = new Gson().fromJson(req.body(), JsonArray.class);
            System.out.println(states);
        } else {
            return null;
        }

        JsonArray queryResultJson;
        try(Connection connection = DatabaseConnection.openConnection()) {  // Get data from DB
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(generateQuery(states, req.params("productName")));
            queryResultJson = new JsonArray();
            while (result.next()) {
                queryResultJson = ResultSetToJsonMapper.mapResultSet(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

        JsonArray result = new JsonArray();
        Gson gson = new Gson();
        for(JsonElement element : queryResultJson) {  // Do calculation and make JsonArray out of results
            result.add(gson.toJsonTree(Sales.getProductSaleData(
                    element.getAsJsonObject(),
                    Double.parseDouble(req.params("finalPrice")),
                    Double.parseDouble(req.params("logisticCost"))
            )));
        }

        //System.out.println(result.toString());

        return result;
    }

    public static String generateQuery(JsonArray stateIDs, String productName) {
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
