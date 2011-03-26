package niwaruka.controller.niwaruka.login;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class IndexController extends Controller {

    
    // /niwaruka/login/にきたら/niwarukaに戻すだけ
    @Override
    public Navigation run() throws Exception {
        return forward("/niwaruka");
    }
}
