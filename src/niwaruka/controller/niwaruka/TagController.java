package niwaruka.controller.niwaruka;

import niwaruka.controller.niwaruka.login.LoginController;
import niwaruka.meta.TagMeta;
import niwaruka.model.Tag;
import niwaruka.model.User;
import niwaruka.service.LoginService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

public class TagController extends Controller {

    private LoginService ls = new LoginService();

    @Override
    public Navigation run() throws Exception {

        String id = requestScope("id");

        Tag tag = null;
        try {
            Key key = Datastore.createKey(Tag.class, Long.parseLong(id));
            tag = Datastore.get(Tag.class, key);
        } catch (NumberFormatException e) {
            tag =
                Datastore
                    .query(Tag.class)
                    .filter(TagMeta.get().tag.equal(id))
                    .filter(
                        TagMeta.get().access.equal(Tag.TagAccess.publicTag
                            .name()))
                    .asSingle();
        }
        if (tag != null) {
            User user =
                ls.getUser(sessionScope(LoginController.USER_ID_SESSION_KEY));

            requestScope("user", user);
            requestScope("tag", tag);

            return forward("tag.jsp");
        }

        return redirect("/");
    }
}
