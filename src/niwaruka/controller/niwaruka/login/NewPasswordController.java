package niwaruka.controller.niwaruka.login;

import niwaruka.model.User;
import niwaruka.service.LoginService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

public class NewPasswordController extends Controller {

    LoginService ls = new LoginService();

    @Override
    public Navigation run() throws Exception {

        User user =
            sessionScope(NewPasswordFormController.PASSWORD_RENEWAL_SESSION_KEY);
        if (user == null) {
            return redirect("newPasswordExpired");
        }

        if (!validate()) {
            return forward("newPasswordForm.jsp");
        }

        if (!requestScope("passwd1").equals(requestScope("passwd2"))) {
            errors.put("passwordError", "パスワードが一致しません");
            return forward("newPasswordForm.jsp");
        }

        user.setPassword((String) requestScope("passwd1"));
        user.setNewPasswordKey(null);
        ls.save(user);

        return forward("newPassword.jsp");
    }

    private boolean validate() {
        Validators v = new Validators(request);
        v.add(
            "passwd1",
            v.required(),
            v.regexp("^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]+$"));
        v.add(
            "passwd2",
            v.required(),
            v.regexp("^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]+$"));

        return v.validate();

    }
}
