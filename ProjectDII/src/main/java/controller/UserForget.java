package controller;


import dao.UserDao;
import model.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/UserForget")
public class UserForget extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDao userDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        userDao = new UserDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        resp.setContentType("text/html");
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out= resp.getWriter();
        try {
            UserDao.UserForget(user);
            out.print("<script language='javascript'>alert('Password changes successfully');window.location.href='page-login.html'</script>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
