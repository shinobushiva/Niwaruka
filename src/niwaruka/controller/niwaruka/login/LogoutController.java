package niwaruka.controller.niwaruka.login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class LogoutController extends Controller {

    @Override
    public Navigation run() throws Exception {

        HttpSession session = request.getSession();
        session.invalidate();

        Cookie cookie = new Cookie("keepLogin", null);
        cookie.setMaxAge(0);
        cookie.setPath("/niwaruka");
        response.addCookie(cookie);

        return forward(basePath);
    }

}