package niwaruka.controller.niwaruka;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class CharactersController extends Controller {

    @Override
    public Navigation run() throws Exception {
        return forward("characters.jsp");
    }
}
