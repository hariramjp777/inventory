package com.inventory.controller;

import com.inventory.auth.Authentication;
import com.inventory.model.objects.order.Order;
import com.inventory.model.objects.order.purchase.PurchaseOrder;
import com.inventory.model.objects.order.purchase.PurchaseOrderDAO;
import com.inventory.model.objects.user.User;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/purchaseorder")
public class PurchaseOrderServlet extends HttpServlet {
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
                int purchaseOrderID = Integer.parseInt(request.getParameter("purchaseorder_id"));
                PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();
                JSONObject resultJSON = purchaseOrderDAO.getPurchaseOrder(organizationID, purchaseOrderID);
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
                int vendorID = Integer.parseInt(request.getParameter("vendor_id"));
                JSONObject purchaseOrderJSON = new JSONObject(request.getParameter("json_string"));
                JSONArray lineItems = purchaseOrderJSON.getJSONArray("line_items");
                ArrayList<Order> orders = new ArrayList<Order>();
                for (int i = 0, n = lineItems.length(); i < n; i++) {
                    JSONObject lineItem = lineItems.getJSONObject(i);
                    orders.add(new Order(lineItem.getInt("item_id"), lineItem.getInt("quantity")));
                }
                PurchaseOrder purchaseOrder = new PurchaseOrder(vendorID, organizationID, purchaseOrderJSON.getString("status"), orders, purchaseOrderJSON.getString("purchase_order_ref_number"));
                PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();
                JSONObject resultJSON = purchaseOrderDAO.createPurchaseOrder(purchaseOrder);
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
