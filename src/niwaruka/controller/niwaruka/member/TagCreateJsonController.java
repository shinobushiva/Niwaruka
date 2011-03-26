package niwaruka.controller.niwaruka.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import jp.co.topgate.controller.JsonController;
import niwaruka.meta.TagMeta;
import niwaruka.model.Tag;
import niwaruka.model.Tag.TagAccess;
import niwaruka.model.UserData;
import niwaruka.service.LoginService;
import niwaruka.service.TagService;
import niwaruka.service.TweetService;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

public class TagCreateJsonController extends JsonController {

    private static final Logger LOG = Logger.getLogger(TweetService.class
        .getName());

    TweetService tweetService = new TweetService();
    LoginService ls = new LoginService();
    TagService ts = new TagService();

    @Override
    protected Map<String, Object> handle() throws Exception {

        LOG.info("TagCreateJsonController#handle");

        Map<String, Object> map = new HashMap<String, Object>();

        String query = (String) requestScope("query");
        String type = (String) requestScope("type");

        if (query == null || query.isEmpty() || query.equals("#")) {
            return map;
        }

        TagAccess ta = TagAccess.publicTag;
        if (!query.startsWith("#")) {
            ta = TagAccess.valueOf(type);
        }

        List<Tag> tags = null;
        if (ta.equals(TagAccess.publicTag)) {
            tags =
                Datastore
                    .query(Tag.class)
                    .filter(TagMeta.get().tag.equal(query))
                    .filter(TagMeta.get().access.equal(ta.name()))
                    .asList();
        } else {

            String str = sessionScope("user");
            UserData ud = ls.getUserData(str);

            tags =
                Datastore
                    .query(Tag.class)
                    .filter(TagMeta.get().tag.equal(query))
                    .filter(TagMeta.get().access.equal(ta.name()))
                    .filter(TagMeta.get().creatorRef.equal(ud.getKey()))
                    .asList();
        }

        if (tags.isEmpty()) {
            Key k =
                ts.saveTag(
                    ls.getUserData((String) sessionScope("user")),
                    query,
                    ta);

            tags.add(Datastore.get(Tag.class, k));
        }

        map.put("tags", tags);

        return map;
    }
}
