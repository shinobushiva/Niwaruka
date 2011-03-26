package niwaruka.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Transaction;

public class TagTest extends AppEngineTestCase {

    private Tag model = new Tag();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
        model.setTag("Izumiken");

        Transaction tx = Datastore.beginTransaction();
        Datastore.put(model);
        tx.commit();

        Tag t = Datastore.query(Tag.class).asSingle();
        assertThat(t.getTag(), is("Izumiken"));
        System.out.println(t.getTag());
    }
}
