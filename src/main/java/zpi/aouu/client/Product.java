package zpi.aouu.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import spark.Request;
import spark.Response;
import zpi.aouu.database.DatabaseConnection;
import zpi.aouu.database.DatabaseQuery;
import zpi.aouu.jsonservice.ResultSetToJsonMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

public class Product {
	private Product() {
		throw new IllegalStateException("Product class");
	}

	public static JsonArray getProduct() {
		JsonArray jsonArray = null;
		try (Connection connection = DatabaseConnection.openConnection()) {
			Statement statement = connection.createStatement();
			String query = "SELECT p.name, c.name as column_name, p.description, p.base_price::money::numeric::float8, p.quantity  FROM products p join categories c on p.category_id = c.id;";
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

	public static String updateProduct(Request req, Response res) {
		JsonObject productUpdated;
		if (!req.body().isEmpty()) {
			productUpdated = new Gson().fromJson(req.body(), JsonObject.class);
		} else {
			res.status(400);
			return "not ok";
		}

		String query = String
				.format(Locale.US,
						"UPDATE products SET description = '%s', base_price = %f, quantity = %d WHERE name = '%s';",
						productUpdated.get("description").getAsString(),
						productUpdated.get("base_price").getAsDouble(),
						productUpdated.get("quantity").getAsInt(),
						req.params("productId"));
		DatabaseQuery.query(query);

		return "ok";
	}
}
