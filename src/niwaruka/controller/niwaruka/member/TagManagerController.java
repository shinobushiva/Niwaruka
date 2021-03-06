package niwaruka.controller.niwaruka.member;

import java.util.ArrayList;
import java.util.List;

import niwaruka.controller.niwaruka.login.LoginController;
import niwaruka.model.Tag;
import niwaruka.model.User;
import niwaruka.service.LoginService;
import niwaruka.service.TweetService;
import niwaruka.service.TwitterService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

public class TagManagerController extends Controller {

    TweetService tweetService = new TweetService();
    TwitterService ts = new TwitterService();
    LoginService ls = new LoginService();

    @Override
    public Navigation run() throws Exception {
        String str = (String) sessionScope("user");
        if (str != null) {

            User user =
                ls.getUser(sessionScope(LoginController.USER_ID_SESSION_KEY));

            if (user != null) {

                List<Tag> list = Datastore.get(Tag.class, user.getTags());
                requestScope("tags", list);

                List<Tag> cl = new ArrayList<Tag>(list);
                if (user.getDisabledTags() != null) {
                    List<Tag> tl =
                        Datastore.get(Tag.class, user.getDisabledTags());
                    cl.removeAll(tl);
                    // System.out.println("dt : " + tl);
                    requestScope("disabledTags", tl);
                }
                requestScope("enabledTags", cl);
            }

            // ts.fetchFromTwitter(str, tweetService, ls);

        }

        return forward("tagManager.jsp");
    }
}
