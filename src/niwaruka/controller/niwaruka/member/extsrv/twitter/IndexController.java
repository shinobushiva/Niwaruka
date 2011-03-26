package niwaruka.controller.niwaruka.member.extsrv.twitter;

import niwaruka.model.TwitterToken;
import niwaruka.service.TwitterService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class IndexController extends Controller {

    TwitterService ts = new TwitterService();

    @Override
    public Navigation run() throws Exception {

        TwitterToken tt = ts.getTwitterToken((String) sessionScope("user"));
        if (tt != null) {
            requestScope("twitterId", tt.getTwitterId());
        }
        return forward("index.jsp");
    }
}
