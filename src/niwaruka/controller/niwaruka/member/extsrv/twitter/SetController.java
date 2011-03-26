package niwaruka.controller.niwaruka.member.extsrv.twitter;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.http.RequestToken;

public class SetController extends Controller {

    @Override
    public Navigation run() throws Exception {

        // このファクトリインスタンスは再利用可能でスレッドセーフです
        // twitterオブジェクトをセッションに格納
        Twitter twitter = new TwitterFactory().getInstance();
        sessionScope("twitter", twitter);

        // callback用のURLを生成して格納
        StringBuffer callbackURL = request.getRequestURL();
        int index = callbackURL.lastIndexOf("/");
        callbackURL
            .replace(index, callbackURL.length(), "")
            .append("/callback");

        // RequestTokenを取得してセッションに格納、アプリケーション認可画面に移動
        RequestToken requestToken =
            twitter.getOAuthRequestToken(callbackURL.toString());
        request.getSession().setAttribute("requestToken", requestToken);

        return redirect(requestToken.getAuthenticationURL());

    }
}
