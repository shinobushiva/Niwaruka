package niwaruka.controller.niwaruka.login;

import org.slim3.tester.ControllerTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class NewPasswordExpiredControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/niwaruka/login/newPasswordExpired");
        NewPasswordExpiredController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/niwaruka/login/newPasswordExpired.jsp"));
    }
}