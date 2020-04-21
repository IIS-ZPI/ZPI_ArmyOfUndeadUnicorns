package zpi.aouu;

import com.google.gson.JsonArray;
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

        // Testing REST and DB
        get("/hello", (req, res) -> "Hello, world");

        get("/hello/:name", (req, res) -> "Hello, " + req.params(":name"));

        get("/products", (req, res) -> {
            res.type("application/json");
            JsonArray array = getProduct();
            return array;
        });

        get("/", (q, a) -> renderContent("/static/index.html"));

    }

    private static JsonArray getProduct() {
        JsonArray jsonArray = null;
        try (Connection connection = DatabaseConnection.openConnection()) {
            Statement statement = connection.createStatement();
            String query = "SELECT p.name,c.name as column_name , p.description, p.base_price::money::numeric::float8  FROM products p join categories c on p.category_id = c.id;";
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetToJsonMapper json = new ResultSetToJsonMapper();
            jsonArray = new JsonArray();
            while (resultSet.next()) {
                jsonArray = json.mapResultSet(resultSet);
            }
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
