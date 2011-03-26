package niwaruka.service;

import java.util.logging.Logger;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;

import com.google.appengine.api.datastore.Key;

/*
 * 入力されたユーザ名とパスワードが一致してるか確認するクラス
 */
public class DatastoreService {

    Logger LOG = Logger.getLogger(DatastoreService.class.getName());

    public void put(Object model) {
        GlobalTransaction gtx = Datastore.beginGlobalTransaction();
        Datastore.put(model);
        gtx.commit();

    }

    public <T> T get(Class<T> t, long id) {
        Key key = Datastore.createKey(t, id);
        return Datastore.get(t, key);
    }

}