package niwaruka.model;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class FetchedHashTagTest extends AppEngineTestCase {

    private FetchedHashTag model = new FetchedHashTag();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
