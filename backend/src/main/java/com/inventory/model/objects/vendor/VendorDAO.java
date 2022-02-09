package com.inventory.model.objects.vendor;

import com.inventory.model.database.Database;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.SQLException;

public class VendorDAO {
    public JSONObject createVendor(Vendor vendor) throws Exception {
        JSONObject json = new JSONObject();
        int aiID = Database.executeUpdate("insert into vendors (contact_name, contact_email, company_name, country, payables, unused_credits, organization_id) values (?,?,?,?,?,?,?)", new Object[] {
                vendor.getContactName(),
                vendor.getContactEmail(),
                vendor.getCompanyName(),
                vendor.getCountry(),
                vendor.getPayables(),
                vendor.getUnusedCredits(),
                vendor.getOrganizationID()
        });
        json.put("code", 0);
        json.put("message", "success");
        json.put("created_vendor", Database.executeQuery("select id, contact_name, contact_email, company_name, country, payables, unused_credits, organization_id, vendor_created_date from vendors where ai_id = '" +
                aiID + "'").getJSONArray("result").get(0));
        return json;
    }

    public JSONObject getVendor(int organizationID, int vendorID) throws SQLException, ClassNotFoundException {
        JSONObject json = new JSONObject();
        JSONObject vendorJSON = Database.executeQuery("select id, contact_name, contact_email, company_name, country, payables, unused_credits, organization_id, vendor_created_date from vendors where id = '" +
                vendorID + "'" + " and organization_id = '" +
                organizationID + "'");
        JSONArray resultArray = vendorJSON.getJSONArray("result");
        if (resultArray.length() == 0) {
            json.put("code", 105);
            json.put("message", "No vendor found");
            return json;
        }
        json.put("code", 0);
        json.put("message", "success");
        json.put("vendor", resultArray.getJSONObject(0));
        return json;
    }
}
