package niwaruka.controller.mail;

import org.slim3.tester.ControllerTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class ReceiveControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/mail/receive");
        ReceiveController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/mail/receive.jsp"));
    }
}
