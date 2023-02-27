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

@WebServlet("/UserLogin")
public class UserLogin extends HttpServlet {
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
            int count;
            count = UserDao.LoginConfirm(user);
            if(count == 0)
            {
                out.print("<script language='javascript'>alert('The email address or password is incorrect, please re-enter it');window.location.href='page-login.html'</script>");
            } else{
                out.print("<script language='javascript'>alert('Login successful');window.location.href='index.jsp'</script>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
