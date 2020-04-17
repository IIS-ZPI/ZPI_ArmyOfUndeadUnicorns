package adrian.spark;

import java.sql.*;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {

        get("/hello", (req, res) -> "Hello, world");

        get("/hello/:name", (req,res)->{
            return "Hello, "+ req.params(":name");
        });

        get("/name", (req, res) -> {
            String name = getNameFromDB();
            //System.out.println(name);
            return name;
        });
    }

    private static String getNameFromDB() {
        String name = null;
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "postgres", "postgres")) {
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
}
