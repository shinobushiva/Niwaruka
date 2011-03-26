package niwaruka.controller.niwaruka.login;

import niwaruka.model.User;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;
import org.slim3.tester.ControllerTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class LoginControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {

        User u = new User();
        u.setName("shiva");
        u.setPassword("password");

        GlobalTransaction gtx = Datastore.beginGlobalTransaction();
        Datastore.put(u);
        gtx.commit();

        tester.param("name", "shiva");
        tester.param("passwd", "password");

        tester.start("/niwaruka/login/login");
        LoginController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));

        assertThat(tester.getDestinationPath(), is("/niwaruka/login/login.jsp"));

    }

    @Test
    public void run2() throws Exception {

        User u = new User();
        u.setName("shiva");
        u.setPassword("password");

        GlobalTransaction gtx = Datastore.beginGlobalTransaction();
        Datastore.put(u);
        gtx.commit();

        tester.param("name", "hoge");
        tester.param("passwd", "hoge");

        tester.start("/niwaruka/login/login");
        LoginController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));

        assertThat(tester.getDestinationPath(), is("/niwaruka/login/index.jsp"));

    }
}
