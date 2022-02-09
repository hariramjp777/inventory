package com.inventory.model.database;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class Database {
    public static Connection initializeDataBase() throws SQLException, ClassNotFoundException {
        String databaseDriver = "com.mysql.cj.jdbc.Driver";
        String databaseURL = "jdbc:mysql://localhost:3306/inventory_v1";
        String databaseUsername = "local";
        String databasePassword = "local@123";

        Class.forName(databaseDriver);
        return DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);
    }

    public static JSONObject executeQuery(String sqlString) throws SQLException, ClassNotFoundException {
        Connection connection = Database.initializeDataBase();
        JSONObject json = new JSONObject();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlString);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int numberOfColumns = resultSetMetaData.getColumnCount();
            JSONObject innerJSON = new JSONObject();
            for (int i = 1; i <= numberOfColumns; i++) {
                String columnName = resultSetMetaData.getColumnName(i);
                if (resultSetMetaData.getColumnType(i) == -6) {
                    innerJSON.put(columnName, resultSet.getBoolean(i));
                    continue;
                }
                innerJSON.put(columnName, resultSet.getObject(i));
            }
            jsonArray.put(innerJSON);
        }
        json.put("result", jsonArray);
        statement.close();
        connection.close();
        return json;
    }

    public static int executeUpdate(String sqlPlaceholderString, Object[] options) throws Exception {
        Connection connection = Database.initializeDataBase();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlPlaceholderString, Statement.RETURN_GENERATED_KEYS);
        int lengthOfOptions = options.length;
        for (int i = 0; i < lengthOfOptions; i++) {
            if (options[i] instanceof String) {
                preparedStatement.setString(i + 1, (String) options[i]);
                continue;
            }
            if (options[i] instanceof Integer) {
                preparedStatement.setInt(i + 1, (Integer) options[i]);
                continue;
            }
            if (options[i] instanceof Double) {
                preparedStatement.setDouble(i + 1, (Double) options[i]);
                continue;
            }
            if (options[i] instanceof Boolean) {
                preparedStatement.setBoolean(i + 1, (Boolean) options[i]);
                continue;
            }
            if (options[i] instanceof Float) {
                preparedStatement.setFloat(i + 1, (Float) options[i]);
            }
            else {
                throw new Exception("Type not allowed" + options[i] + i);
            }
        }
        preparedStatement.executeUpdate();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt(1);
        }
        preparedStatement.close();
        connection.close();
        return id;
    }
}
