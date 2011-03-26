package niwaruka.controller.niwaruka.member.extsrv.twitter;

import niwaruka.service.TwitterService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

public class CallbackController extends Controller {

    TwitterService ts = new TwitterService();

    @Override
    public Navigation run() throws Exception {

        String user = (String) sessionScope("user");

        // セッションからtwitterオブジェクトとRequestTokenの取出
        AccessToken accessToken = null;
        Twitter twitter = (Twitter) sessionScope("twitter");
        RequestToken requestToken = (RequestToken) sessionScope("requestToken");
        String verifier = request.getParameter("oauth_verifier");

        // AccessTokenの取得
        try {
            accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
            removeSessionScope("requestToken");
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        if (accessToken != null) {
            ts.connectToTwitter(accessToken, user);
        }

        return redirect("/niwaruka/member/mypage");
    }

}
