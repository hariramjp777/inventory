package com.inventory.controller;

import com.inventory.auth.Authentication;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("get to login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        JSONObject userJSON = new JSONObject(request.getParameter("json_string"));
        JSONObject resultJSON = new JSONObject();
        String email = userJSON.getString("email");
        String password = userJSON.getString("password");
        try {
            int loginStatus = Authentication.login(request, email, password);
            if (loginStatus == 1) {
                response.setStatus(200);
                resultJSON.put("code", 0);
                resultJSON.put("message", "success");
                out.println(resultJSON.toString());
            }
            else {
                response.setStatus(400);
                resultJSON.put("code", 102);
                resultJSON.put("message", "Invalid Username or Password");
                out.println(resultJSON.toString());
            }
        } catch (Exception e) {
            response.setStatus(500);
            out.println("{'code': '500', 'message': 'Internal Server Error'}");
            e.printStackTrace();
        }
    }
}
