package niwaruka.controller.niwaruka.member;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;

import com.google.appengine.api.datastore.Key;

import jp.co.topgate.controller.JsonController;
import niwaruka.controller.niwaruka.login.LoginController;
import niwaruka.meta.TagMailMeta;
import niwaruka.model.Tag;
import niwaruka.model.TagMail;
import niwaruka.model.User;
import niwaruka.service.LoginService;

public class TagMailSetJsonController extends JsonController {

    // private static final Logger LOG = Logger
    // .getLogger(TagMailSetJsonController.class.getName());

    LoginService ls = new LoginService();

    @Override
    protected Map<String, Object> handle() throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();

        String[] ta = (String[]) requestScope("tagMailArray");
        List<String> tagMails = new ArrayList<String>();
        if (ta != null) {
            tagMails = Arrays.asList(ta);
        }
        // System.out.println(tagMails);

        User user =
            ls.getUser(sessionScope(LoginController.USER_ID_SESSION_KEY));

        List<Key> kls =
            Datastore
                .query(TagMail.class)
                .filter(TagMailMeta.get().owner.equal(user.getKey()))
                .asKeyList();
        for (Key key : kls) {
            GlobalTransaction gtx = Datastore.beginGlobalTransaction();
            Datastore.delete(key);
            gtx.commit();
        }

        for (String t : tagMails) {
            String[] split = t.split(",");
            String email = split[0];
            String opt = split[1];

            TagMail tm = new TagMail();
            tm.setEmail(email);
            tm.setOption(opt);
            List<Key> tkList = new ArrayList<Key>();
            for (int i = 2; i < split.length; i++) {
                Key ck =
                    Datastore.createKey(Tag.class, Long.parseLong(split[i]));
                tkList.add(ck);
            }
            tm.setTags(tkList);
            tm.setLastMessage(new Date());
            tm.setOwner(user.getKey());
            tm.setTagList(Datastore.get(Tag.class, tkList));

            GlobalTransaction gtx = Datastore.beginGlobalTransaction();
            Datastore.put(tm);
            gtx.commit();
        }

        //
        // GlobalTransaction tx = Datastore.beginGlobalTransaction();
        // Datastore.put(user);
        // tx.commit();
        //
        // LOG.info("Some user tags are disabled : " + user.getDisabledTags());

        return map;
    }
}
