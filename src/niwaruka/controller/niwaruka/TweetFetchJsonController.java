package niwaruka.controller.niwaruka;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import jp.co.topgate.controller.JsonController;
import niwaruka.model.Tag;
import niwaruka.model.Tweet;
import niwaruka.service.LoginService;
import niwaruka.service.TweetService;

import org.slim3.datastore.Datastore;

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

        Long tagId = asLong("tagId");
        Key tagKey = Datastore.createKey(Tag.class, tagId);
        String olderThan = requestScope("olderThan");
        if (olderThan != null) {
            long parseLong = Long.parseLong(olderThan);
            List<Tweet> fetch =
                tweetService.fetchWithTagOlderThan(tagKey, new Date(parseLong));
            map.put("tweets", fetch);

        } else {
            List<Tweet> fetch =
                tweetService.fetchWithTagOlderThan(tagKey, new Date());
            map.put("tweets", fetch);
        }

        System.out.println(map);

        return map;
    }
}
