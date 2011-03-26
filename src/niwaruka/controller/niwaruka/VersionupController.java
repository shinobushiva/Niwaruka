package niwaruka.controller.niwaruka;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class VersionupController extends Controller {

    @Override
    public Navigation run() throws Exception {

        System.out.println("Versionup!");

        // v1();
        // v2();
        // v3();
        // v4();
        // v5();
        // v6();
        // v7();
        // v8();
        // v9();
        // v10();

        return null;
    }

    // private void v10() {
    // List<Tag> asList = Datastore.query(Tag.class).asList();
    // for (Tag tag : asList) {
    // if (tag.getAccess() == null) {
    // tag.setAccess(TagAccess.publicTag.name());
    // }
    //
    // GlobalTransaction gtx = Datastore.beginGlobalTransaction();
    // Datastore.put(tag);
    // gtx.commit();
    // }
    // }

    // private void v9() {
    // List<Tag> asList = Datastore.query(Tag.class).asList();
    // for (Tag tag : asList) {
    // if (tag.getTag().startsWith("*") || tag.getTag().startsWith("-")) {
    // tag.setTag(tag.getTag().substring(1));
    // }
    //
    // GlobalTransaction gtx = Datastore.beginGlobalTransaction();
    // Datastore.put(tag);
    // gtx.commit();
    // }
    // }

    // private void v8() {
    // DatastoreService ds = new DatastoreService();
    //
    // List<Tag> tags = Datastore.query(Tag.class).asList();
    // for (Tag tag : tags) {
    // if (tag.getCreatorRef().getKey() != null) {
    // UserData udd =
    // Datastore
    // .query(UserData.class)
    // .filter(
    // UserDataMeta.get().userKey.equal(tag
    // .getCreatorRef()
    // .getKey()))
    // .asSingle();
    //
    // if (udd != null) {
    // try {
    // tag.getCreatorRef().setKey(udd.getKey());
    // ds.put(tag);
    // } catch (EntityNotFoundRuntimeException e) {
    // e.printStackTrace();
    //
    // }
    // }
    // }
    // }
    // }

    // private void v7() {
    // DatastoreService ds = new DatastoreService();
    //
    // List<User> users = Datastore.query(User.class).asList();
    // for (User user : users) {
    // UserData ud = new UserData();
    //
    // UserData udd =
    // Datastore
    // .query(UserData.class)
    // .filter(UserDataMeta.get().userKey.equal(user.getKey()))
    // .asSingle();
    // if (udd == null) {
    // BeanUtil.copy(user, ud);
    // ud.setKey(null);
    // ud.setUserKey(user.getKey());
    // ud.setVersion(null);
    // ds.put(ud);
    // }
    // }
    //
    // List<Tag> tags = Datastore.query(Tag.class).asList();
    // for (Tag tag : tags) {
    // if (tag.getCreatorRef().getKey() != null) {
    // UserData udd =
    // Datastore
    // .query(UserData.class)
    // .filter(
    // UserDataMeta.get().userKey.equal(tag.getOwner()))
    // .asSingle();
    //
    // try {
    // tag.getCreatorRef().setKey(udd.getKey());
    // ds.put(tag);
    // } catch (EntityNotFoundRuntimeException e) {
    //
    // }
    // }
    // }
    //
    // List<Tweet> tweets = Datastore.query(Tweet.class).asList();
    // for (Tweet tweet : tweets) {
    // Key key = tweet.getUserRef().getKey();
    //
    // UserData udd =
    // Datastore
    // .query(UserData.class)
    // .filter(UserDataMeta.get().userKey.equal(key))
    // .asSingle();
    // tweet.getUserDataRef().setModel(udd);
    // ds.put(tweet);
    // }
    //
    // }

    // private void v5() {
    // DatastoreService ds = new DatastoreService();
    //
    // List<Tag> tags = Datastore.query(Tag.class).asList();
    // for (Tag tag : tags) {
    // if (tag.getOwner() != null) {
    // User user = null;
    // try {
    // user = Datastore.get(User.class, tag.getOwner());
    // } catch (EntityNotFoundRuntimeException e) {
    //
    // }
    // if (user != null) {
    // tag.getCreator().setModel(user);
    // ds.put(tag);
    // }
    // }
    //
    // }
    //
    // }

    // private void v4() {
    // DatastoreService ds = new DatastoreService();
    //
    // List<Tag> tags = Datastore.query(Tag.class).asList();
    // for (Tag tag : tags) {
    // if (tag.getAccess() == null) {
    // tag.setAccess(Tag.TagAccess.publicTag.name());
    // ds.put(tag);
    // }
    //
    // }
    //
    // }

    // private void v3() {
    // List<Tag> asList = Datastore.query(Tag.class).asList();
    // for (Tag tag : asList) {
    // if (tag.getTag().startsWith("-")) {
    // tag.setTag(Tag.TagAccess.secretTag.name());
    // }
    // if (tag.getTag() == null) {
    // tag.setTag(Tag.TagAccess.publicTag.name());
    // }
    // }
    // }

    // private void v2() {
    // List<TagTweet> ts = Datastore.query(TagTweet.class).asList();
    // for (TagTweet tagTweet : ts) {
    // tagTweet.setTagKey(tagTweet.getTagRef().getKey());
    // GlobalTransaction gtx = Datastore.beginGlobalTransaction();
    // Datastore.put(tagTweet);
    // gtx.commit();
    // }
    // }

    // private void v1() {
    // List<Tweet> ts = Datastore.query(Tweet.class).asList();
    //
    // for (Tweet tweet : ts) {
    // List<TagTweet> tt =
    // Datastore
    // .query(TagTweet.class)
    // .filter(TagTweetMeta.get().tweetRef.equal(tweet.getKey()))
    // .asList();
    //
    // tweet.setTagList(new ArrayList<Tag>());
    // for (TagTweet tagTweet : tt) {
    // tweet.getTagList().add(tagTweet.getTagRef().getModel());
    // }
    //
    // GlobalTransaction gtx = Datastore.beginGlobalTransaction();
    // Datastore.put(tweet);
    // gtx.commit();
    // }
    // }

}
