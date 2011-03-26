package niwaruka.controller.niwaruka.member;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import jp.co.topgate.controller.JsonController;
import niwaruka.controller.niwaruka.login.LoginController;
import niwaruka.model.Tweet;
import niwaruka.model.User;
import niwaruka.service.LoginService;
import niwaruka.service.TweetService;

import com.google.appengine.api.datastore.Key;

public class TweetFetchJsonController extends JsonController {

    private static final Logger LOG = Logger.getLogger(TweetService.class
        .getName());

    TweetService tweetService = new TweetService();
    LoginService ls = new LoginService();

    @Override
    protected Map<String, Object> handle() throws Exception {

        LOG.info("TweetFetchJsonController#handle");

        Map<String, Object> map = new HashMap<String, Object>();

        String str = sessionScope("user");
        if (str != null) {
            User user =
                ls.getUser(sessionScope(LoginController.USER_ID_SESSION_KEY));
            if (user != null) {

                ArrayList<Key> tags = new ArrayList<Key>(user.getTags());
                tags.removeAll(user.getDisabledTags());

                String olderThan = requestScope("olderThan");
                if (olderThan != null) {
                    try {
                        long parseLong = Long.parseLong(olderThan);
                        List<Tweet> fetch =
                            tweetService.fetchWithTagListOlderThan(
                                user,
                                tags,
                                new Date(parseLong));
                        map.put("tweets", fetch);
                    } catch (Exception e) {
                        map.put("tweets", new ArrayList<Tweet>());
                    }
                } else {
                    List<Tweet> fetch =
                        tweetService.fetchWithTagList(user, tags);
                    map.put("tweets", fetch);
                }
            }

        }
        return map;
    }
}
