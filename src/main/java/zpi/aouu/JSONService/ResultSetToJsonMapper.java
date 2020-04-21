package zpi.aouu.JSONService;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

public class ResultSetToJsonMapper {
    public static JsonArray mapResultSet(ResultSet rs) throws SQLException, JsonIOException {
        JsonArray jArray = new JsonArray();
        JsonObject jsonObject;
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        while (rs.next()) {
            jsonObject = new JsonObject();
            for (int index = 1; index <= columnCount; index++) {
                String column = rs.getMetaData().getColumnLabel(index);
                Object value = rs.getObject(column);
                if (value == null) {
                    jsonObject.addProperty(column, "");
                } else if (value instanceof Number) {
                    jsonObject.addProperty(column, (Number) value);
                } else if (value instanceof String) {
                    jsonObject.addProperty(column, (String) value);
                } else if (value instanceof Boolean) {
                    jsonObject.addProperty(column, (Boolean) value);
                } else if (value instanceof Date) {
                    jsonObject.addProperty(column, ((Date) value).getTime());
                } else if (value instanceof byte[]) {
                    jsonObject.addProperty(column, String.valueOf(value));
                } else {
                    throw new IllegalArgumentException("Unmappable object type: " + value.getClass());
                }
            }
            jArray.add(jsonObject);
        }
        return jArray;
    }
}
