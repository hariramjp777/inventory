package com.inventory.model.objects.order.sales;

import com.inventory.model.database.Database;
import com.inventory.model.objects.order.Order;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;


public class SalesOrderDAO {
    public JSONObject createSalesOrder(SalesOrder salesOrder) throws Exception {
        JSONObject json = new JSONObject();
        int aiID = Database.executeUpdate("insert into salesorders (customer_id, organization_id, status, is_invoiced, sales_order_ref_number) values (?,?,?,?,?)", new Object[] {
                salesOrder.getCustomerID(),
                salesOrder.getOrganizationID(),
                salesOrder.getStatus(),
                salesOrder.isInvoiced(),
                salesOrder.getSalesOrderRefNumber()
        });
        float totalOrderPrice = 0.0f;
        JSONArray lineItems = new JSONArray();
        for (Order order : salesOrder.getOrders()) {
            JSONObject resJSON = Database.executeQuery("select sales_rate, physical_stock_on_hand, physical_committed_stock, physical_available_for_sale, accounting_stock_on_hand, accounting_committed_stock, accounting_available_for_sale from items where id = '" +
                    order.getItemID() + "'");
            JSONArray result = resJSON.getJSONArray("result");
            if (result.length() == 0) {
                throw new Exception("Item doesn't exist");
            }
            float salesRate = result.getJSONObject(0).getFloat("sales_rate");
            int physicalAvailableForSale = result.getJSONObject(0).getInt("physical_available_for_sale");
            int physicalStockOnHand = result.getJSONObject(0).getInt("physical_stock_on_hand");
            int accountingStockOnHand = result.getJSONObject(0).getInt("accounting_stock_on_hand");
            int physicalCommittedStock = result.getJSONObject(0).getInt("physical_committed_stock");
            int accountingCommittedStock = result.getJSONObject(0).getInt("accounting_committed_stock");
            if (physicalAvailableForSale < order.getQuantity()) {
                throw new Exception("Insufficient items");
            }
            float totalPrice = salesRate * order.getQuantity();
            totalOrderPrice += totalPrice;
            int aiID1 = Database.executeUpdate("insert into itemsales (item_id, quantity, rate_per_quantity, total) values(?,?,?,?)", new Object[] {
                    order.getItemID(),
                    order.getQuantity(),
                    salesRate,
                    totalPrice
            });
            lineItems.put(Database.executeQuery("select item_id, quantity, rate_per_quantity, total from itemsales where ai_id = '" +
                    aiID1 + "'").getJSONArray("result").getJSONObject(0));
            if (salesOrder.getStatus().equals("open")) {
                Database.executeUpdate("update items set physical_committed_stock=?, physical_available_for_sale=?, accounting_committed_stock=?, accounting_available_for_sale=? where id = ?", new Object[] {
                        physicalCommittedStock + order.getQuantity(),
                        physicalAvailableForSale - order.getQuantity(),
                        accountingCommittedStock + order.getQuantity(),
                        accountingCommittedStock - order.getQuantity(),
                        order.getItemID()
                });
            }
        }
        Database.executeUpdate("update salesorders set total_price=? where ai_id=?", new Object[] {
                totalOrderPrice,
                aiID
        });
        JSONObject createdSalesOrderJSON = Database.executeQuery("select id, customer_id, organization_id, status, is_invoiced, total_price, sales_order_ref_number from salesorders where ai_id = '" +
                aiID + "'").getJSONArray("result").getJSONObject(0);
        createdSalesOrderJSON.put("lineItems", lineItems);
        json.put("code", 0);
        json.put("message", "success");
        json.put("created_sales_order", createdSalesOrderJSON);
        return json;
    }
}
