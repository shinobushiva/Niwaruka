package niwaruka.controller.niwaruka.login;

import niwaruka.service.LoginService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

public class UserRegistrationConfirmController extends Controller {

    LoginService ls = new LoginService();

    @Override
    public Navigation run() throws Exception {

        UserForm f = new UserForm();
        BeanUtil.copy(request, f);
        requestScope("u", f);

        if (!validate()) {
            return forward("userRegistrationForm.jsp");
        }

        if (!f.getPasswd1().equals(f.getPasswd2())) {
            errors.put("passwordError", "パスワードが一致しません");
            return forward("userRegistrationForm.jsp");

        }

        if (ls.getUser(f.getName()) != null) {
            errors.put("idError", "入力したにわるかID ["
                + f.getName()
                + "]は既に使用されています。");
            return forward("userRegistrationForm.jsp");
        }
        if (ls.getUserByEmail(f.getEmail()) != null) {
            errors.put("emailError", "入力したメールアドレス ["
                + f.getEmail()
                + "]は既に使用されています。");
            return forward("userRegistrationForm.jsp");
        }

        sessionScope("registratingUser", f);
        return forward("userRegistrationConfirm.jsp");
    }

    private boolean validate() {
        Validators v = new Validators(request);
        v.add(
            "name",
            v.required(),
            v.regexp("^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]+$"));
        v.add(
            "passwd1",
            v.required(),
            v.regexp("^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]+$"));
        v.add(
            "passwd2",
            v.required(),
            v.regexp("^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]+$"));
        v.add("userName", v.required());
        v
            .add(
                "email",
                v.required(),
                v
                    .regexp("^[a-zA-Z0-9!$&*.=^`|~#%'+\\/?_{}-]+@([a-zA-Z0-9_-]+\\.)+[a-zA-Z]{2,4}$"));

        return v.validate();

    }

}
