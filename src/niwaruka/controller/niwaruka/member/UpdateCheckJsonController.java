package niwaruka.controller.niwaruka.member;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import jp.co.topgate.controller.JsonController;
import niwaruka.controller.niwaruka.login.LoginController;
import niwaruka.model.User;
import niwaruka.service.LoginService;
import niwaruka.service.TweetService;

import com.google.appengine.api.datastore.Key;

public class UpdateCheckJsonController extends JsonController {

    private static final Logger LOG = Logger.getLogger(TweetService.class
        .getName());

    private TweetService tweetService = new TweetService();
    private LoginService ls = new LoginService();

    @Override
    protected Map<String, Object> handle() throws Exception {

        LOG.info("UpdateCheckJsonController#handle");

        Map<String, Object> map = new HashMap<String, Object>();

        Date date = new Date(Long.parseLong((String) requestScope("time")));

        User user =
            ls.getUser(sessionScope(LoginController.USER_ID_SESSION_KEY));
        if (user != null) {

            ArrayList<Key> tags = new ArrayList<Key>(user.getTags());
            tags.removeAll(user.getDisabledTags());

            int num = tweetService.countWithTagList(user, tags, date);
            map.put("newTweets", num);
        }

        return map;
    }
}
