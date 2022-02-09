package com.inventory.model.objects.organization;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inventory.model.database.Database;
import com.inventory.model.objects.user.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class OrganizationDAO {
    public JSONObject createOrganization(Organization organization) throws Exception {
        JSONObject json = new JSONObject();
        int aiId = Database.executeUpdate("insert into organizations (user_id, name, industry, business_location, is_org_active, logo_url, fiscal_year_start_month)" +
                "values (?,?,?,?,?,?,?)", new Object[]{
                        organization.getUser().getUserID(),
                        organization.getName(),
                        organization.getIndustry(),
                        organization.getBusinessLocation(),
                        organization.isOrganizationActive(),
                        organization.getLogoURL(),
                        organization.getFiscalYearStartMonth()
        });
        json.put("code", 0);
        json.put("message", "success");
        json.put("created_organization", Database.executeQuery("select id, name, industry, business_location, org_created_date, is_org_active, logo_url, fiscal_year_start_month from organizations where ai_id = '" +
                aiId + "'").getJSONArray("result").getJSONObject(0));
        return json;
    }

    public JSONObject getOrganization(int userID, int organizationID) throws SQLException, ClassNotFoundException {
        JSONObject json = new JSONObject();
        JSONObject orgJSON = Database.executeQuery("select id, name, industry, business_location, org_created_date, is_org_active, logo_url, fiscal_year_start_month from organizations where id = '" +
                organizationID + "'" + " and user_id = '" +
                userID + "'");
        JSONArray resultArray = orgJSON.getJSONArray("result");
        if (resultArray.length() == 0) {
            json.put("code", 103);
            json.put("message", "No organization found");
            return json;
        }
        json.put("code", 0);
        json.put("message", "success");
        json.put("organization", resultArray.getJSONObject(0));
        return json;
    }
}

