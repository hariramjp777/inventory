package com.inventory.auth;

import com.inventory.model.database.Database;
import com.inventory.model.objects.user.User;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public class Authentication {
    public static int login(HttpServletRequest request, String email, String password) throws Exception {
        JSONObject json = Database.executeQuery("select id from users where email = '" + email +
                "' and password = '" + password +
                "'");
        JSONArray result = json.getJSONArray("result");
        if (result.length() > 1) {
            throw new Exception("Two Data Found");
        }
        if (result.length() == 1) {
            request.getSession().setAttribute("user", email);
            return 1;
        }
        return 0;
    }

    public static int logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("user", null);
        session.invalidate();
        return 1;
    }

    public static boolean isAuthenticated(HttpServletRequest request) {
        return request.getSession().getAttribute("user") != null;
    }

    public static User getUser(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        if (!isAuthenticated(request)) {
            return null;
        }
        JSONObject json = Database.executeQuery("select id, first_name, last_name, email, is_verified from users where email = '" + request.getSession().getAttribute("user") + "'");
        JSONObject userJSON = json.getJSONArray("result").getJSONObject(0);
        User user = new User(userJSON.getInt("id"), userJSON.getString("first_name"), userJSON.getString("last_name"), userJSON.getString("email"), userJSON.getBoolean("is_verified"));
        return user;
    }
}
