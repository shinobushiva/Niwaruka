package niwaruka.controller.niwaruka.login;

import java.util.UUID;

import javax.servlet.http.Cookie;

import niwaruka.model.User;
import niwaruka.service.LoginService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;
import org.slim3.util.BeanUtil;

/*
 * ログインクラス。sessionを生成してisLoginとuser.nameをセットしてます。
 */

public class LoginController extends Controller {

    private LoginService loginService = new LoginService();

    // public static final String USER_MODEL_SESSION_KEY = "userModel";
    public static final String USER_ID_SESSION_KEY = "user";

    @Override
    public Navigation run() throws Exception {
        LoginForm form = new LoginForm();
        BeanUtil.copy(request, form);

        if (loginService.authorize(form.getName(), form.getPasswd())) {
            sessionScope(USER_ID_SESSION_KEY, form.getName());

            User user = loginService.getUser(form.getName());
            // sessionScope(USER_MODEL_SESSION_KEY, user);

            if (form.isKeepLogin()) {
                String cookieString = UUID.randomUUID().toString();
                Cookie cookie = new Cookie("keepLogin", cookieString);
                cookie.setMaxAge(7 * 24 * 60 * 60);
                cookie.setPath("/niwaruka");
                // System.out.println(cookie.getName() + ":" +
                // cookie.getValue());
                user.setCookieString(cookieString);

                GlobalTransaction gtx = Datastore.beginGlobalTransaction();
                Datastore.put(user);
                gtx.commit();

                response.addCookie(cookie);
            }
        }

        return forward("index");
    }

    public class LoginForm {
        private String name;
        private String passwd;
        private boolean keepLogin;

        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }

        public String getPasswd() {
            return passwd;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setKeepLogin(boolean keepLogin) {
            this.keepLogin = keepLogin;
        }

        public boolean isKeepLogin() {
            return keepLogin;
        }
    }
}
