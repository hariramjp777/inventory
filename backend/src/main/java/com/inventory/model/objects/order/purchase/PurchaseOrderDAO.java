package com.inventory.model.objects.order.purchase;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inventory.model.database.Database;
import com.inventory.model.objects.order.Order;

import java.sql.*;

public class PurchaseOrderDAO {
    public JsonObject placePurchaseOrder(int organizationID, int vendorID, String purchaseOrderRefNumber, Order[] orders) throws SQLException, ClassNotFoundException {
        Connection connection = Database.initializeDataBase();
        PreparedStatement preparedStatement = connection.prepareStatement("insert into purchaseorders (vendor_id, purchaseorder_ref_number) values(?,?)");
        preparedStatement.setInt(1, vendorID);
        preparedStatement.setString(2, purchaseOrderRefNumber);
        preparedStatement.executeUpdate();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select id from purchaseorders where purchaseorder_ref_number = '" + purchaseOrderRefNumber + "'");
        rs.next();
        int purchaseOrderID = rs.getInt(1);
        for(Order order : orders) {
            Statement statement1 = connection.createStatement();
            ResultSet rs1 = statement1.executeQuery("select purchase_rate, stock_on_hand from items where id = '" + order.itemID + "'");
            rs1.next();
            int ratePerQuantity = rs1.getInt(1);
            int stockOnHand = rs1.getInt(2);
            PreparedStatement preparedStatement1 = connection.prepareStatement("insert into itempurchaseorders (item_id, quantity, rate_per_quantity, purchase_order_id) values (?,?,?,?)");
            preparedStatement1.setInt(1, order.itemID);
            preparedStatement1.setInt(2, order.quantity);
            preparedStatement1.setInt(3, ratePerQuantity);
            preparedStatement1.setInt(4, purchaseOrderID);
            preparedStatement1.executeUpdate();
            int availableForSale = stockOnHand + order.quantity;
            PreparedStatement preparedStatement2 = connection.prepareStatement("update items set available_for_sale = ? where id = ?");
            preparedStatement2.setInt(1, availableForSale);
            preparedStatement2.setInt(2, order.itemID);
            preparedStatement2.executeUpdate();
        }
        Gson gson = new Gson();
        JsonObject json = gson.toJsonTree(new Object()).getAsJsonObject();
        json.addProperty("code", 0);
        json.addProperty("message", "success");
        json.add("created_purchase_order", gson.toJsonTree(new PurchaseOrder(organizationID, vendorID, orders, purchaseOrderRefNumber)));
        return json;
    }
}
