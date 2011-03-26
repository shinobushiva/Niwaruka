package niwaruka.controller.niwaruka.member;

import java.util.HashMap;
import java.util.Map;

import jp.co.topgate.controller.JsonController;
import niwaruka.controller.niwaruka.login.LoginController;
import niwaruka.model.Tweet;
import niwaruka.model.UserData;
import niwaruka.service.LoginService;
import niwaruka.service.TweetService;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

public class DeleteJsonController extends JsonController {

    TweetService tweetService = new TweetService();
    LoginService ls = new LoginService();

    @Override
    protected Map<String, Object> handle() throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();

        Long id = asLong("tweetId");

        // User user =
        // ls.getUser(sessionScope(LoginController.USER_ID_SESSION_KEY));
        UserData userData =
            ls.getUserData(sessionScope(LoginController.USER_ID_SESSION_KEY));

        Key key = Datastore.createKey(Tweet.class, id);
        Tweet tweet = Datastore.get(Tweet.class, key);

        tweetService.deleteTweet(tweet, userData);

        return map;
    }

}
