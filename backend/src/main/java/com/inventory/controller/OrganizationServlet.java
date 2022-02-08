package com.inventory.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inventory.model.objects.organization.OrganizationDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/organization")
public class OrganizationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrganizationDAO organizationDAO = new OrganizationDAO();
        Gson gson = new Gson();
        Object sessionUserEmail = request.getSession().getAttribute("user");
        if (sessionUserEmail == null) {
            response.setContentType("application/json");
            response.setStatus(403);
            response.getWriter().println("{'code': '1000'}");
        }
        if (sessionUserEmail != null) {
            String organizationName = request.getParameter("organization_name");
            String industryType = request.getParameter("industry_type");
            String country = request.getParameter("country");
            try {
                JsonObject json = organizationDAO.createOrganization(organizationName, industryType, country, sessionUserEmail.toString());
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
