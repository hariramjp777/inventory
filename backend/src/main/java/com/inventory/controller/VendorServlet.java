package com.inventory.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inventory.model.objects.customer.CustomerDAO;
import com.inventory.model.objects.vendor.VendorDAO;

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

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VendorDAO vendorDAO = new VendorDAO();
        Gson gson = new Gson();
        Object sessionUserEmail = request.getSession().getAttribute("user");
        if (sessionUserEmail == null) {
            response.setContentType("application/json");
            response.setStatus(400);
            response.getWriter().println("{'code': '1000'}");
        }
        else {
            String contactName = request.getParameter("contact_name");
            String companyName = request.getParameter("company_name");
            String country = request.getParameter("country");
            int organizationID = Integer.parseInt(request.getParameter("organization_id"));
            try {
                JsonObject json = vendorDAO.createVendor(contactName, companyName, country, organizationID);
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                if (json.get("code").toString().equals("0")) {
                    response.setStatus(200);
                    out.println(json.toString());
                }
                else {
                    response.setStatus(404);
                    out.println(json.toString());
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
