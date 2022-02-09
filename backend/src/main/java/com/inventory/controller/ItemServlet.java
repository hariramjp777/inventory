package com.inventory.controller;

import com.inventory.auth.Authentication;
import com.inventory.model.objects.item.Item;
import com.inventory.model.objects.item.ItemDAO;
import com.inventory.model.objects.user.User;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/item")
public class ItemServlet extends HttpServlet {
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
                int itemID = Integer.parseInt(request.getParameter("item_id"));
                ItemDAO itemDAO = new ItemDAO();
                JSONObject resultJSON = itemDAO.getItem(organizationID, itemID);
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
            response.setStatus(400);
            response.getWriter().println("{'code': '1000'}");
        }
        else {
            try {
                User user = Authentication.getUser(request);
                int organizationID = Integer.parseInt(request.getParameter("organization_id"));
                JSONObject itemJSON = new JSONObject(request.getParameter("json_string"));
                ItemDAO itemDAO = new ItemDAO();
                Item item = new Item(itemJSON.getString("name"), itemJSON.getFloat("sales_rate"), itemJSON.getFloat("purchase_rate"), organizationID);
                item.setPhysicalStockOnHand(itemJSON.getInt("opening_stock"));
                item.setAccountingStockOnHand(itemJSON.getInt("opening_stock"));
                item.setAccountingAvailableForSale(itemJSON.getInt("opening_stock"));
                item.setPhysicalAvailableForSale(itemJSON.getInt("opening_stock"));
                JSONObject resultJSON = itemDAO.createItem(item);
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
