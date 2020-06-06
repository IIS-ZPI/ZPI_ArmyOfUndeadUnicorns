package zpi.aouu.database;

import com.google.gson.JsonArray;
import zpi.aouu.jsonservice.ResultSetToJsonMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseQuery {
    private DatabaseQuery() {
        throw new IllegalStateException("DatabaseQuery class");
    }

    public static JsonArray query(String query) {
        JsonArray jsonArray = null;
        Statement statement;
        ResultSet resultSet;
        try (Connection connection = DatabaseConnection.openConnection()) {
            statement = connection.createStatement();
            if(query.contains("SELECT")) {
                resultSet = statement.executeQuery(query);
                jsonArray = resultSetToJsonArray(resultSet);
                resultSet.close();
            } else {
                statement.executeUpdate(query);
            }
            statement.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return jsonArray;
    }

    private static JsonArray resultSetToJsonArray(ResultSet resultSet) throws SQLException {
        JsonArray jsonArray;
        jsonArray = new JsonArray();
        while (resultSet.next()) {
            jsonArray = ResultSetToJsonMapper.mapResultSet(resultSet);
        }
        return jsonArray;
    }
}
