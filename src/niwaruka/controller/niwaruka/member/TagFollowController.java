package niwaruka.controller.niwaruka.member;

import niwaruka.controller.niwaruka.login.LoginController;
import niwaruka.model.Tag;
import niwaruka.model.User;
import niwaruka.service.LoginService;
import niwaruka.service.TagService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

public class TagFollowController extends Controller {

    private TagService ts = new TagService();
    private LoginService ls = new LoginService();

    @Override
    public Navigation run() throws Exception {
        String id = requestScope("id");

        Key key = Datastore.createKey(Tag.class, Long.parseLong(id));
        Tag tag = Datastore.get(Tag.class, key);

        User user =
            ls.getUser(sessionScope(LoginController.USER_ID_SESSION_KEY));

        if (user != null) {
            ts.toggleTag(tag, user, ls);
        }

        user = ls.getUser(sessionScope(LoginController.USER_ID_SESSION_KEY));
        requestScope("user", user);

        return redirect("/niwaruka/tag/" + id);
    }
}
