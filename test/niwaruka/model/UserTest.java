package niwaruka.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import niwaruka.meta.UserMeta;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class UserTest extends AppEngineTestCase {

    private User user = new User();

    @Test
    public void test() throws Exception {
        assertThat(user, is(notNullValue()));

        Tag tag = new Tag();
        tag.setTag("Izumiken");
        Transaction tx2 = Datastore.beginTransaction();
        Key key = Datastore.put(tag);
        tx2.commit();

        user.setName("Shinobu");
        user.setPassword("Izumi");
        user.getTags().add(key);
        Transaction tx = Datastore.beginTransaction();
        Datastore.put(user);
        tx.commit();

        User user = Datastore.query(User.class).asSingle();
        List<Key> tags = user.getTags();
        for (Key mr : tags) {
            Tag tag2 = Datastore.get(Tag.class, mr);
            System.out.println(tag2.getTag());
        }

        User u2 =
            Datastore
                .query(User.class)
                .filter(UserMeta.get().name.equal("Shinobu"))
                .filter(UserMeta.get().password.equal("Izumi"))
                .asSingle();
        System.out.println(u2.getName());
    }
}
