package com.inventory.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inventory.model.objects.item.ItemDAO;

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

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ItemDAO itemDAO = new ItemDAO();
        Gson gson = new Gson();
        Object sessionUserEmail = request.getSession().getAttribute("user");
        if (sessionUserEmail == null) {
            response.setContentType("application/json");
            response.setStatus(400);
            response.getWriter().println("{'code': '1000'}");
        }
        else {
            String itemName = request.getParameter("item_name");
            String itemUnit = request.getParameter("item_unit");
            String productType = request.getParameter("product_type");
            int stockOnHand = Integer.parseInt(request.getParameter("stock_on_hand"));
            float salesRate = Float.parseFloat(request.getParameter("sales_rate"));
            float purchaseRate = Float.parseFloat(request.getParameter("purchase_rate"));
            int organizationID = Integer.parseInt(request.getParameter("organization_id"));
            try {
                JsonObject json = itemDAO.createItem(itemName, itemUnit, productType, stockOnHand, salesRate, purchaseRate, organizationID);
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
