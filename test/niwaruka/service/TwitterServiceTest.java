package niwaruka.service;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TwitterServiceTest extends AppEngineTestCase {

    private TwitterService service = new TwitterService();

    @Test
    public void test() throws Exception {
        assertThat(service, is(notNullValue()));
    }
}
