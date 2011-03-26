package niwaruka.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import niwaruka.meta.TagTweetMeta;
import niwaruka.model.Tag;
import niwaruka.model.TagTweet;
import niwaruka.model.Tweet;
import niwaruka.model.User;
import niwaruka.model.UserData;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;
import org.slim3.memcache.Memcache;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Builder;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

/*
 * つぶやきを保存したり取り出したりするクラス。
 */
public class TweetService {

    Logger LOG = Logger.getLogger(TweetService.class.getName());

    TwitterService ts = new TwitterService();

    /**
     * @deprecated use addTweet(String content, User user, LoginService
     *             ls,List<Tag> tags)
     * @param content
     * @param user
     * @param ls
     */
    public void addTweet(String content, User user, LoginService ls) {

        List<Key> tagKeys = new ArrayList<Key>(user.getTags());
        tagKeys.removeAll(user.getDisabledTags());

        List<Tag> tags = new ArrayList<Tag>();
        for (Key tagKey : tagKeys) {
            Tag tag = Datastore.get(Tag.class, tagKey);
            tags.add(tag);
        }

        this.addTweet(content, user, ls, tags);

    }

    public void addTweet(String content, User user, LoginService ls,
            List<Tag> tags) {

        UserData userData = ls.getUserData(user);

        Date time = new Date();

        Tweet tweet = new Tweet();
        tweet.setContent(content);
        tweet.setTime(time);
        tweet.getUserDataRef().setModel(userData);

        tweet.setTagList(tags);

        this.addTweet(tweet, user, ls);

    }

    // XXX:Parent existing TagTweet - Tweet relationships in the Datastore.
    public void deleteTweet(Tweet t, UserData u) {
        if (t.getUserDataRef().getModel().equals(u)) {
            List<Key> keys =
                Datastore
                    .query(TagTweet.class)
                    .filter(TagTweetMeta.get().tweetRef.equal(t.getKey()))
                    .asKeyList();
            for (Key key : keys) {
                GlobalTransaction gtx = Datastore.beginGlobalTransaction();
                Datastore.delete(key);
                gtx.commit();
            }

            GlobalTransaction gtx = Datastore.beginGlobalTransaction();
            Datastore.delete(t.getKey());
            gtx.commit();
        }
    }

    synchronized public void addTweet(Tweet tweet, User user, LoginService ls) {

        LOG.info("AddTweet : " + tweet);

        GlobalTransaction gtx = Datastore.beginGlobalTransaction();
        Key tweetKey = Datastore.put(tweet);
        gtx.commit();
        tweet = Datastore.get(Tweet.class, tweetKey);

        String toTwitter = "";
        boolean postToTwitter = false;

        for (Tag tag : tweet.getTagList()) {
            TagTweet tt = new TagTweet();
            tt.setTime(tweet.getTime());
            tt.getTweetRef().setKey(tweetKey);
            tt.setTagKey(tag.getKey());

            // キーに親子関係を設定
            Key ttKey = Datastore.allocateId(tweetKey, TagTweet.class);
            tt.setKey(ttKey);

            gtx = Datastore.beginGlobalTransaction();
            Datastore.put(tt);
            gtx.commit();

            if (tag.getTag().startsWith("#")) {
                toTwitter += tag.getTag() + " ";
                postToTwitter = true;
            }
        }

        // Twitterからフェッチしたメッセージやハッシュタグを持たないメッセージはTwitterに投げない
        if (!TwitterService.TWITTER_CLIENT.equals(tweet.getClient())
            && postToTwitter) {
            toTwitter += tweet.getContent();
            if (toTwitter.length() > 140) {
                toTwitter = toTwitter.substring(0, 140);
            }
            ts.tweet(toTwitter, user);
        }

        gtx = Datastore.beginGlobalTransaction();
        Key key = Datastore.put(tweet);
        gtx.commit();

        // TODO:Push送信
        // String string = (String) Memcache.get("clientId");
        // System.out.println("check : " + string);
        // if (string != null) {
        // ChannelServiceFactory.getChannelService().sendMessage(
        // new ChannelMessage(string, tweet.getContent()));
        // }

        // Twitterからフェッチしたメッセージはメールしない。
        if (!TwitterService.TWITTER_CLIENT.equals(tweet.getClient())) {
            QueueFactory.getQueue("mail-queue").add(
                Builder
                    .withUrl("/niwaruka/tagMailSend")
                    .param("tweetKey", Datastore.keyToString(key))
                    .method(Method.POST));
        }

    }

    public void addClientId(String cc) {
        @SuppressWarnings("unchecked")
        List<String> clientIds = (List<String>) Memcache.get("clientIds");
        if (clientIds == null) {
            clientIds = new ArrayList<String>();
            Memcache.put("clientIds", cc);
        }
    }

    public List<Tweet> fetch(User u) {
        return fetchWithTagList(u, u.getTags());
    }

    public int countWithTagList(User u, List<Key> tags, Date d) {

        TagTweetMeta ttm = TagTweetMeta.get();
        int num =
            Datastore
                .query(TagTweet.class)
                .filter(ttm.tagKey.in(tags))
                .filter(ttm.time.greaterThan(d))
                .limit(50)
                .count();
        return num;
    }

    /**
     * 指定したタグに関するつぶやきを取得します
     * 
     * @param tagKey
     * @param date
     * @return
     */
    public List<Tweet> fetchWithTagOlderThan(Key tagKey, Date date) {
        List<Tweet> list = new ArrayList<Tweet>();

        TagTweetMeta ttm = TagTweetMeta.get();
        List<TagTweet> tts =
            Datastore
                .query(TagTweet.class)
                .sort(ttm.time.desc)
                .sort(ttm.time.getName(), SortDirection.DESCENDING)
                .filter(ttm.tagKey.equal(tagKey))
                .filter(ttm.time.lessThan(date))
                .limit(50)
                .asList();

        List<Key> tk = new ArrayList<Key>();
        for (TagTweet tagTweet : tts) {
            Key tweetKey = tagTweet.getTweetRef().getKey();
            if (!tk.contains(tweetKey)) {
                tk.add(tweetKey);
            }
        }
        list = Datastore.get(Tweet.class, tk);

        return list;
    }

    public List<Tweet> fetchWithTagListOlderThan(User u, List<Key> tags,
            Date date) {
        List<Tweet> list = new ArrayList<Tweet>();

        if (tags == null || tags.isEmpty()) {
            return list;
        }

        TagTweetMeta ttm = TagTweetMeta.get();
        List<TagTweet> tts =
            Datastore
                .query(TagTweet.class)
                .sort(ttm.time.desc)
                .sort(ttm.time.getName(), SortDirection.DESCENDING)
                .filter(ttm.tagKey.in(tags))
                .filter(ttm.time.lessThan(date))
                .limit(50)
                .asList();

        List<Key> tk = new ArrayList<Key>();
        for (TagTweet tagTweet : tts) {
            Key tweetKey = tagTweet.getTweetRef().getKey();
            if (!tk.contains(tweetKey)) {
                tk.add(tweetKey);
            }
        }
        list = Datastore.get(Tweet.class, tk);

        return list;
    }

    public List<Tweet> fetchWithTagList(User u, List<Key> tags, Date newerThan) {
        List<Tweet> list = new ArrayList<Tweet>();

        if (tags == null || tags.isEmpty()) {
            return list;
        }

        TagTweetMeta ttm = TagTweetMeta.get();
        List<TagTweet> tts =
            Datastore
                .query(TagTweet.class)
                .sort(ttm.time.desc)
                .sort(ttm.time.getName(), SortDirection.DESCENDING)
                .filter(ttm.tagKey.in(tags))
                .filter(ttm.time.greaterThan(newerThan))
                .limit(50)
                .asList();

        List<Key> tk = new ArrayList<Key>();
        for (TagTweet tagTweet : tts) {
            Key tweetKey = tagTweet.getTweetRef().getKey();
            if (!tk.contains(tweetKey)) {
                tk.add(tweetKey);
            }
        }
        list = Datastore.get(Tweet.class, tk);

        return list;
    }

    // public List<Tweet> fetchWithTagList2(User u, List<Key> tags, Date d) {
    // List<Tweet> list = new ArrayList<Tweet>();
    // // // 使われていないTagTweet
    // // List<Key> dead = new ArrayList<Key>();
    //
    // for (Key tagKey : tags) {
    // List<TagTweet> tts =
    // Datastore
    // .query(TagTweet.class)
    // .sort(
    // TagTweetMeta.get().time.getName(),
    // SortDirection.DESCENDING)
    // .filter(TagTweetMeta.get().tagRef.equal(tagKey))
    // .filter(TagTweetMeta.get().time.greaterThan(d))
    // .limit(50)
    // .asList();
    //
    // for (TagTweet tagTweet : tts) {
    // Key tweetKey = tagTweet.getTweetRef().getKey();
    // try {
    // Tweet tweet = Datastore.get(Tweet.class, tweetKey);
    // if (!list.contains(tweet)) {
    // list.add(tweet);
    // // System.out.println(tweet.getTagList());
    // } else {
    // tweet = list.get(list.indexOf(tweet));
    // }
    //
    // } catch (EntityNotFoundRuntimeException e) {
    // // 対応するTweetが存在しない場合、ゴミとしてマーク
    // LOG.warning(e.toString());
    // // dead.add(tweetKey);
    // }
    // }
    // }
    //
    // Collections.sort(list, new Comparator<Tweet>() {
    // @Override
    // public int compare(Tweet o1, Tweet o2) {
    // return o2.getTime().compareTo(o1.getTime());
    // }
    // });
    //
    // // 使われていないTagTweetを掃除
    // // for (Key key : dead) {
    // // List<Key> kl2 =
    // // Datastore
    // // .query(TagTweet.class)
    // // .filter(TagTweetMeta.get().tweetRef.equal(key))
    // // .asKeyList();
    // // for (Key key2 : kl2) {
    // // GlobalTransaction tx = Datastore.beginGlobalTransaction();
    // // Datastore.delete(key2);
    // // tx.commit();
    // // }
    // // }
    //
    // return list;
    // }

    public List<Tweet> fetchWithTagList(User u, List<Key> tags) {

        return fetchWithTagList(u, tags, new Date(0));

    }

    public List<Tweet> fetchSamples(int lim) {

        LOG.info("lim=" + lim);

        List<Tweet> list = Datastore.query(Tweet.class).limit(lim).asList();

        Collections.sort(list, new Comparator<Tweet>() {
            @Override
            public int compare(Tweet o1, Tweet o2) {
                return o2.getTime().compareTo(o1.getTime());
            }
        });

        return list;
    }

    // 指定日移行のTweetをフェッチ(いらないかな？)
    // public List<Tweet> fetch(User u, Date d) {
    //
    // List<Tweet> list = new ArrayList<Tweet>();
    //
    // List<Key> tags = u.getTags();
    // for (Key tagKey : tags) {
    //
    // List<TagTweet> tts =
    // Datastore
    // .query(TagTweet.class)
    // .filter(TagTweetMeta.get().tagRef.equal(tagKey))
    // .filter(TagTweetMeta.get().time.greaterThan(d))
    // .limit(100)
    // .asList();
    // for (TagTweet tagTweet : tts) {
    // Key tweetKey = tagTweet.getTweetRef().getKey();
    // Tweet tweet = Datastore.get(Tweet.class, tweetKey);
    //
    // if (!list.contains(tweet)) {
    // list.add(tweet);
    // }
    // }
    // }
    // Collections.sort(list, new Comparator<Tweet>() {
    // @Override
    // public int compare(Tweet o1, Tweet o2) {
    // return o2.getTime().compareTo(o1.getTime());
    // }
    // });
    //
    // // System.out.println(list);
    // return list;
    // }

}
