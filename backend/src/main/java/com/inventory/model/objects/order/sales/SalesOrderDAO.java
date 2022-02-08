package com.inventory.model.objects.order.sales;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inventory.model.database.Database;
import com.inventory.model.objects.order.Order;

import java.sql.*;

public class SalesOrderDAO {
    public JsonObject placeSalesOrder(int organizationID, int customerID, String salesOrderRefNumber, Order[] orders) throws SQLException, ClassNotFoundException {
        Connection connection = Database.initializeDataBase();
        PreparedStatement preparedStatement = connection.prepareStatement("insert into salesorders (customer_id, salesorder_ref_number) values(?,?)");
        preparedStatement.setInt(1, customerID);
        preparedStatement.setString(2, salesOrderRefNumber);
        preparedStatement.executeUpdate();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select id from salesorders where salesorder_ref_number = '" + salesOrderRefNumber + "'");
        rs.next();
        int salesOrderID = rs.getInt(1);
        for(Order order : orders) {
            Statement statement1 = connection.createStatement();
            ResultSet rs1 = statement1.executeQuery("select sales_rate, stock_on_hand, committed_stock from items where id = '" + order.itemID + "'");
            rs1.next();
            int ratePerQuantity = rs1.getInt(1);
            int stockOnHand = rs1.getInt(2);
            int committedStock = rs1.getInt(3);
            PreparedStatement preparedStatement1 = connection.prepareStatement("insert into itemsalesorders (item_id, quantity, rate_per_quantity, sales_order_id) values (?,?,?,?)");
            preparedStatement1.setInt(1, order.itemID);
            preparedStatement1.setInt(2, order.quantity);
            preparedStatement1.setInt(3, ratePerQuantity);
            preparedStatement1.setInt(4, salesOrderID);
            preparedStatement1.executeUpdate();
            committedStock += order.quantity;
            int availableForSale = stockOnHand - committedStock;
            PreparedStatement preparedStatement2 = connection.prepareStatement("update items set committed_stock = ? where id = ?");
            preparedStatement2.setInt(1, committedStock);
            preparedStatement2.setInt(2, order.itemID);
            preparedStatement2.executeUpdate();
            PreparedStatement preparedStatement3 = connection.prepareStatement("update items set available_for_sale = ? where id = ?");
            preparedStatement3.setInt(1, availableForSale);
            preparedStatement3.setInt(2, order.itemID);
            preparedStatement3.executeUpdate();
        }
        Gson gson = new Gson();
        JsonObject json = gson.toJsonTree(new Object()).getAsJsonObject();
        json.addProperty("code", 0);
        json.addProperty("message", "success");
        json.add("created_sales_order", gson.toJsonTree(new SalesOrder(organizationID, customerID, orders, salesOrderRefNumber)));
        return json;
    }
}
