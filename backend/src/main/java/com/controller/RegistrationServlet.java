package com.controller;

import com.google.gson.JsonObject;
import com.model.objects.user.UserDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("get to register..");
        response.getWriter().println("get to register..");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDAO userDAO = new UserDAO();
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String emailID = request.getParameter("email_id");
        String password = request.getParameter("password");
        try {
            JsonObject json = userDAO.createUser(firstName, lastName, emailID, password);
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
