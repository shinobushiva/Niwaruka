package niwaruka.controller.niwaruka.member;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import jp.co.topgate.controller.JsonController;
import niwaruka.controller.niwaruka.login.LoginController;
import niwaruka.model.User;
import niwaruka.service.LoginService;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;

import com.google.appengine.api.datastore.Key;

public class TagChangeJsonController extends JsonController {

    private static final Logger LOG = Logger
        .getLogger(TagChangeJsonController.class.getName());

    LoginService ls = new LoginService();

    @Override
    protected Map<String, Object> handle() throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();

        String[] ta = (String[]) requestScope("tagArray");
        List<String> enableTags = new ArrayList<String>();
        if (ta != null) {
            enableTags = Arrays.asList(ta);
        }

        User user =
            ls.getUser(sessionScope(LoginController.USER_ID_SESSION_KEY));

        // チェックされていなかったタグをDisabledTagsに追加
        user.getDisabledTags().clear();
        List<Key> tags2 = user.getTags();
        for (Key key : tags2) {
            if (!enableTags.contains("" + key.getId())) {
                user.getDisabledTags().add(key);
            }
        }

        GlobalTransaction tx = Datastore.beginGlobalTransaction();
        Datastore.put(user);
        tx.commit();

        LOG.info("Some user tags are disabled : " + user.getDisabledTags());

        return map;
    }
}
