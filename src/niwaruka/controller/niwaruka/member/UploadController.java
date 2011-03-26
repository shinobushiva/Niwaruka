package niwaruka.controller.niwaruka.member;

import niwaruka.controller.niwaruka.login.LoginController;
import niwaruka.model.UploadedData;
import niwaruka.model.User;
import niwaruka.service.LoginService;
import niwaruka.service.UploadService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.upload.FileItem;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;

public class UploadController extends Controller {

    private UploadService service = new UploadService();
    private LoginService ls = new LoginService();

    @Override
    public Navigation run() {

        String clear = requestScope("clear");

        User user =
            ls.getUser(sessionScope(LoginController.USER_ID_SESSION_KEY));
        if ("true".equals(clear)) {
            user.setUserIcon(null);
        } else {
            
            FileItem formFile = requestScope("formFile");
            UploadedData upload = service.upload(formFile);
            if (upload == null) {
                return redirect("mypage");
            }
            user.setUserIcon(upload.getKey());
        }

        GlobalTransaction gtx = Datastore.beginGlobalTransaction();
        Datastore.put(user);
        gtx.commit();

        return redirect("mypage");
    }
}
