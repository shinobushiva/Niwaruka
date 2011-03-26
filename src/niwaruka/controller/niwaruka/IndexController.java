package niwaruka.controller.niwaruka;

import javax.servlet.http.Cookie;

import niwaruka.controller.niwaruka.login.LoginController;
import niwaruka.model.User;
import niwaruka.service.LoginService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

/*
 * ログイン画面
 */
public class IndexController extends Controller {

    LoginService ls = new LoginService();

    @Override
    public Navigation run() throws Exception {

        if (sessionScope(LoginController.USER_ID_SESSION_KEY) != null) {
            return forward("/niwaruka/member");
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("keepLogin".equals(cookie.getName())) {
                    String value = cookie.getValue();
                    User user = ls.getUserByCookieString(value);

                    sessionScope("login", "isLogin");
                    sessionScope("user", user.getName());

                    return forward("/niwaruka/member");
                }
            }
        }

        return forward("index.jsp");
    }
}
