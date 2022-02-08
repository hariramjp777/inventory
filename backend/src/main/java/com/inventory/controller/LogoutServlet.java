package com.inventory.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().setAttribute("user", null);
        request.getSession().invalidate();
        JsonObject json = new Gson().toJsonTree(new Object()).getAsJsonObject();
        json.addProperty("code", 0);
        json.addProperty("message", "You've been logged out");
        response.setContentType("application/json");
        response.setStatus(200);
        response.getWriter().println(json.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
