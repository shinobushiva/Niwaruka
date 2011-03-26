package niwaruka.controller.niwaruka.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.topgate.controller.JsonController;
import niwaruka.controller.niwaruka.login.LoginController;
import niwaruka.model.Tag;
import niwaruka.model.User;
import niwaruka.service.LoginService;
import niwaruka.service.TweetService;

import org.slim3.datastore.Datastore;
import org.slim3.util.BeanUtil;

import com.google.appengine.api.datastore.Key;

public class TweetJsonController extends JsonController {

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

        String[] ta = (String[]) requestScope("tagArray");
        List<Tag> tags = new ArrayList<Tag>();
        for (String tagStr : ta) {
            Long l = Long.parseLong(tagStr);
            Key key = Datastore.createKey(Tag.class, l);
            Tag tag = Datastore.get(Tag.class, key);
            tags.add(tag);
        }

        tweetService.addTweet(f.getTweet(), user, ls, tags);

        return map;
    }

    public class TweetForm {
        private String tweet;

        public void setTweet(String tweet) {
            this.tweet = tweet;
        }

        public String getTweet() {
            return tweet;
        }

    }
}
