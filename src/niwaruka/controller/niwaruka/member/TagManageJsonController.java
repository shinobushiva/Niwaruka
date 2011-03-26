package niwaruka.controller.niwaruka.member;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.topgate.controller.JsonController;
import niwaruka.controller.niwaruka.login.LoginController;
import niwaruka.model.Tag;
import niwaruka.model.User;
import niwaruka.service.LoginService;
import niwaruka.service.TagService;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;

import com.google.appengine.api.datastore.Key;

public class TagManageJsonController extends JsonController {

    // private static final Logger LOG = Logger
    // .getLogger(TagManageJsonController.class.getName());

    LoginService ls = new LoginService();
    TagService ts = new TagService();

    @Override
    protected Map<String, Object> handle() throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();

        String[] ta = (String[]) requestScope("tagArray");
        List<String> eTags = new ArrayList<String>();
        if (ta != null) {
            eTags = Arrays.asList(ta);
        }

        String[] dta = (String[]) requestScope("dTagArray");
        List<String> dTags = new ArrayList<String>();
        if (dta != null) {
            dTags = Arrays.asList(dta);
        }

        // System.out.println("==");
        //
        // System.out.println(eTags);
        // System.out.println(dTags);

        User user =
            ls
                .getUser((String) sessionScope(LoginController.USER_ID_SESSION_KEY));

        if (user.getTags() == null) {
            user.setTags(new ArrayList<Key>());
        }
        if (user.getDisabledTags() == null) {
            user.setDisabledTags(new ArrayList<Key>());
        }

        List<Key> newEnabledTags = new ArrayList<Key>();
        for (String t : eTags) {
            Key ck = Datastore.createKey(Tag.class, Long.parseLong(t));
            newEnabledTags.add(ck);
        }

        List<Key> newDisabledTags = new ArrayList<Key>();
        for (String t : dTags) {
            Key ck = Datastore.createKey(Tag.class, Long.parseLong(t));
            newDisabledTags.add(ck);
        }

        List<Key> newTags = new ArrayList<Key>();
        newTags.addAll(newEnabledTags);
        newTags.addAll(newDisabledTags);

        List<Key> currentTags = new ArrayList<Key>(user.getTags());

        currentTags.removeAll(newTags);

        // System.out.println(user.getTags());

        user.getTags().removeAll(currentTags);

        newTags.removeAll(user.getTags());

        // System.out.println(newTags);
        // System.out.println(newDisabledTags);

        for (Key ck : newTags) {
            if (!user.getTags().contains(ck)) {
                Tag tag = Datastore.get(Tag.class, ck);
                ts.toggleTag(tag, user, ls);
            }
        }

        List<Key> ndt = new ArrayList<Key>(newDisabledTags);
        ndt.remove(user.getTags());
        newDisabledTags.remove(ndt);
        user.setDisabledTags(newDisabledTags);

        // System.out.println(user.getTags());
        // System.out.println(user.getDisabledTags());

        GlobalTransaction tx = Datastore.beginGlobalTransaction();
        Datastore.put(user);
        tx.commit();

        // LOG.info("Some user tags are disabled : " + user.getDisabledTags());

        return map;
    }
}
