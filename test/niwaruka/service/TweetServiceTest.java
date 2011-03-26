package niwaruka.service;

import niwaruka.service.TweetService;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TweetServiceTest extends AppEngineTestCase {

    private TweetService service = new TweetService();

    @Test
    public void test() throws Exception {
        assertThat(service, is(notNullValue()));
    }
}
