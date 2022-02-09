package com.inventory.controller;

import com.inventory.auth.Authentication;
import com.inventory.model.objects.user.User;
import com.inventory.model.objects.vendor.Vendor;
import com.inventory.model.objects.vendor.VendorDAO;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/vendor")
public class VendorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        if (!Authentication.isAuthenticated(request)) {
            response.setStatus(400);
            response.getWriter().println("{'code': '1000'}");
        }
        else {
            try {
                User user = Authentication.getUser(request);
                int organizationID = Integer.parseInt(request.getParameter("organization_id"));
                int vendorID = Integer.parseInt(request.getParameter("vendor_id"));
                VendorDAO vendorDAO = new VendorDAO();
                JSONObject resultJSON = vendorDAO.getVendor(organizationID, vendorID);
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
        } else {
            try {
                User user = Authentication.getUser(request);
                int organizationID = Integer.parseInt(request.getParameter("organization_id"));
                JSONObject vendorJSON = new JSONObject(request.getParameter("json_string"));
                VendorDAO vendorDAO = new VendorDAO();
                JSONObject resultJSON = vendorDAO.createVendor(new Vendor(vendorJSON.getString("contact_name"), vendorJSON.getString("contact_email"), vendorJSON.getString("company_name"), vendorJSON.getString("country"), organizationID));
                if (resultJSON.getInt("code") == 0) {
                    response.setStatus(200);
                    out.println(resultJSON.toString());
                } else {
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
