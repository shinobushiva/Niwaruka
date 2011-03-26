package niwaruka.controller.niwaruka.member;

import java.util.HashMap;
import java.util.Map;

import jp.co.topgate.controller.JsonController;
import niwaruka.controller.niwaruka.login.LoginController;
import niwaruka.model.User;
import niwaruka.service.LoginService;
import niwaruka.service.TweetService;

import org.slim3.util.BeanUtil;

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

        tweetService.addTweet(f.getTweet(), user, ls);

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
