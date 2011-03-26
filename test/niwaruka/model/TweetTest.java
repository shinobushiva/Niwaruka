package niwaruka.model;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TweetTest extends AppEngineTestCase {

    private Tweet model = new Tweet();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
