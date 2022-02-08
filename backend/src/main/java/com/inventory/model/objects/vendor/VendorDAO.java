package com.inventory.model.objects.vendor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inventory.model.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VendorDAO {
    public JsonObject createVendor(String contactName, String companyName, String country, int organizationID) throws SQLException, ClassNotFoundException {
        Connection connection = Database.initializeDataBase();
        PreparedStatement preparedStatement = connection.prepareStatement("insert into vendors (contact_name, company_name, country, organization_id) values (?,?,?,?)");
        preparedStatement.setString(1, contactName);
        preparedStatement.setString(2, companyName);
        preparedStatement.setString(3, country);
        preparedStatement.setInt(4, organizationID);
        preparedStatement.executeUpdate();
        Gson gson = new Gson();
        JsonObject json = gson.toJsonTree(new Object()).getAsJsonObject();
        json.addProperty("code", 0);
        json.addProperty("message", "success");
        json.add("created_vendor", gson.toJsonTree(new Vendor(contactName, companyName, country, organizationID)));
        return json;
    }
}
