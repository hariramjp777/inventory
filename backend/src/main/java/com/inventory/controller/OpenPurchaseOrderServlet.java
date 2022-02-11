package com.inventory.controller;

import com.inventory.auth.Authentication;
import com.inventory.model.objects.order.purchase.PurchaseOrderDAO;
import com.inventory.model.objects.order.sales.SalesOrderDAO;
import com.inventory.model.objects.user.User;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/open-purchase-order")
public class OpenPurchaseOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                JSONObject resultJSON = purchaseOrderDAO.openPurchaseOrder(organizationID, purchaseOrderID);
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
