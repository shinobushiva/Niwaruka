package niwaruka.controller.niwaruka.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import jp.co.topgate.controller.JsonController;
import niwaruka.model.Tag;
import niwaruka.model.UserData;
import niwaruka.service.LoginService;
import niwaruka.service.TagService;
import niwaruka.service.TweetService;

public class TagSearchJsonController extends JsonController {

    private static final Logger LOG = Logger.getLogger(TweetService.class
        .getName());

    TweetService tweetService = new TweetService();
    LoginService ls = new LoginService();
    TagService ts = new TagService();

    @Override
    protected Map<String, Object> handle() throws Exception {

        LOG.info("TagSearchJsonController#handle");

        Map<String, Object> map = new HashMap<String, Object>();

        String query = (String) requestScope("query");
        if (query == null || query.isEmpty()) {
            return map;
        }

        int num = 20;
        if (asInteger("num") != null) {
            num = asInteger("num");
        }

        String str = sessionScope("user");
        UserData ud = ls.getUserData(str);

        List<Tag> tags = ts.search(query, num, ud);
        map.put("tags", tags);

        return map;
    }
}
