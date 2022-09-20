package nix.servlet;

import nix.model.User;
import nix.service.Storage;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@WebServlet(name = "UserServlet", value = "/UserServlet")
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final PrintWriter responseBody = response.getWriter();
        response.setContentType("text/html");

        final User user = new User(request.getRemoteAddr(), request.getHeader("User-Agent"), LocalDateTime.now());
        Storage.addUser(user);

        responseBody.println("<h1 align=\"center\">The resulting list of users</h1>");
        for (User u : Storage.getUsers()) {
            if (user.equals(u)) {
                responseBody.println("<p align=\"center\"><b>" + u + "</b></p>");
            } else {
                responseBody.println("<p align=\"center\">" + u + "</p>");
            }
        }
    }
}