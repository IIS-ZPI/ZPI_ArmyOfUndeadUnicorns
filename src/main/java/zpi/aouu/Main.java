package zpi.aouu;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import spark.Spark;
import spark.utils.IOUtils;
import zpi.aouu.DatabaseConnection.DatabaseConnection;
import zpi.aouu.JSONService.ResultSetToJsonMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        staticFiles.location("/static");
        get("/products", (req, res) -> {
            res.type("application/json");
            return getProduct();
        });

        get("/availableStatesForProduct", "application/json", (req, res) -> {
            String product = req.queryParams("product");
            if (product == null) throw new IllegalArgumentException();
            else return getAvailableStatesValueForProduct(product);
        });

        get("/states", (req, res) -> {
            res.type("application/json");
            return new Gson().fromJson("[\n" +
                    "{\n" +
                    "   \"text\":\"Alabama\",\n" +
                    "   \"value\":\"1\"\n" +
                    "},\n" +
                    "{\n" +
                    "   \"text\":\"California\",\n" +
                    "   \"value\":\"4\"\n" +
                    "},\n" +
                    " {\n" +
                    "   \"text\":\"Arizona\",\n" +
                    "  \"value\":\"3\"\n" +
                    "}\n" +
                    "]", JsonArray.class);
        });

        get("/", (q, a) -> renderContent("/static/index.html"));

    }

    private static JsonArray getProduct() {
        JsonArray jsonArray = null;
        try (Connection connection = DatabaseConnection.openConnection()) {
            Statement statement = connection.createStatement();
            String query = "SELECT p.name, c.name as column_name, p.description, p.base_price::money::numeric::float8  FROM products p join categories c on p.category_id = c.id;";
            ResultSet resultSet = statement.executeQuery(query);
            jsonArray = new JsonArray();
            while (resultSet.next()) {
                jsonArray = ResultSetToJsonMapper.mapResultSet(resultSet);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return jsonArray;
    }


    private static JsonArray getAvailableStatesValueForProduct(String productName) {
        JsonArray jsonArray = null;
        try (Connection connection = DatabaseConnection.openConnection()) {
            Statement statement = connection.createStatement();
            String query = "SELECT category_id from products where name = '" + productName + "'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String categoryId = resultSet.getString("category_id");
                if (categoryId == null) throw new IllegalArgumentException();
                else {
                    query = "SELECT s.name, cs.tax from categories_by_states cs join states s on s.id = cs.state_id where category_id = '" + categoryId + "' ";
                    resultSet = statement.executeQuery(query);
                    jsonArray = new JsonArray();
                    while (resultSet.next()) {
                        jsonArray = ResultSetToJsonMapper.mapResultSet(resultSet);
                    }
                }
            } else throw new IllegalArgumentException();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return jsonArray;

    }


    private static String renderContent(String htmlFile) {
        String html = null;
        try {
            html = IOUtils.toString(Spark.class.getResourceAsStream(htmlFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
