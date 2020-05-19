package zpi.aouu.client;

import com.google.gson.JsonArray;
import zpi.aouu.database.DatabaseConnection;
import zpi.aouu.jsonservice.ResultSetToJsonMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Product {
	public static JsonArray getProduct() {
		JsonArray jsonArray = null;
		try (Connection connection = DatabaseConnection.openConnection()) {
			Statement statement = connection.createStatement();
			String query = "SELECT p.name, c.name as column_name, p.description, p.base_price::money::numeric::float8  FROM products p join categories c on p.category_id = c.id;";
			ResultSet resultSet = statement.executeQuery(query);
			jsonArray = new JsonArray();
			while (resultSet.next()) {
				jsonArray = ResultSetToJsonMapper.mapResultSet(resultSet);
			}
			statement.close();
			resultSet.close();
		} catch (SQLException throwable) {
			throwable.printStackTrace();

		}
		return jsonArray;
	}

	public static JsonArray getAvailableStates(String productName) {
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
			resultSet.close();
			statement.close();
		} catch (SQLException throwable) {
			throwable.printStackTrace();
		}
		return jsonArray;
	}
}
