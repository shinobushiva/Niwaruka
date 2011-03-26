package niwaruka.controller.niwaruka;

import niwaruka.controller.niwaruka.login.UserRegistrationController;

import org.slim3.tester.ControllerTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class UserRegistrationControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/niwaruka/userRegistration");
        UserRegistrationController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is(nullValue()));
    }
}
