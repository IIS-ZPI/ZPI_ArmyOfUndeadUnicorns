package zpi.aouu.client;

import com.google.gson.JsonArray;
import spark.Request;
import spark.Response;
import zpi.aouu.database.DatabaseQuery;

public class Country {
    public static JsonArray getCountries(Request req, Response res) {  //  Format: [{"text": "Afghanistan", "value": "AF"}]
        res.type("application/json");
        String query = "SELECT name as \"text\", id as \"value\" FROM countries ORDER BY text ASC;";
        return DatabaseQuery.query(query);
    }
}
