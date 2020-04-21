package zpi.aouu.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection openConnection() throws SQLException {
        String db = "jdbc:postgresql://" + System.getenv("DB_HOST") + ":" + System.getenv("DB_PORT") + "/" + System.getenv("DB_NAME"); // Using environment variables to hide sensitive data
        DriverManager.getConnection(db, System.getenv("DB_USER"), System.getenv("DB_PASSWORD"));
        return DriverManager.getConnection(db, System.getenv("DB_USER"), System.getenv("DB_PASSWORD"));
    }
}
