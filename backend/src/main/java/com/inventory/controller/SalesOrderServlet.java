package com.inventory.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inventory.auth.Authentication;
import com.inventory.model.objects.order.Order;
import com.inventory.model.objects.order.sales.SalesOrder;
import com.inventory.model.objects.order.sales.SalesOrderDAO;
import com.inventory.model.objects.user.User;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/salesorder")
public class SalesOrderServlet extends HttpServlet {
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
                int salesOrderID = Integer.parseInt(request.getParameter("salesorder_id"));
                SalesOrderDAO salesOrderDAO = new SalesOrderDAO();
                JSONObject resultJSON = salesOrderDAO.getSalesOrder(organizationID, salesOrderID);
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
                int customerID = Integer.parseInt(request.getParameter("customer_id"));
                JSONObject salesOrderJSON = new JSONObject(request.getParameter("json_string"));
                JSONArray lineItems = salesOrderJSON.getJSONArray("line_items");
                ArrayList<Order> orders = new ArrayList<Order>();
                for (int i = 0, n = lineItems.length(); i < n; i++) {
                    JSONObject lineItem = lineItems.getJSONObject(i);
                    orders.add(new Order(lineItem.getInt("item_id"), lineItem.getInt("quantity")));
                }
                SalesOrder salesOrder = new SalesOrder(customerID, organizationID, salesOrderJSON.getString("status"), orders, salesOrderJSON.getString("sales_order_ref_number"));
                SalesOrderDAO salesOrderDAO = new SalesOrderDAO();
                JSONObject resultJSON = salesOrderDAO.createSalesOrder(salesOrder);
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
