package niwaruka.controller.niwaruka.member.extsrv.twitter;

import niwaruka.service.TwitterService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class UnsetController extends Controller {

    TwitterService ts = new TwitterService();

    @Override
    public Navigation run() throws Exception {

        ts.removeTwitterConnection((String) sessionScope("user"));

        return redirect("/niwaruka/member/mypage");

    }
}
