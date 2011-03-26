package niwaruka.controller.niwaruka.member;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import jp.co.topgate.controller.JsonController;
import niwaruka.model.Tag;
import niwaruka.model.TagPermissionRequest;
import niwaruka.model.User;
import niwaruka.service.DatastoreService;
import niwaruka.service.LoginService;
import niwaruka.service.TweetService;

public class TagRequestDetermineJsonController extends JsonController {

    private static final Logger LOG = Logger.getLogger(TweetService.class
        .getName());

    TweetService ts = new TweetService();
    LoginService ls = new LoginService();
    DatastoreService ds = new DatastoreService();

    @Override
    protected Map<String, Object> handle() throws Exception {

        LOG.info("TagRequestCheckJsonController#handle");
        Map<String, Object> map = new HashMap<String, Object>();

        String id = requestScope("id");

        boolean yesno = false;
        if (id.startsWith("yes-")) {
            yesno = true;
        }
        id = id.replace("yes-", "").replace("no-", "");

        TagPermissionRequest tpr =
            ds.get(TagPermissionRequest.class, Long.parseLong(id));

        if (yesno) {

            User model = tpr.getRequester().getModel();
            if (!model.getTags().contains(tpr.getTag().getKey())) {
                model.getTags().add(tpr.getTag().getKey());
                ds.put(model);

                Tag tag = tpr.getTag().getModel();
                tag.setUserCount(tag.getUserCount() + 1);
                ds.put(model);
            }
            tpr.setState(TagPermissionRequest.STATE_PERMITTED);
        } else {
            tpr.setState(TagPermissionRequest.STATE_DECLINED);
        }
        ds.put(tpr);

        return map;
    }
}
