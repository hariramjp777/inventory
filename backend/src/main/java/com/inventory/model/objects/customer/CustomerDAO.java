package com.inventory.model.objects.customer;

import com.inventory.model.database.Database;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;

public class CustomerDAO {
    public JSONObject createCustomer(Customer customer) throws Exception {
        JSONObject json = new JSONObject();
        int aiID = Database.executeUpdate("insert into customers (organization_id, contact_name, contact_email, type, country, receivables, unused_credits) values (?,?,?,?,?,?,?)", new Object[] {
                customer.getOrganizationID(),
                customer.getContactName(),
                customer.getContactEmail(),
                customer.getType(),
                customer.getCountry(),
                customer.getReceivables(),
                customer.getUnusedCredits()
        });
        json.put("code", 0);
        json.put("message", "success");
        json.put("created_customer", Database.executeQuery("select id, contact_name, contact_email, type, country, receivables, unused_credits, organization_id from customers where ai_id = '" +
                aiID + "'").getJSONArray("result").getJSONObject(0));
        return json;
    }

    public JSONObject getCustomer(int organizationID, int customerID) throws SQLException, ClassNotFoundException {
        JSONObject json = new JSONObject();
        JSONObject customerJSON = Database.executeQuery("select id, contact_name, contact_email, type, country, receivables, unused_credits, organization_id from customers where id = '" +
                customerID + "'" + " and organization_id = '" +
                organizationID + "'");
        JSONArray resultArray = customerJSON.getJSONArray("result");
        if (resultArray.length() == 0) {
            json.put("code", 104);
            json.put("message", "No customer found");
            return json;
        }
        json.put("code", 0);
        json.put("message", "success");
        json.put("customer", resultArray.getJSONObject(0));
        return json;
    }
}
