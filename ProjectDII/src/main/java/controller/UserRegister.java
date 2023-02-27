package controller;

import dao.UserDao;
import model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/UserRegister")
public class UserRegister extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDao userDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        userDao = new UserDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        resp.setContentType("text/html");
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out= resp.getWriter();
        try {
            int count;
            count=UserDao.RegisterConfirm(user);
            if(count == 0)
            {
                UserDao.Register(user);
                out.print("<script language='javascript'>alert('Register successful');window.location.href='page-login.html'</script>");
            } else{
                out.print("<script language='javascript'>alert('The email address has already been registered, please re-enter it');window.location.href='page-register.html'</script>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}