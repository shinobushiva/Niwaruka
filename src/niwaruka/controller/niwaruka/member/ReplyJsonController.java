package niwaruka.controller.niwaruka.member;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.co.topgate.controller.JsonController;
import niwaruka.controller.niwaruka.login.LoginController;
import niwaruka.model.Tweet;
import niwaruka.model.User;
import niwaruka.model.UserData;
import niwaruka.service.LoginService;
import niwaruka.service.TweetService;

import org.slim3.datastore.Datastore;
import org.slim3.util.BeanUtil;

import com.google.appengine.api.datastore.Key;

public class ReplyJsonController extends JsonController {

    TweetService tweetService = new TweetService();
    LoginService ls = new LoginService();

    @Override
    protected Map<String, Object> handle() throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();

        TweetForm f = new TweetForm();
        BeanUtil.copy(request, f);
        if (f.getTweet() == null || f.getTweet().trim().length() <= 0) {
            return map;
        }

        User user =
            ls.getUser(sessionScope(LoginController.USER_ID_SESSION_KEY));
        UserData userData =
            ls.getUserData(sessionScope(LoginController.USER_ID_SESSION_KEY));

        Key key = Datastore.createKey(Tweet.class, f.getId());
        Tweet tweet = Datastore.get(Tweet.class, key);

        Tweet nt = new Tweet();
        Date time = new Date();
        nt.setContent(f.getTweet());
        nt.setTime(time);
        nt.getUserDataRef().setModel(userData);
        nt.setTagList(tweet.getTagList());
        nt.setReplyTo(key);

        tweetService.addTweet(nt, user, ls);

        return map;
    }

    public class TweetForm {
        private String tweet;
        private long id;

        public void setTweet(String tweet) {
            this.tweet = tweet;
        }

        public String getTweet() {
            return tweet;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }

    }
}
