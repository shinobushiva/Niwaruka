package niwaruka.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import niwaruka.meta.TagMeta;
import niwaruka.meta.TagPermissionRequestMeta;
import niwaruka.model.FetchedHashTag;
import niwaruka.model.Tag;
import niwaruka.model.TagPermissionRequest;
import niwaruka.model.User;
import niwaruka.model.UserData;
import niwaruka.model.Tag.TagAccess;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

public class TagService {

    /**
     * タグを検索します。
     * 
     * @param query
     * @param num
     * @param ud
     * @return
     */
    public List<Tag> search(String query, int num, UserData ud) {
        List<Tag> tags =
            Datastore
                .query(Tag.class)
                .filter(TagMeta.get().tag.startsWith(query))
                .filter(TagMeta.get().creatorRef.equal(ud.getKey()))
                .limit(num)
                .asList();
        tags.addAll(Datastore
            .query(Tag.class)
            .filter(TagMeta.get().tag.startsWith(query))
            .limit(num * 5)
            .asList());
        List<Tag> whiteList = new ArrayList<Tag>();
        for (Tag tag : tags) {
            if (tag.getAccess().equals(TagAccess.secretTag.name())
                && !tag.getCreatorRef().getKey().equals(ud.getKey())) {
            } else if (whiteList.contains(tag)) {
            } else {
                whiteList.add(tag);
            }
            if (whiteList.size() >= num) {
                break;
            }
        }
        return whiteList;
    }

    public List<Tag> fetch(User u) {
        return Datastore.get(Tag.class, u.getTags()); // Tagオブジェクトのリスト
    }

    public void delete(Tag t) {

    }

    public void removeTag(Tag tag, User u) {
        u.getTags().remove(tag.getKey());
        GlobalTransaction gtx = Datastore.beginGlobalTransaction();
        Datastore.put(u);
        gtx.commit();

        tag.setUserCount(tag.getUserCount() - 1);
        gtx = Datastore.beginGlobalTransaction();
        Datastore.put(tag);
        gtx.commit();
    }

    public void addTag(Tag tag, User u, LoginService ls) {
        UserData ud = ls.getUserData(u);
        if (!ud.getKey().equals(tag.getCreatorRef().getKey())
            && tag.isRequirePermission()) {
            requestTagPermission(tag, u);
        } else {
            u.getTags().add(tag.getKey());
            GlobalTransaction gtx = Datastore.beginGlobalTransaction();
            Datastore.put(u);
            gtx.commit();

            tag.setUserCount(tag.getUserCount() + 1);
            gtx = Datastore.beginGlobalTransaction();
            Datastore.put(tag);
            gtx.commit();
        }
    }

    public void toggleTag(Tag tag, User u, LoginService ls) {
        if (u.getTags().contains(tag.getKey())) {
            removeTag(tag, u);
        } else {
            addTag(tag, u, ls);
        }
    }

    public void requestTagPermission(Tag tag, User requester) {

        TagPermissionRequestMeta tprm = TagPermissionRequestMeta.get();
        TagPermissionRequest tpr =
            Datastore
                .query(TagPermissionRequest.class)
                .filter(
                    tprm.owner.equal(tag
                        .getCreatorRef()
                        .getModel()
                        .getUserKey()))
                .filter(tprm.requester.equal(requester.getKey()))
                .filter(tprm.state.equal(TagPermissionRequest.STATE_REQUESTED))
                .filter(tprm.tag.equal(tag.getKey()))
                .asSingle();
        if (tpr == null) {
            tpr = new TagPermissionRequest();
            tpr.getOwner().setKey(tag.getCreatorRef().getModel().getUserKey());
            tpr.getTag().setModel(tag);
            tpr.getRequester().setModel(requester);
        }
        tpr.setTime(new Date());

        GlobalTransaction gtx = Datastore.beginGlobalTransaction();
        Datastore.put(tpr);
        gtx.commit();

    }

    /**
     * IDからタグを取得します。
     * 
     * @param id
     * @return
     */
    public Tag loadTag(long id) {
        Key k = Datastore.createKey(Tag.class, id);
        return Datastore.get(Tag.class, k);
    }

    /**
     * 文字列から公開タグを取得します。
     * 
     * @param id
     * @return
     */
    public Key getPublicTag(String str) {
        Tag t =
            Datastore
                .query(Tag.class)
                .filter(TagMeta.get().tag.equal(str))
                .filter(TagMeta.get().access.equal(TagAccess.publicTag.name()))
                .asSingle();

        if (t == null) {
            return saveTag(null, "niwaruka", TagAccess.publicTag);
        } else {
            return t.getKey();
        }
    }

    public Key saveTag(UserData u, String tag, TagAccess ta) {

        Tag t = new Tag();
        t.setTag(tag);

        Tag t2 = null;
        Key tagKey = null;

        // secret タグ
        if (ta.equals(TagAccess.secretTag)) {
            t2 =
                Datastore
                    .query(Tag.class)
                    .filter(TagMeta.get().tag.equal(tag))
                    .filter(
                        TagMeta.get().access.equal(Tag.TagAccess.secretTag
                            .name()))
                    .filter(TagMeta.get().creatorRef.equal(u.getKey()))
                    .asSingle();
            if (t2 == null) {

                t.getCreatorRef().setKey(u.getKey());
                t.setRequirePermission(true);
                t.setAccess(Tag.TagAccess.secretTag.name());
                Transaction tx = Datastore.beginTransaction();
                tagKey = Datastore.put(t);
                tx.commit();
            } else {
                tagKey = t2.getKey();
            }
        } else if (ta.equals(TagAccess.sharedTag)) {
            t2 =
                Datastore
                    .query(Tag.class)
                    .filter(TagMeta.get().tag.equal(tag))
                    .filter(
                        TagMeta.get().access.equal(Tag.TagAccess.sharedTag
                            .name()))
                    .filter(TagMeta.get().creatorRef.equal(u.getKey()))
                    .asSingle();
            if (t2 == null) {
                t.getCreatorRef().setKey(u.getKey());
                t.setRequirePermission(true);
                t.setAccess(Tag.TagAccess.sharedTag.name());
                Transaction tx = Datastore.beginTransaction();
                tagKey = Datastore.put(t);
                tx.commit();
            } else {
                tagKey = t2.getKey();
            }
        } else {
            t2 =
                Datastore
                    .query(Tag.class)
                    .filter(TagMeta.get().tag.equal(tag))
                    .asSingle();
            if (t2 == null) {
                Transaction tx = Datastore.beginTransaction();
                tagKey = Datastore.put(t);
                tx.commit();
                registrateHashTagFetched(t, tagKey);
            } else {
                tagKey = t2.getKey();
            }
        }
        return tagKey;
    }

    private void registrateHashTagFetched(Tag t, Key tagKey) {

        String tag = t.getTag();

        // Twitterのハッシュタグ
        if (tag.startsWith("#")) {
            FetchedHashTag fht = new FetchedHashTag();
            fht.setHashTag(tag);
            fht.getTag().setKey(t.getKey());
            GlobalTransaction gtx = Datastore.beginGlobalTransaction();
            Datastore.put(fht);
            gtx.commit();
        }

    }
}
