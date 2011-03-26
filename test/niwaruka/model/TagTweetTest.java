package niwaruka.model;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TagTweetTest extends AppEngineTestCase {

    private TagTweet model = new TagTweet();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
