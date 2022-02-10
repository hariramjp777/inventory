package com.inventory.model.objects.item;

import com.inventory.model.database.Database;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.*;

public class ItemDAO {
    public JSONObject createItem(Item item) throws Exception {
        JSONObject json = new JSONObject();
        int aiID = Database.executeUpdate("insert into items (organization_id, name, unit, type, sales_rate, purchase_rate, physical_stock_on_hand, physical_committed_stock, physical_available_for_sale, accounting_stock_on_hand, accounting_committed_stock, accounting_available_for_sale) values (?,?,?,?,?,?,?,?,?,?,?,?)", new Object[] {
                item.getOrganizationID(),
                item.getName(),
                item.getUnit(),
                item.getType(),
                item.getSalesRate(),
                item.getPurchaseRate(),
                item.getPhysicalStockOnHand(),
                item.getPhysicalCommittedStock(),
                item.getPhysicalAvailableForSale(),
                item.getAccountingStockOnHand(),
                item.getAccountingCommittedStock(),
                item.getAccountingAvailableForSale()
        });
        json.put("code", 0);
        json.put("message", "success");
        json.put("created_item", Database.executeQuery("select id, name, unit, type, sales_rate, purchase_rate, physical_stock_on_hand, physical_committed_stock, physical_available_for_sale, accounting_stock_on_hand, accounting_committed_stock, accounting_available_for_sale, organization_id item_created_date from items where ai_id = '" +
                aiID + "'").getJSONArray("result").get(0));
        return json;
    }

    public JSONObject getItem(int organizationID, int itemID) throws SQLException, ClassNotFoundException {
        JSONObject json = new JSONObject();
        JSONObject itemJSON = Database.executeQuery("select id, name, unit, type, sales_rate, purchase_rate, physical_stock_on_hand, physical_committed_stock, physical_available_for_sale, accounting_stock_on_hand, accounting_committed_stock, accounting_available_for_sale, organization_id, item_created_date from items where id = '" +
                itemID + "'" + " and organization_id = '" +
                organizationID + "'");
        JSONArray resultArray = itemJSON.getJSONArray("result");
        if (resultArray.length() == 0) {
            json.put("code", 105);
            json.put("message", "No item found");
            return json;
        }
        json.put("code", 0);
        json.put("message", "success");
        json.put("item", resultArray.getJSONObject(0));
        return json;
    }
}
