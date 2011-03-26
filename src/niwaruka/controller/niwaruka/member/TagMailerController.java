package niwaruka.controller.niwaruka.member;

import java.util.List;

import niwaruka.controller.niwaruka.login.LoginController;
import niwaruka.meta.TagMailMeta;
import niwaruka.model.Tag;
import niwaruka.model.TagMail;
import niwaruka.model.User;
import niwaruka.service.LoginService;
import niwaruka.service.TweetService;
import niwaruka.service.TwitterService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

public class TagMailerController extends Controller {

    TweetService tweetService = new TweetService();
    TwitterService ts = new TwitterService();
    LoginService ls = new LoginService();

    @Override
    public Navigation run() throws Exception {

        User user =
            ls.getUser(sessionScope(LoginController.USER_ID_SESSION_KEY));
        requestScope("email", user.getEmail());

        if (user != null) {
            List<Tag> list = Datastore.get(Tag.class, user.getTags());
            requestScope("tags", list);

            List<TagMail> tagMails =
                Datastore
                    .query(TagMail.class)
                    .filter(TagMailMeta.get().owner.equal(user.getKey()))
                    .asList();
            requestScope("tagMails", tagMails);
        }
        return forward("tagMailer.jsp");
    }
}
