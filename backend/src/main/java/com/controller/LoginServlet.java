package com.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.model.objects.user.User;
import com.model.objects.user.UserDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        System.out.println("get to login..");
//        System.out.println(request.getSession().getAttribute("user"));
//        String jsonString = "{'user': 'hariramjp777'}";
//        JsonObject data = new Gson().fromJson(jsonString, JsonObject.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDAO userDAO = new UserDAO();
        String emailID = request.getParameter("email_id");
        String password = request.getParameter("password");
        try {
            JsonObject json = userDAO.validateUser(emailID, password);
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            if (json.get("code").toString().equals("0")) {
                response.setStatus(200);
                System.out.println(emailID);
                request.getSession().setAttribute("user", emailID);
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
