package niwaruka.controller.niwaruka.login;

import niwaruka.model.User;
import niwaruka.service.LoginService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class NewPasswordFormController extends Controller {

    public static final String PASSWORD_RENEWAL_SESSION_KEY = "passwordRenewal";
    LoginService ls = new LoginService();

    @Override
    public Navigation run() throws Exception {

        String uuid = requestScope("uuid");
        User user = ls.getUserByNewPasswordKey(uuid);
        if (user == null) {
            return redirect("newPasswordExpired");
        }

        sessionScope(PASSWORD_RENEWAL_SESSION_KEY, user);

        return forward("newPasswordForm.jsp");
    }
}
