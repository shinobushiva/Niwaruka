package niwaruka.controller.niwaruka.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import jp.co.topgate.controller.JsonController;
import niwaruka.controller.niwaruka.login.LoginController;
import niwaruka.meta.TagPermissionRequestMeta;
import niwaruka.model.TagPermissionRequest;
import niwaruka.model.User;
import niwaruka.service.LoginService;
import niwaruka.service.TweetService;

import org.slim3.datastore.Datastore;

public class TagRequestCheckJsonController extends JsonController {

    private static final Logger LOG = Logger.getLogger(TweetService.class
        .getName());

    TweetService ts = new TweetService();
    LoginService ls = new LoginService();

    @Override
    protected Map<String, Object> handle() throws Exception {

        LOG.info("TagRequestCheckJsonController#handle");
        Map<String, Object> map = new HashMap<String, Object>();

        User user =
            ls.getUser(sessionScope(LoginController.USER_ID_SESSION_KEY));

        TagPermissionRequestMeta tprm = TagPermissionRequestMeta.get();
        List<TagPermissionRequest> list =
            Datastore
                .query(tprm)
                .filter(tprm.owner.equal(user.getKey()))
                .filter(tprm.state.equal(TagPermissionRequest.STATE_REQUESTED))
                .sort(tprm.time.desc)
                .asList();
        // System.out.println(list);
        map.put("requests", list);

        return map;
    }
}
