package com.inventory.model.objects.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inventory.model.database.Database;
import org.json.JSONObject;

import java.sql.*;

public class UserDAO {
    public JSONObject getUser(String email) throws SQLException, ClassNotFoundException {
        return Database.executeQuery("select id, first_name, last_name, email from users where email = '" + email + "'");
    }

    public JSONObject createUser(String firstName, String lastName, String email, String password) throws Exception {
        JSONObject json = new JSONObject();
        JSONObject userWithSameEmail = getUser(email);
        if (userWithSameEmail.getJSONArray("result").length() != 0) {
            json.put("code", 101);
            json.put("message", "A user with this email id already exists");
            return json;
        }
        Database.executeUpdate("insert into users (first_name, last_name, email, password) values (?,?,?,?)", new Object[]{firstName, lastName, email, password});
        json.put("code", 0);
        json.put("message", "success");
        json.put("created_user", getUser(email).getJSONArray("result").get(0));
        return json;
    }
}
