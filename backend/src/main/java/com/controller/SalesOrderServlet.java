package com.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.model.objects.order.Order;
import com.model.objects.order.sales.SalesOrderDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/salesorder")
public class SalesOrderServlet extends HttpServlet {
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
            int customerID = Integer.parseInt(request.getParameter("customer_id"));
            String salesOrderRefNumber = request.getParameter("salesorder_ref_number");
            String lineItems = request.getParameter("line_items");
            System.out.println(lineItems);
            Gson gson = new Gson();
            Order[] orders = gson.fromJson(lineItems, Order[].class);
            for (Order order : orders) {
                System.out.println(order.itemID + " " + order.quantity);
            }
            SalesOrderDAO salesOrderDAO = new SalesOrderDAO();
            try {
                JsonObject json = salesOrderDAO.placeSalesOrder(organizationID, customerID, salesOrderRefNumber, orders);
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
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
