package com.model.objects.organization;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.model.database.Database;
import com.model.objects.user.User;

import java.sql.*;

public class OrganizationDAO {
    public JsonObject createOrganization(String organizationName, String industryType, String country, String sessionUserEmail) throws SQLException, ClassNotFoundException {
        Connection connection = Database.initializeDataBase();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select id from users where email_id = '" + sessionUserEmail + "'");
        rs.next();
        int sessionUserID = rs.getInt(1);
        PreparedStatement preparedStatement = connection.prepareStatement("insert into organizations (organization_name, industry_type, country, user_id) values (?,?,?,?)");
        preparedStatement.setString(1, organizationName);
        preparedStatement.setString(2, industryType);
        preparedStatement.setString(3, country);
        preparedStatement.setInt(4, sessionUserID);
        preparedStatement.executeUpdate();
        Gson gson = new Gson();
        JsonObject json = gson.toJsonTree(new Object()).getAsJsonObject();
        json.addProperty("code", 0);
        json.addProperty("message", "success");
        json.add("created_organization", gson.toJsonTree(new User(organizationName, industryType, country)));
        return json;
    }
}
