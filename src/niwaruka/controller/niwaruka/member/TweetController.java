package niwaruka.controller.niwaruka.member;

import javax.servlet.http.HttpSession;

import niwaruka.model.User;
import niwaruka.service.LoginService;
import niwaruka.service.TweetService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.util.BeanUtil;

public class TweetController extends Controller {

    TweetService tweetService = new TweetService();
    LoginService ls = new LoginService();

    @Override
    public Navigation run() throws Exception {

        TweetForm f = new TweetForm();
        BeanUtil.copy(request, f);
        if (f.getTweet() == null || f.getTweet().trim().length() <= 0) {
            return forward("index");
        }

        HttpSession session = request.getSession();
        String str = (String) session.getAttribute("user");
        User user = ls.getUser(str);

        tweetService.addTweet(f.getTweet(), user, ls);

        return forward("/niwaruka/member");
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
