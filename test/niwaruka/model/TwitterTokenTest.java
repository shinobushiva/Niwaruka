package niwaruka.model;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TwitterTokenTest extends AppEngineTestCase {

    private TwitterToken model = new TwitterToken();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
