package niwaruka.controller.niwaruka;

import niwaruka.service.LoginService;
import niwaruka.service.TwitterService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class HashtagFetcherController extends Controller {

    TwitterService ts = new TwitterService();
    LoginService ls = new LoginService();

    @Override
    public Navigation run() throws Exception {

        ts.fetchHashTags(ls, false);

        return null;
    }
}
