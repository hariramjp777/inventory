package com.inventory.controller;

import com.inventory.model.objects.user.UserDAO;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("get to register");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        JSONObject userJSON = new JSONObject(request.getParameter("json_string"));
        String firstName = userJSON.getString("first_name");
        String lastName = userJSON.getString("last_name");
        String email = userJSON.getString("email");
        String password = userJSON.getString("password");
        UserDAO userDAO = new UserDAO();
        try {
            JSONObject json = userDAO.createUser(firstName, lastName, email, password);
            if (json.getInt("code") == 0) {
                response.setStatus(200);
                out.println(json.toString());
            }
            else {
                response.setStatus(404);
                out.println(json.toString());
            }
        } catch (Exception e) {
            response.setStatus(500);
            out.println("{'code': '500', 'message': 'Internal Server Error'}");
            e.printStackTrace();
        }
    }
}
