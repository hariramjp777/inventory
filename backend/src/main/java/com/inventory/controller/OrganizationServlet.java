package com.inventory.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inventory.auth.Authentication;
import com.inventory.model.objects.organization.Organization;
import com.inventory.model.objects.organization.OrganizationDAO;
import com.inventory.model.objects.user.User;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/organization")
public class OrganizationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        if (!Authentication.isAuthenticated(request)) {
            response.setStatus(403);
            response.getWriter().println("{'code': '1000'}");
        }
        else {
            try {
                User user = Authentication.getUser(request);
                int organizationID = Integer.parseInt(request.getParameter("organization_id"));
                OrganizationDAO organizationDAO = new OrganizationDAO();
                JSONObject resultJSON = organizationDAO.getOrganization(user.getUserID(), organizationID);
                if (resultJSON.getInt("code") == 0) {
                    response.setStatus(200);
                    out.println(resultJSON.toString());
                }
                else {
                    response.setStatus(404);
                    out.println(resultJSON.toString());
                }
            } catch (SQLException | ClassNotFoundException e) {
                response.setStatus(500);
                out.println("{'code': '500', 'message': 'Internal Server Error'}");
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        if (!Authentication.isAuthenticated(request)) {
            response.setStatus(403);
            response.getWriter().println("{'code': '1000'}");
        }
        else {
            try {
                User user = Authentication.getUser(request);
                JSONObject orgJSON = new JSONObject(request.getParameter("json_string"));
                Organization organization = new Organization(user, orgJSON.getString("name"), orgJSON.getString("business_location"), orgJSON.getInt("fiscal_year_start_month"));
                OrganizationDAO organizationDAO = new OrganizationDAO();
                JSONObject resultJSON = organizationDAO.createOrganization(organization);
                if (resultJSON.getInt("code") == 0) {
                    response.setStatus(200);
                    out.println(resultJSON.toString());
                }
                else {
                    response.setStatus(404);
                    out.println(resultJSON.toString());
                }
            } catch (Exception e) {
                response.setStatus(500);
                out.println("{'code': '500', 'message': 'Internal Server Error'}");
                e.printStackTrace();
            }
        }
    }
}