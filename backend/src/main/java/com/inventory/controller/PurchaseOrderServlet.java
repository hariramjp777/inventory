package com.inventory.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inventory.model.objects.order.Order;
import com.inventory.model.objects.order.purchase.PurchaseOrderDAO;
import com.inventory.model.objects.order.sales.SalesOrderDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/purchaseorder")
public class PurchaseOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object sessionUserEmail = request.getSession().getAttribute("user");
        if (sessionUserEmail == null) {
            response.setContentType("application/json");
            response.setStatus(400);
            response.getWriter().println("{'code': '1000'}");
        }
        else {
            int organizationID = Integer.parseInt(request.getParameter("organization_id"));
            int vendorID = Integer.parseInt(request.getParameter("vendor_id"));
            String purchaseOrderRefNumber = request.getParameter("purchaseorder_ref_number");
            String lineItems = request.getParameter("line_items");
            System.out.println(lineItems);
            Gson gson = new Gson();
            Order[] orders = gson.fromJson(lineItems, Order[].class);
            for (Order order : orders) {
                System.out.println(order.itemID + " " + order.quantity);
            }
            PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();
            try {
                JsonObject json = purchaseOrderDAO.placePurchaseOrder(organizationID, vendorID, purchaseOrderRefNumber, orders);
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
