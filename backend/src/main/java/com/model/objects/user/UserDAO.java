package com.model.objects.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.model.database.Database;

import java.sql.*;

public class UserDAO {
    public JsonObject createUser(String firstName, String lastName, String emailID, String password) throws SQLException, ClassNotFoundException {
        Connection connection = Database.initializeDataBase();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select first_name from users where email_id = '" + emailID + "'");
        if (rs.next()) {
            Gson gson = new Gson();
            JsonObject json = gson.toJsonTree(new Object()).getAsJsonObject();
            json.addProperty("code", "101");
            json.addProperty("message", "A user with this email id already exists");
            return json;
        }
        PreparedStatement preparedStatement = connection.prepareStatement("insert into users (first_name, last_name, email_id, password, is_verified) values(?,?,?,?,?)");
        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, lastName);
        preparedStatement.setString(3, emailID);
        preparedStatement.setString(4, password);
        preparedStatement.setBoolean(5, false);
        preparedStatement.executeUpdate();
        Gson gson = new Gson();
        JsonObject json = gson.toJsonTree(new Object()).getAsJsonObject();
        json.addProperty("code", 0);
        json.addProperty("message", "success");
        json.add("created_user", gson.toJsonTree(new User(firstName, lastName, emailID)));
        return json;
    }

    public JsonObject validateUser(String emailID, String password) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        Connection connection = Database.initializeDataBase();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select first_name from users where email_id = '" + emailID + "'");
        if (!rs.next()) {
            JsonObject json = gson.toJsonTree(new Object()).getAsJsonObject();
            json.addProperty("code", "102");
            json.addProperty("message", "Please create an account to use inventory");
            return json;
        }
        ResultSet rs1 = statement.executeQuery("select password from users where email_id = '" + emailID + "'");
        if (rs1.next()) {
            if (rs1.getString(1).equals(password)) {
                JsonObject json = gson.toJsonTree(new Object()).getAsJsonObject();
                json.addProperty("code", 0);
                json.addProperty("message", "success");
                return json;
            }
            JsonObject json = gson.toJsonTree(new Object()).getAsJsonObject();
            json.addProperty("code", 103);
            json.addProperty("message", "Invalid Email or Password");
            return json;
        }
        return gson.toJsonTree(new Object()).getAsJsonObject();
    }
}
