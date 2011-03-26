package niwaruka.controller.niwaruka.member;

import java.util.List;

import niwaruka.controller.niwaruka.login.LoginController;
import niwaruka.controller.niwaruka.login.UserForm;
import niwaruka.model.Tag;
import niwaruka.model.TwitterToken;
import niwaruka.model.UploadedData;
import niwaruka.model.User;
import niwaruka.service.LoginService;
import niwaruka.service.TagService;
import niwaruka.service.TwitterService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

public class MypageController extends Controller {

    TagService tagService = new TagService();
    TwitterService ts = new TwitterService();
    LoginService ls = new LoginService();

    @Override
    public Navigation run() throws Exception {

        User user =
            ls.getUser(sessionScope(LoginController.USER_ID_SESSION_KEY));

        if (user != null) {

            UserForm uf = new UserForm();
            uf.setEmail(user.getEmail());
            uf.setEmail2(user.getEmail2());
            uf.setName(user.getName());
            uf.setPasswd1(user.getPassword());
            uf.setPasswd2(user.getPassword());
            uf.setUserName(user.getUserName());

            // System.out.println(user.getTags());

            List<Tag> fetch = tagService.fetch(user);
            StringBuilder buf = new StringBuilder();
            for (Tag tag : fetch) {
                buf.append("" + tag.getTag() + ",");
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            uf.setTags(buf.toString());
            // System.out.println(buf.toString());

            TwitterToken tt = ts.getTwitterToken(user.getName());
            if (tt != null) {
                requestScope("twitterId", tt.getTwitterId());
            }

            if (user.getUserIcon() != null) {
                UploadedData ud =
                    Datastore.get(UploadedData.class, user.getUserIcon());
                requestScope("userIcon", Datastore.keyToString(ud.getKey()));
            }

            requestScope("user", uf);
        } else {
            return forward(basePath);
        }

        return forward("mypage.jsp");

    }
}