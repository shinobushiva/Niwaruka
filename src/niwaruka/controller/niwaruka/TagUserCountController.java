package niwaruka.controller.niwaruka;

import java.util.List;

import niwaruka.meta.UserMeta;
import niwaruka.model.Tag;
import niwaruka.model.User;
import niwaruka.service.DatastoreService;
import niwaruka.service.LoginService;
import niwaruka.service.TwitterService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

public class TagUserCountController extends Controller {

    TwitterService ts = new TwitterService();
    LoginService ls = new LoginService();
    DatastoreService ds = new DatastoreService();

    @Override
    public Navigation run() throws Exception {

        List<Tag> tags = Datastore.query(Tag.class).asList();
        for (Tag tag : tags) {
            int count =
                Datastore
                    .query(User.class)
                    .filter(UserMeta.get().tags.equal(tag.getKey()))
                    .count();
            tag.setUserCount(count);
            ds.put(tag);
        }

        return null;
    }
}
