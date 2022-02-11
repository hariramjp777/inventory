package com.inventory.model.objects.order.sales;

import com.inventory.model.database.Database;
import com.inventory.model.objects.order.Order;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.xml.crypto.Data;
import java.sql.SQLException;
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
        int salesOrderID = Database.executeQuery("select id from salesorders where ai_id = '" +
                aiID + "'").getJSONArray("result").getJSONObject(0).getInt("id");
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
            int accountingAvailableForSale = result.getJSONObject(0).getInt("accounting_available_for_sale");
            if (physicalAvailableForSale < order.getQuantity()) {
                throw new Exception("Insufficient items");
            }
            float totalPrice = salesRate * order.getQuantity();
            totalOrderPrice += totalPrice;
            int aiID1 = Database.executeUpdate("insert into itemsales (item_id, quantity, rate_per_quantity, total, salesorder_id) values(?,?,?,?,?)", new Object[] {
                    order.getItemID(),
                    order.getQuantity(),
                    salesRate,
                    totalPrice,
                    salesOrderID
            });
            lineItems.put(Database.executeQuery("select item_id, quantity, rate_per_quantity, total from itemsales where ai_id = '" +
                    aiID1 + "'").getJSONArray("result").getJSONObject(0));
            if (salesOrder.getStatus().equals("open")) {
                Database.executeUpdate("update items set physical_committed_stock=?, physical_available_for_sale=?, accounting_committed_stock=?, accounting_available_for_sale=? where id = ?", new Object[] {
                        physicalCommittedStock + order.getQuantity(),
                        physicalAvailableForSale - order.getQuantity(),
                        accountingCommittedStock + order.getQuantity(),
                        accountingAvailableForSale - order.getQuantity(),
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

    public JSONObject getSalesOrder(int organizationID, int salesOrderID) throws SQLException, ClassNotFoundException {
        JSONObject json = new JSONObject();
        JSONObject salesOrderJSON = Database.executeQuery("select id, customer_id, organization_id, status, is_shipped, is_invoiced, total_price, sales_order_ref_number from salesorders where id = '" +
                 salesOrderID + "'" + " and organization_id = '" +
                 organizationID + "'");
        JSONArray resultArray = salesOrderJSON.getJSONArray("result");
        if (resultArray.length() == 0) {
            json.put("code", 110);
            json.put("message", "No salesorder found");
            return json;
        }
        JSONObject itemSalesJSON = Database.executeQuery("select id, quantity, rate_per_quantity, total, item_id from itemsales where salesorder_id = '" +
               salesOrderID + "'");
        JSONArray items = new JSONArray();
        JSONArray itemSalesArray = itemSalesJSON.getJSONArray("result");
        if (itemSalesArray.length() != 0) {
            for (int i = 0, n = itemSalesArray.length(); i < n; i++) {
                int itemID = itemSalesArray.getJSONObject(i).getInt("item_id");
                JSONObject item = Database.executeQuery("select id, name, unit, type from items where id = '" +
                       itemID + "'").getJSONArray("result").getJSONObject(0);
                item.put("quantity", itemSalesArray.getJSONObject(i).getInt("quantity"));
                item.put("rate_per_quantity", itemSalesArray.getJSONObject(i).getInt("rate_per_quantity"));
                item.put("total_price", itemSalesArray.getJSONObject(i).getInt("total"));
                items.put(item);
            }
            resultArray.getJSONObject(0).put("items", items);
        }
        json.put("code", 0);
        json.put("sales_order", resultArray.get(0));
        return json;
    }

    public JSONObject openSalesOrder(int organizationID, int salesOrderID) throws Exception {
        JSONObject resJSON = Database.executeQuery("select status from salesorders where id = '" +
                salesOrderID + "'" + " and organization_id = '" +
                organizationID + "'");
        JSONArray resArray = resJSON.getJSONArray("result");
        if (resArray.length() == 0) {
            throw new Exception("no sales order found");
        }
        String status = resArray.getJSONObject(0).getString("status");
        if (!status.equals("open")) {
            String newStatus = "open";
            JSONArray itemSalesArray = Database.executeQuery("select id, quantity, rate_per_quantity, total, item_id from itemsales where salesorder_id = '" +
                    salesOrderID + "'").getJSONArray("result");
            for (int i = 0, n = itemSalesArray.length(); i < n; i++) {
                int item_id = itemSalesArray.getJSONObject(i).getInt("item_id");
                int quantity = itemSalesArray.getJSONObject(i).getInt("quantity");
                JSONObject itemJSON = Database.executeQuery("select physical_stock_on_hand, physical_committed_stock, physical_available_for_sale, accounting_stock_on_hand, accounting_committed_stock, accounting_available_for_sale from items where id = '" +
                        item_id + "'");
                JSONArray result = itemJSON.getJSONArray("result");
                if (result.length() == 0) {
                    throw new Exception("Item doesn't exist");
                }
                int physicalAvailableForSale = result.getJSONObject(0).getInt("physical_available_for_sale");
                int physicalStockOnHand = result.getJSONObject(0).getInt("physical_stock_on_hand");
                int accountingStockOnHand = result.getJSONObject(0).getInt("accounting_stock_on_hand");
                int physicalCommittedStock = result.getJSONObject(0).getInt("physical_committed_stock");
                int accountingCommittedStock = result.getJSONObject(0).getInt("accounting_committed_stock");
                int accountingAvailableForSale = result.getJSONObject(0).getInt("accounting_available_for_sale");
                Database.executeUpdate("update items set physical_committed_stock=?, physical_available_for_sale=?, accounting_committed_stock=?, accounting_available_for_sale=? where id = ?", new Object[] {
                        physicalCommittedStock + quantity,
                        physicalAvailableForSale - quantity,
                        accountingCommittedStock + quantity,
                        accountingAvailableForSale - quantity,
                        item_id
                });
            }
            Database.executeUpdate("update salesorders set status=? where id=?", new Object[] {
                    newStatus,
                    salesOrderID
            });
        }
        return getSalesOrder(organizationID, salesOrderID);
    }

    public JSONObject invoiceSalesOrder(int organizationID, int salesOrderID) throws Exception {
        JSONObject resJSON = Database.executeQuery("select customer_id, total_price, is_invoiced, status from salesorders where id = '" +
               salesOrderID + "'" + " and organization_id = '" +
                organizationID + "'");
        JSONArray resArray = resJSON.getJSONArray("result");
        System.out.println(resArray);
        if (resArray.length() == 0) {
            throw new Exception("no sales order found");
        }
        String status = resArray.getJSONObject(0).getString("status");
        int customerID = resArray.getJSONObject(0).getInt("customer_id");
        float totalPrice = resArray.getJSONObject(0).getFloat("total_price");
        boolean isInvoiced = resArray.getJSONObject(0).getBoolean("is_invoiced");
        if (!isInvoiced) {
            if (!status.equals("open")) {
                openSalesOrder(organizationID, salesOrderID);
            }
            Database.executeUpdate("update salesorders set is_invoiced = ? where id = ?", new Object[] {
                    true,
                    salesOrderID
            });
            float receivables = Database.executeQuery("select receivables from customers where id = '" +
                    customerID + "'").getJSONArray("result").getJSONObject(0).getFloat("receivables");
            Database.executeUpdate("update customers set receivables = ? where id = ?", new Object[] {
                    totalPrice + receivables,
                    customerID
            });
            JSONArray itemSalesArray = Database.executeQuery("select id, quantity, rate_per_quantity, total, item_id from itemsales where salesorder_id = '" +
                    salesOrderID + "'").getJSONArray("result");
            for (int i = 0, n = itemSalesArray.length(); i < n; i++) {
                int item_id = itemSalesArray.getJSONObject(i).getInt("item_id");
                int quantity = itemSalesArray.getJSONObject(i).getInt("quantity");
                JSONObject itemJSON = Database.executeQuery("select accounting_stock_on_hand, accounting_committed_stock, accounting_available_for_sale from items where id = '" +
                        item_id + "'");
                JSONArray result = itemJSON.getJSONArray("result");
                if (result.length() == 0) {
                    throw new Exception("Item doesn't exist");
                }
                int accountingStockOnHand = result.getJSONObject(0).getInt("accounting_stock_on_hand");
                int accountingCommittedStock = result.getJSONObject(0).getInt("accounting_committed_stock");
                int accountingAvailableForSale = result.getJSONObject(0).getInt("accounting_available_for_sale");
                Database.executeUpdate("update items set accounting_stock_on_hand=?, accounting_committed_stock=? where id = ?", new Object[] {
                        accountingStockOnHand - quantity,
                        accountingCommittedStock - quantity,
                        item_id
                });
            }
        }
        return getSalesOrder(organizationID, salesOrderID);
    }

    public JSONObject shipSalesOrder(int organizationID, int salesOrderID) throws Exception {
        JSONObject resJSON = Database.executeQuery("select is_shipped, status from salesorders where id = '" +
                salesOrderID + "'" + " and organization_id = '" +
                organizationID + "'");
        JSONArray resArray = resJSON.getJSONArray("result");
        System.out.println(resArray);
        if (resArray.length() == 0) {
            JSONObject json = new JSONObject();
            json.put("code", 110);
            json.put("message", "No sales Order found");
            return json;
        }
        String status = resArray.getJSONObject(0).getString("status");
        boolean isShipped = resArray.getJSONObject(0).getBoolean("is_shipped");
        if (!isShipped) {
            if (!status.equals("open")) {
                JSONObject json = new JSONObject();
                json.put("code", 110);
                json.put("message", "Draft sales order");
                return json;
            }
            Database.executeUpdate("update salesorders set is_shipped = ? where id = ?", new Object[] {
                    true,
                    salesOrderID
            });
            JSONArray itemSalesArray = Database.executeQuery("select id, quantity, rate_per_quantity, total, item_id from itemsales where salesorder_id = '" +
                    salesOrderID + "'").getJSONArray("result");
            for (int i = 0, n = itemSalesArray.length(); i < n; i++) {
                int item_id = itemSalesArray.getJSONObject(i).getInt("item_id");
                int quantity = itemSalesArray.getJSONObject(i).getInt("quantity");
                JSONObject itemJSON = Database.executeQuery("select physical_stock_on_hand, physical_committed_stock from items where id = '" +
                        item_id + "'");
                JSONArray result = itemJSON.getJSONArray("result");
                if (result.length() == 0) {
                    throw new Exception("Item doesn't exist");
                }
                int physicalStockOnHand = result.getJSONObject(0).getInt("physical_stock_on_hand");
                int physicalCommittedStock = result.getJSONObject(0).getInt("physical_committed_stock");
                Database.executeUpdate("update items set physical_stock_on_hand=?, physical_committed_stock=? where id = ?", new Object[] {
                        physicalStockOnHand - quantity,
                        physicalCommittedStock - quantity,
                        item_id
                });
            }
        }
        return getSalesOrder(organizationID, salesOrderID);
    }
}
