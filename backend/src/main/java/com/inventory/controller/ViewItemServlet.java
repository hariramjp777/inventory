package com.inventory.controller;

import com.google.gson.JsonObject;
import com.inventory.model.objects.item.ItemDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/viewItem")
public class ViewItemServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int organizationID = Integer.parseInt(request.getParameter("organization_id"));
        int itemID = Integer.parseInt(request.getParameter("item_id"));
        ItemDAO itemDAO = new ItemDAO();
        try {
            JsonObject json = itemDAO.viewItem(organizationID, itemID);
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
