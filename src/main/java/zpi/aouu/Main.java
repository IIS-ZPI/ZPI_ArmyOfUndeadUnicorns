package zpi.aouu;

import spark.Spark;
import spark.utils.IOUtils;

import java.io.IOException;
import java.sql.*;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        staticFiles.location("/static");

        // Testing REST and DB
        get("/hello", (req, res) -> "Hello, world");

        get("/hello/:name", (req,res)-> "Hello, " + req.params(":name"));

        get("/name", (req, res) -> getNameFromDB());

        get("/", (q, a) -> renderContent("/static/index.html"));

    }

    private static String getNameFromDB() {
        String name = null;
        String db = "jdbc:postgresql://" + System.getenv("DB_HOST")+ ":" + System.getenv("DB_PORT") + "/" + System.getenv("DB_NAME"); // Using environment variables to hide sensitive data
        try (Connection connection = DriverManager.getConnection(db, System.getenv("DB_USER"), System.getenv("DB_PASSWORD"))) {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM people WHERE imie = 'Jan'";
            ResultSet result = statement.executeQuery(query);
            if(result.next()) {
                name = result.getString("imie") + " " + result.getString("nazwisko");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return name;
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
