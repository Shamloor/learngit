package dao;

import model.User;
import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public static void Register(User user) {
        //连接数据库
        Connection conn = DatabaseUtil.getConnection();

        //创建PreparedStatement对象
        String sql = "INSERT INTO USER values(?, ?, ?);";
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            String email = user.getEmail();
            String name = user.getUsername();
            String pwd = user.getPassword();
            System.out.println(email + " " + name + " " + pwd);
            ps.setString(1, email);
            ps.setString(2, name);
            ps.setString(3, pwd);

            //执行语句
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static int RegisterConfirm(User user) {
        //连接数据库
        Connection conn = DatabaseUtil.getConnection();

        //创建PreparedStatement对象
        String sql = "SELECT * FROM user " + "WHERE email=? ";
        PreparedStatement ps = null;
        int a = 0;//返回值
        try {
            //查询是否存在账号
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getEmail());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                a++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DatabaseUtil.closeConnection(conn);
        return a;
    }

    public static int LoginConfirm(User user) {
        //连接数据库
        Connection conn = DatabaseUtil.getConnection();

        //创建PreparedStatement对象
        String sql = "SELECT * FROM user " + "WHERE email=? and password=?";
        PreparedStatement ps = null;
        int i = 0;//返回值
        try {
            //查询是否存在账号
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DatabaseUtil.closeConnection(conn);
        return i;
    }

    public static void UserForget(User user) {
        //连接数据库
        Connection conn = DatabaseUtil.getConnection();

        //创建PreparedStatement对象
        String sql = "UPDATE user SET password= ? WHERE email = ?";
        PreparedStatement ps = null;
        int i = 0;//返回值
        try {
            //查询是否存在账号
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getEmail());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DatabaseUtil.closeConnection(conn);
    }
}