package config;

import java.util.ArrayList;
import java.util.List;

public class SecurityConfig {
    private static List<String> uriList;
    public static List<String> getUriList() {
        return uriList;
    }
    /**
     * 放行的接口
     * */
    static{
        uriList = new ArrayList<>();
        uriList.add("/page-login.jsp");
        uriList.add("/page-register.jsp");
    }
}