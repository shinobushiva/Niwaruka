package niwaruka.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import niwaruka.model.User;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class LoginServiceTest extends AppEngineTestCase {

    private LoginService service = new LoginService();

    @Test
    public void testRegistrate() throws Exception {
        assertThat(service, is(notNullValue()));

        User u = new User();
        u.setName("shiva");
        u.setPassword("password");

        service.saveUser(u);

        User user = service.getUser(u.getName());
        assertEquals(user, u);

    }

    @Test
    public void testAuthorize() throws Exception {
        assertThat(service, is(notNullValue()));

        User u = new User();
        u.setName("shiva");
        u.setPassword("password");

        service.saveUser(u);

        boolean authorize = service.authorize(u.getName(), u.getPassword());
        assertThat(authorize, is(true));
    }

    @Test
    public void testGetUser() throws Exception {
        assertThat(service, is(notNullValue()));

        User u = new User();
        u.setName("shiva");
        u.setPassword("password");

        service.saveUser(u);

        User user = service.getUser(u.getName());
        assertEquals(user, u);
    }
}
