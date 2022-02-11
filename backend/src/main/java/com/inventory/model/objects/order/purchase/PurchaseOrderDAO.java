package com.inventory.model.objects.order.purchase;

import com.inventory.model.database.Database;
import com.inventory.model.objects.order.Order;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;


public class PurchaseOrderDAO {
    public JSONObject createPurchaseOrder(PurchaseOrder purchaseOrder) throws Exception {
        JSONObject json = new JSONObject();
        int aiID = Database.executeUpdate("insert into purchaseorders (vendor_id, organization_id, status, is_received, is_billed, purchase_order_ref_number) values (?,?,?,?,?,?)", new Object[] {
                purchaseOrder.getVendorID(),
                purchaseOrder.getOrganizationID(),
                purchaseOrder.getStatus(),
                purchaseOrder.isReceived(),
                purchaseOrder.isBilled(),
                purchaseOrder.getPurchaseOrderRefNumber()
        });
        int purchaseOrderID = Database.executeQuery("select id from purchaseorders where ai_id = '" +
                aiID + "'").getJSONArray("result").getJSONObject(0).getInt("id");
        float totalOrderPrice = 0.0f;
        JSONArray lineItems = new JSONArray();
        for (Order order: purchaseOrder.getOrders()) {
            JSONObject resJSON = Database.executeQuery("select purchase_rate, physical_stock_on_hand, physical_committed_stock, physical_available_for_sale, accounting_stock_on_hand, accounting_committed_stock, accounting_available_for_sale from items where id = '" +
                    order.getItemID() + "'");
            JSONArray result = resJSON.getJSONArray("result");
            if (result.length() == 0) {
                throw new Exception("Item doesn't exist");
            }
            float purchaseRate = result.getJSONObject(0).getFloat("purchase_rate");
            int physicalAvailableForSale = result.getJSONObject(0).getInt("physical_available_for_sale");
            int physicalStockOnHand = result.getJSONObject(0).getInt("physical_stock_on_hand");
            int accountingStockOnHand = result.getJSONObject(0).getInt("accounting_stock_on_hand");
            int physicalCommittedStock = result.getJSONObject(0).getInt("physical_committed_stock");
            int accountingCommittedStock = result.getJSONObject(0).getInt("accounting_committed_stock");
            int accountingAvailableForSale = result.getJSONObject(0).getInt("accounting_available_for_sale");
            float totalPrice = purchaseRate * order.getQuantity();
            totalOrderPrice += totalPrice;
            int aiID1 = Database.executeUpdate("insert into itempurchases (item_id, quantity, rate_per_quantity, total, purchaseorder_id) values(?,?,?,?,?)", new Object[] {
                    order.getItemID(),
                    order.getQuantity(),
                    purchaseRate,
                    totalPrice,
                    purchaseOrderID
            });
            lineItems.put(Database.executeQuery("select item_id, quantity, rate_per_quantity, total from itempurchases where ai_id = '" +
                    aiID1 + "'").getJSONArray("result").getJSONObject(0));
            if (purchaseOrder.isReceived()) {
                Database.executeUpdate("update items set physical_stock_on_hand=?, physical_available_for_sale=? where id = ?", new Object[] {
                        physicalStockOnHand + order.getQuantity(),
                        physicalAvailableForSale + order.getQuantity(),
                        order.getItemID()
                });
            }
            if (purchaseOrder.isBilled()) {
                Database.executeUpdate("update items set accounting_stock_on_hand=?, accounting_available_for_sale=? where id = ?", new Object[] {
                        accountingStockOnHand + order.getQuantity(),
                        accountingAvailableForSale + order.getQuantity(),
                        order.getItemID()
                });
            }
        }
        Database.executeUpdate("update purchaseorders set total_price=? where ai_id=?", new Object[] {
                totalOrderPrice,
                aiID
        });
        JSONObject createdPurchaseOrderJSON = Database.executeQuery("select id, vendor_id, organization_id, status, is_billed, is_received, total_price, purchase_order_ref_number from purchaseorders where ai_id = '" +
                aiID + "'").getJSONArray("result").getJSONObject(0);
        createdPurchaseOrderJSON.put("lineItems", lineItems);
        json.put("code", 0);
        json.put("message", "success");
        json.put("created_purchase_order", createdPurchaseOrderJSON);
        return json;
    }

    public JSONObject getPurchaseOrder(int organizationID, int purchaseOrderID) throws SQLException, ClassNotFoundException {
        JSONObject json = new JSONObject();
        JSONObject purchaseOrderJSON = Database.executeQuery("select id, vendor_id, organization_id, status, is_billed, is_received, total_price, purchase_order_ref_number from purchaseorders where id = '" +
                purchaseOrderID + "'" + " and organization_id = '" +
                organizationID + "'");
        JSONArray resultArray = purchaseOrderJSON.getJSONArray("result");
        if (resultArray.length() == 0) {
            json.put("code", 110);
            json.put("message", "No purchase Order found");
            return json;
        }
        JSONObject itempurchasesJSON = Database.executeQuery("select id, quantity, rate_per_quantity, total, item_id from itempurchases where purchaseorder_id = '" +
                purchaseOrderID + "'");
        JSONArray items = new JSONArray();
        JSONArray itemPurchasesArray = itempurchasesJSON.getJSONArray("result");
        if (itemPurchasesArray.length() != 0) {
            for (int i = 0, n = itemPurchasesArray.length(); i < n; i++) {
                int itemID = itemPurchasesArray.getJSONObject(i).getInt("item_id");
                JSONObject item = Database.executeQuery("select id, name, unit, type from items where id = '" +
                        itemID + "'").getJSONArray("result").getJSONObject(0);
                item.put("quantity", itemPurchasesArray.getJSONObject(i).getInt("quantity"));
                item.put("rate_per_quantity", itemPurchasesArray.getJSONObject(i).getInt("rate_per_quantity"));
                item.put("total_price", itemPurchasesArray.getJSONObject(i).getInt("total"));
                items.put(item);
            }
            resultArray.getJSONObject(0).put("items", items);
        }
        json.put("code", 0);
        json.put("purchase_order", resultArray.get(0));
        return json;
    }

    public JSONObject openPurchaseOrder(int organizationID, int purchaseOrderID) throws Exception {
        JSONObject resJSON = Database.executeQuery("select status from purchaseorders where id = '" +
                purchaseOrderID + "'" + " and organization_id = '" +
                organizationID + "'");
        JSONArray resArray = resJSON.getJSONArray("result");
        if (resArray.length() == 0) {
            JSONObject json = new JSONObject();
            json.put("code", 110);
            json.put("message", "No purchase Order found");
            return json;
        }
        String status = resArray.getJSONObject(0).getString("status");
        if (!status.equals("open")) {
            String newStatus = "open";
            Database.executeUpdate("update purchaseorders set status=? where id=?", new Object[] {
                    newStatus,
                    purchaseOrderID
            });
        }
        return getPurchaseOrder(organizationID, purchaseOrderID);
    }

    public JSONObject billPurchaseOrder(int organizationID, int purchaseOrderID) throws Exception {
        JSONObject resJSON = Database.executeQuery("select vendor_id, total_price, is_billed, status from purchaseorders where id = '" +
                purchaseOrderID + "'" + " and organization_id = '" +
                organizationID + "'");
        JSONArray resArray = resJSON.getJSONArray("result");
        if (resArray.length() == 0) {
            JSONObject json = new JSONObject();
            json.put("code", 110);
            json.put("message", "No purchase Order found");
            return json;
        }
        int vendorID = resArray.getJSONObject(0).getInt("vendor_id");
        float totalPrice = resArray.getJSONObject(0).getFloat("total_price");
        boolean isBilled = resArray.getJSONObject(0).getBoolean("is_billed");
        if (!isBilled) {
            Database.executeUpdate("update purchaseorders set status = ?, is_billed = ? where id = ?", new Object[] {
                    "open",
                    true,
                    purchaseOrderID
            });
            float payables = Database.executeQuery("select payables from vendors where id = '" +
                    vendorID + "'").getJSONArray("result").getJSONObject(0).getFloat("payables");
            Database.executeUpdate("update vendors set payables = ? where id = ?", new Object[] {
                    totalPrice + payables,
                    vendorID
            });
            JSONArray itemPurchaseArray = Database.executeQuery("select id, quantity, rate_per_quantity, total, item_id from itempurchases where purchaseorder_id = '" +
                    purchaseOrderID + "'").getJSONArray("result");
            for (int i = 0, n = itemPurchaseArray.length(); i < n; i++) {
                int item_id = itemPurchaseArray.getJSONObject(i).getInt("item_id");
                int quantity = itemPurchaseArray.getJSONObject(i).getInt("quantity");
                JSONObject itemJSON = Database.executeQuery("select accounting_stock_on_hand, accounting_available_for_sale from items where id = '" +
                        item_id + "'");
                JSONArray result = itemJSON.getJSONArray("result");
                if (result.length() == 0) {
                    throw new Exception("Item doesn't exist");
                }
                int accountingStockOnHand = result.getJSONObject(0).getInt("accounting_stock_on_hand");
                int accountingAvailableForSale = result.getJSONObject(0).getInt("accounting_available_for_sale");
                Database.executeUpdate("update items set accounting_stock_on_hand=?, accounting_available_for_sale=? where id = ?", new Object[] {
                        accountingStockOnHand + quantity,
                        accountingAvailableForSale + quantity,
                        item_id
                });
            }
        }
        return getPurchaseOrder(organizationID, purchaseOrderID);
    }

    public JSONObject receivePurchaseOrder(int organizationID, int purchaseOrderID) throws Exception {
        JSONObject resJSON = Database.executeQuery("select is_received, status from purchaseorders where id = '" +
                purchaseOrderID + "'" + " and organization_id = '" +
                organizationID + "'");
        JSONArray resArray = resJSON.getJSONArray("result");
        if (resArray.length() == 0) {
            JSONObject json = new JSONObject();
            json.put("code", 110);
            json.put("message", "No purchase Order found");
            return json;
        }
        boolean isReceived = resArray.getJSONObject(0).getBoolean("is_received");
        if (!isReceived) {
            Database.executeUpdate("update purchaseorders set status = ?, is_received = ? where id = ?", new Object[] {
                    "open",
                    true,
                    purchaseOrderID
            });
            JSONArray itemPurchaseArray = Database.executeQuery("select id, quantity, rate_per_quantity, total, item_id from itempurchases where purchaseorder_id = '" +
                    purchaseOrderID + "'").getJSONArray("result");
            for (int i = 0, n = itemPurchaseArray.length(); i < n; i++) {
                int item_id = itemPurchaseArray.getJSONObject(i).getInt("item_id");
                int quantity = itemPurchaseArray.getJSONObject(i).getInt("quantity");
                JSONObject itemJSON = Database.executeQuery("select physical_stock_on_hand, physical_available_for_sale from items where id = '" +
                        item_id + "'");
                JSONArray result = itemJSON.getJSONArray("result");
                if (result.length() == 0) {
                    throw new Exception("Item doesn't exist");
                }
                int physicalStockOnHand = result.getJSONObject(0).getInt("physical_stock_on_hand");
                int physicalAvailableForSale = result.getJSONObject(0).getInt("physical_available_for_sale");
                Database.executeUpdate("update items set physical_stock_on_hand=?, physical_available_for_sale=? where id = ?", new Object[] {
                        physicalStockOnHand + quantity,
                        physicalAvailableForSale + quantity,
                        item_id
                });
            }
        }
        return getPurchaseOrder(organizationID, purchaseOrderID);
    }
}
