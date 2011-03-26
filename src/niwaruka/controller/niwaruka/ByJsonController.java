package niwaruka.controller.niwaruka;

import java.util.List;
import java.util.Map;

import jp.co.topgate.controller.JsonController;
import niwaruka.model.Tweet;
import niwaruka.model.User;
import niwaruka.service.TweetService;

import org.slim3.datastore.Datastore;

import com.google.appengine.repackaged.com.google.common.collect.Maps;

public class ByJsonController extends JsonController {

    TweetService tweetService = new TweetService();

    @Override
    protected Map<String, Object> handle() throws Exception {

        Map<String, Object> map = Maps.newHashMap();

        User user = Datastore.query(User.class).limit(1).asSingle();
        if (user != null) {
            List<Tweet> fetch = tweetService.fetchSamples(10);
            map.put("tweets", fetch);
        }

        return map;
    }
}
