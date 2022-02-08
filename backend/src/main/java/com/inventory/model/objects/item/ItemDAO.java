package com.inventory.model.objects.item;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inventory.model.database.Database;

import java.sql.*;

public class ItemDAO {
    public JsonObject createItem(String itemName, String itemUnit, String productType, int stockOnHand, float salesRate, float purchaseRate, int organizationID) throws SQLException, ClassNotFoundException {
        Connection connection = Database.initializeDataBase();
        PreparedStatement preparedStatement = connection.prepareStatement("insert into items (item_name, item_unit, product_type, stock_on_hand, sales_rate, purchase_rate, organization_id, committed_stock, available_for_sale) values (?,?,?,?,?,?,?,?,?)");
        preparedStatement.setString(1, itemName);
        preparedStatement.setString(2, itemUnit);
        preparedStatement.setString(3, productType);
        preparedStatement.setInt(4, stockOnHand);
        preparedStatement.setFloat(5, salesRate);
        preparedStatement.setFloat(6, purchaseRate);
        preparedStatement.setFloat(7, organizationID);
        preparedStatement.setInt(8, 0);
        preparedStatement.setInt(9, stockOnHand);
        preparedStatement.executeUpdate();
        Gson gson = new Gson();
        JsonObject json = gson.toJsonTree(new Object()).getAsJsonObject();
        json.addProperty("code", 0);
        json.addProperty("message", "success");
        json.add("created_item", gson.toJsonTree(new Item(itemName, itemUnit, productType, stockOnHand, 0, stockOnHand, salesRate, purchaseRate, organizationID)));
        return json;
    }

    public JsonObject viewItem(int organizationID, int itemID) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        Connection connection = Database.initializeDataBase();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select item_name, item_unit, product_type, stock_on_hand, committed_stock, available_for_sale, sales_rate, purchase_rate, organization_id  from items where id = '" + itemID + "'");
        if (rs.next())  {
            JsonObject json = gson.toJsonTree(new Object()).getAsJsonObject();
            json.addProperty("code", 0);
            json.addProperty("message", "success");
            json.add("item", gson.toJsonTree(new Item(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5),rs.getInt(6), rs.getFloat(7), rs.getFloat(8), rs.getInt(9))));
            return json;
        }
        JsonObject json = gson.toJsonTree(new Object()).getAsJsonObject();
        json.addProperty("code", 108);
        json.addProperty("message", "Item doesn't exist");
        return json;
    }
}
