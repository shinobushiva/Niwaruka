package niwaruka.controller.niwaruka.login.auth;

import java.io.FileInputStream;
import java.util.Properties;

import org.scribe.http.Request;
import org.scribe.http.Request.Verb;
import org.scribe.oauth.Scribe;
import org.scribe.oauth.Token;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class LinkedinController extends Controller {

    @Override
    public Navigation run() throws Exception {

        Properties props = new Properties();
        FileInputStream fis = new FileInputStream("auth/linkedin.properties");
        props.load(fis);
        Scribe scribe = new Scribe(props);

        String verifier = requestScope("oauth_verifier");

        if (verifier == null) {
            Token requestToken = scribe.getRequestToken();
            String path =
                "https://www.linkedin.com/uas/oauth/authorize?oauth_token="
                    + requestToken.getToken();
            sessionScope("secret", requestToken.getSecret());
            return redirect(path);
        } else {
            String token = requestScope("oauth_token");
            String secret = sessionScope("secret");
            Token requestToken = new Token(token, secret);
            Token accessToken = scribe.getAccessToken(requestToken, verifier);

            // Get Data from linked in
            Request request =
                new Request(
                    Verb.GET,
                    "http://www.linkedin.com/v1/people/~/network?count=50");

            scribe.signRequest(request, accessToken);
            // Response response = request.send();
            // Do something with the response.

            return null;
        }
    }
}
