package com.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection initializeDataBase() throws SQLException, ClassNotFoundException {
        String databaseDriver = "com.mysql.cj.jdbc.Driver";
        String databaseURL = "jdbc:mysql://localhost:3306/inventory";
        String databaseUsername = "local";
        String databasePassword = "local@123";

        Class.forName(databaseDriver);
        Connection connection = DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);
        return connection;
    }
}
