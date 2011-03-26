package niwaruka.controller.niwaruka;

import java.io.FileInputStream;

import niwaruka.model.UploadedData;
import niwaruka.model.User;
import niwaruka.service.LoginService;
import niwaruka.service.UploadService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.google.appengine.api.datastore.Key;

public class UserImageController extends Controller {

    private UploadService service = new UploadService();
    private LoginService ls = new LoginService();

    @Override
    public Navigation run() throws Exception {
        String name = asString("name");
        User user = ls.getUser(name);
        if (user == null) {
            return null;
        }
        Key asKey = user.getUserIcon();

        if (asKey != null) {
            UploadedData data = service.getData(asKey);
            byte[] bytes = service.getBytes(data);
            download(data.getFileName(), bytes);
        } else {
            download("niwaruka_sqr.png", new FileInputStream(
                "resources/niwaruka_sqr.png"));
        }

        return null;
    }
}
