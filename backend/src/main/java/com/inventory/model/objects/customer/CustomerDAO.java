package com.inventory.model.objects.customer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inventory.model.database.Database;

import java.sql.*;

public class CustomerDAO {
    public JsonObject createCustomer(String contactName, String companyName, String customerType, String country, int organizationID) throws SQLException, ClassNotFoundException {
        Connection connection = Database.initializeDataBase();
        PreparedStatement preparedStatement = connection.prepareStatement("insert into customers (contact_name, company_name, customer_type, country, organization_id) values (?,?,?,?,?)");
        preparedStatement.setString(1, contactName);
        preparedStatement.setString(2, companyName);
        preparedStatement.setString(3, customerType);
        preparedStatement.setString(4, country);
        preparedStatement.setInt(5, organizationID);
        preparedStatement.executeUpdate();
        Gson gson = new Gson();
        JsonObject json = gson.toJsonTree(new Object()).getAsJsonObject();
        json.addProperty("code", 0);
        json.addProperty("message", "success");
        json.add("created_customer", gson.toJsonTree(new Customer(contactName, companyName, customerType, country, organizationID)));
        return json;
    }
}
