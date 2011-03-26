package niwaruka.controller.niwaruka.login;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

public class UserRegistrationControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {

        UserForm u = new UserForm();
        u.setName("shiva");
        u.setUserName("和泉 信生");
        u.setPasswd1("test");
        u.setPasswd2("test");
        u.setEmail("stagesp1@gmail.com");
        u.setTags("");
        tester.sessionScope("registratingUser", u);

        tester.start("/niwaruka/login/userRegistration");
        UserRegistrationController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(
            tester.getDestinationPath(),
            is("/niwaruka/login/userRegistration.jsp"));
    }
}
