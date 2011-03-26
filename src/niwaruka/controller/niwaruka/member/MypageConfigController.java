package niwaruka.controller.niwaruka.member;

import niwaruka.controller.niwaruka.login.LoginController;
import niwaruka.controller.niwaruka.login.UserForm;
import niwaruka.model.User;
import niwaruka.service.LoginService;
import niwaruka.service.TwitterService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

/*
 * Profile(mypage)の編集を受け持つコントローラー
 */

public class MypageConfigController extends Controller {

    LoginService ls = new LoginService();
    TwitterService ts = new TwitterService();

    @Override
    public Navigation run() throws Exception {

        User user =
            ls.getUser(sessionScope(LoginController.USER_ID_SESSION_KEY));

        UserForm f = new UserForm();
        BeanUtil.copy(request, f); // formをコピー

        if (!validate()) {
            requestScope("user", f);
            return forward("mypage.jsp");
        }

        if (!f.getPasswd1().equals(f.getPasswd2())) {
            errors.put("passwordError", "パスワードが異なっています");
            requestScope("user", f);
            return forward("mypage.jsp");
        }

        String mad = f.getEmail();
        if (mad.length() > 0) {
            User userByEmail = ls.getUserByEmail(mad);
            if (userByEmail != null
                && !userByEmail.getName().equals(f.getName())) {
                errors.put("emailError", "入力したメールアドレス ["
                    + mad
                    + "]は既に使用されています。");
                requestScope("user", f);
                return forward("mypage.jsp");
            }
        }

        mad = f.getEmail2();
        if (mad.length() > 0) {
            User userByEmail2 = ls.getUserByEmail(mad);
            if (userByEmail2 != null
                && !userByEmail2.getName().equals(f.getName())) {
                errors.put("emailError", "入力したメールアドレス ["
                    + mad
                    + "]は既に使用されています。");
                requestScope("user", f);
                return forward("mypage.jsp");
            }
        }

        user.setEmail(f.getEmail());
        user.setEmail2(f.getEmail2());
        user.setPassword(f.getPasswd1());
        user.setUserName(f.getUserName());

        ls.saveUser(user);
        ts.fetchHashTags(ls, true);

        requestScope("message", "変更が保存されました");
        return forward("mypage");
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
