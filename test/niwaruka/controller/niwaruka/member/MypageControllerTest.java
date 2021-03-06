package niwaruka.controller.niwaruka.member;

import org.slim3.tester.ControllerTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class MypageControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/niwaruka/member/mypage");
        MypageController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/niwaruka/member/mypage.jsp"));
    }
}
