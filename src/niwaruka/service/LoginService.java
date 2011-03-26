package niwaruka.service;

import java.util.Arrays;
import java.util.logging.Logger;

import niwaruka.meta.UserDataMeta;
import niwaruka.meta.UserMeta;
import niwaruka.model.User;
import niwaruka.model.UserData;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

/*
 * 入力されたユーザ名とパスワードが一致してるか確認するクラス
 */
public class LoginService {

    Logger LOG = Logger.getLogger(LoginService.class.getName());

    TagService ts = new TagService();

    public boolean authorize(String name, String pass) {

        LOG.info("name=" + name + ", pass=" + pass);

        User user = getUser(name);
        if (user != null && user.getPassword().equals(pass)) {
            return true;
        }

        return false;
    }

    public void save(User u) {
        GlobalTransaction gtx = Datastore.beginGlobalTransaction();
        Datastore.put(u);
        gtx.commit();
    }

    public User getUser(String name) {
        User user =
            Datastore
                .query(User.class)
                .filter(UserMeta.get().name.equal(name))
                .asSingle();
        return user;
    }

    public User getUser(Object name) {
        if (name instanceof String) {
            return getUser((String) name);
        }
        return null;
    }

    public UserData getUserData(Object name) {
        if (name instanceof String) {
            return getUserData((String) name);
        }
        return null;
    }

    public UserData getUserData(String name) {
        return getUserData(getUser(name));
    }

    public UserData getUserData(User u) {
        return Datastore
            .query(UserData.class)
            .filter(UserDataMeta.get().userKey.equal(u.getKey()))
            .asSingle();
    }

    public User getUserByCookieString(String cookie) {
        User user =
            Datastore
                .query(User.class)
                .filter(UserMeta.get().cookieString.equal(cookie))
                .asSingle();
        return user;
    }

    public User getUserByEmail(String email) {
        User user =
            Datastore
                .query(User.class)
                .filter(UserMeta.get().email.equal(email))
                .asSingle();
        if (user == null) {
            user =
                Datastore
                    .query(User.class)
                    .filter(UserMeta.get().email2.equal(email))
                    .asSingle();
        }
        return user;
    }

    public User getUserByNewPasswordKey(String key) {
        User user =
            Datastore
                .query(User.class)
                .filter(UserMeta.get().newPasswordKey.equal(key))
                .asSingle();
        return user;
    }

    public void saveUser(User u) {

        if (u.getKey() == null) {
            Key userKey = Datastore.allocateId(User.class);
            u.setKey(userKey);
        }

        Transaction tx = Datastore.beginTransaction();
        Datastore.put(u);
        tx.commit();

        // UserDataを保存・更新
        UserData ud =
            Datastore
                .query(UserData.class)
                .filter(UserDataMeta.get().userKey.equal(u.getKey()))
                .asSingle();
        if (ud == null) {
            ud = new UserData();
        }
        ud.setName(u.getName());
        ud.setUserName(u.getUserName());
        ud.setUserIcon(u.getUserIcon());
        ud.setUserKey(u.getKey());
        tx = Datastore.beginTransaction();
        Datastore.put(ud);
        tx.commit();
    }

    public User getMasterUser() {
        User u = getUser("niwaruka");
        if (u == null) {
            u = new User();
            u.setName("niwaruka");
            u.setEmail("sojo.izumi.lab@gmail.com");
            u.setUserName("マスターにわるか");
            u.setPassword("asa;+8weraDDe");
            u.setTags(Arrays.asList(new Key[] { ts.getPublicTag("niwaruka") }));
            saveUser(u);
            u = getUser("niwaruka");
        }

        return u;
    }
}
