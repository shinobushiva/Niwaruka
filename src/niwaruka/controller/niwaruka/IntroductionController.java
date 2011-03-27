package niwaruka.controller.niwaruka;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class IntroductionController extends Controller {

    @Override
    public Navigation run() throws Exception {
        return redirect("introduction.jsp");
    }
}
