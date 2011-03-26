package niwaruka.controller.niwaruka.login;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class NewPasswordExpiredController extends Controller {

    @Override
    public Navigation run() throws Exception {
        return forward("/niwaruka/error.jsp");
    }
}
