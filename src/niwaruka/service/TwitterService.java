package niwaruka.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import niwaruka.ApplicationConstants;
import niwaruka.meta.FetchedHashTagMeta;
import niwaruka.meta.TagTweetMeta;
import niwaruka.meta.TweetMeta;
import niwaruka.meta.TwitterTokenMeta;
import niwaruka.model.FetchedHashTag;
import niwaruka.model.Tag;
import niwaruka.model.TagTweet;
import niwaruka.model.Tweet;
import niwaruka.model.TwitterToken;
import niwaruka.model.User;
import niwaruka.model.UserData;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;

import twitter4j.Query;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import twitter4j.http.AccessToken;
import twitter4j.http.OAuthAuthorization;

import com.google.appengine.api.datastore.Key;

/**
 * Twitterとの連携を扱うサービスです
 * 
 * @author shiva
 * 
 */
public class TwitterService {

    public static String TWITTER_CLIENT = "Twitter";

    public void connectToTwitter(AccessToken accessToken, String user) {
        // TokenオブジェクトにAccessToken/SecretとtwitterIDとuserIDを格納し保存

        TwitterToken token = getTwitterToken(user);
        if (token == null) {
            token = new TwitterToken();
        }
        token.setAccessToken(accessToken.getToken());
        token.setAccessSecret(accessToken.getTokenSecret());
        token.setTwitterId(accessToken.getScreenName());
        token.setUserId(user);

        GlobalTransaction tx = Datastore.beginGlobalTransaction();
        Datastore.put(token);
        tx.commit();
    }

    public TwitterToken getTwitterToken(String user) {
        TwitterToken asSingle =
            Datastore
                .query(TwitterToken.class)
                .filter(TwitterTokenMeta.get().userId.equal(user))
                .asSingle();

        return asSingle;
    }

    public void removeTwitterConnection(String user) {
        TwitterToken tt = getTwitterToken(user);
        if (tt != null) {
            GlobalTransaction tx = Datastore.beginGlobalTransaction();
            Datastore.delete(tt.getKey());
            tx.commit();
        }

        List<Key> kl =
            Datastore
                .query(Tweet.class)
                .filter(TweetMeta.get().client.equal(TWITTER_CLIENT))
                .asKeyList();
        for (Key key : kl) {

            List<Key> kl2 =
                Datastore
                    .query(TagTweet.class)
                    .filter(TagTweetMeta.get().tweetRef.equal(key))
                    .asKeyList();
            for (Key key2 : kl2) {
                GlobalTransaction tx = Datastore.beginGlobalTransaction();
                Datastore.delete(key2);
                tx.commit();
            }

            GlobalTransaction tx = Datastore.beginGlobalTransaction();
            Datastore.delete(key);
            tx.commit();
        }
    }

    public void fetchFromTwitter(String user, TweetService ts, LoginService ls) {
        TwitterToken tt = getTwitterToken(user);
        if (tt == null) {
            return;
        }

        Configuration conf = ConfigurationContext.getInstance();
        OAuthAuthorization oa = new OAuthAuthorization(conf);
        AccessToken accessToken =
            new AccessToken(tt.getAccessToken(), tt.getAccessSecret());
        oa.setOAuthAccessToken(accessToken);

        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance(oa);

        User u = ls.getUser(user);
        UserData userData = ls.getUserData(u);
        try {
            ResponseList<Status> homeTimeline = twitter.getUserTimeline();
            Collections.reverse(homeTimeline);
            for (Status status : homeTimeline) {

                if (status.getSource().contains(ApplicationConstants.URL)) {
                    continue;
                }

                if (tt.getLastFetch() == null
                    || status.getCreatedAt().after(tt.getLastFetch())) {
                    Tweet t = new Tweet();
                    t.setContent(status.getText());
                    t.setTime(status.getCreatedAt());
                    t.setClient(TWITTER_CLIENT);
                    t.getUserDataRef().setModel(userData);

                    List<Key> tags = new ArrayList<Key>(u.getTags());
                    tags.removeAll(u.getDisabledTags());
                    for (Key tagKey : tags) {
                        t.getTagList().add(Datastore.get(Tag.class, tagKey));
                    }

                    ts.addTweet(t, u, ls);
                    tt.setLastFetch(status.getCreatedAt());
                }

            }

            GlobalTransaction tx = Datastore.beginGlobalTransaction();
            Datastore.put(tt);
            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FetchedHashTag getFetchedHashTag(String hashTag) {
        return Datastore
            .query(FetchedHashTag.class)
            .filter(FetchedHashTagMeta.get().hashTag.equal(hashTag))
            .asSingle();
    }

    public void tweet(String str, User u) {

        TwitterToken tt = getTwitterToken(u.getName());
        if (tt != null) {

            Configuration conf = ConfigurationContext.getInstance();
            OAuthAuthorization oa = new OAuthAuthorization(conf);
            AccessToken accessToken =
                new AccessToken(tt.getAccessToken(), tt.getAccessSecret());
            oa.setOAuthAccessToken(accessToken);
            TwitterFactory factory = new TwitterFactory(conf);
            Twitter twitter = factory.getInstance(oa);

            try {
                twitter.updateStatus(str);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 指定したハッシュタグに関するTweetを取得します。
     * 
     * @param hashTag
     *            ハッシュタグ
     * @param ls
     *            LoginService
     */
    public void fetchHashTagEntries(String hashTag, LoginService ls) {

        FetchedHashTag fetchedHashTag = getFetchedHashTag(hashTag);
        if (fetchedHashTag == null) {
            return;
        }

        User u = ls.getMasterUser();
        UserData userData = ls.getUserData(u);

        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();

        try {
            Query query = new Query(hashTag);
            query.rpp(100);
            if (fetchedHashTag.getLastFetch() != null) {
                query.setUntil(new SimpleDateFormat("yyyy-MM-dd")
                    .format(fetchedHashTag.getLastFetch()));
            }
            List<twitter4j.Tweet> tweets = twitter.search(query).getTweets();
            Collections.reverse(tweets);
            for (twitter4j.Tweet status : tweets) {

                if (fetchedHashTag.getLastFetch() == null
                    || status.getCreatedAt().after(
                        fetchedHashTag.getLastFetch())) {
                    Tweet tweet = new Tweet();
                    tweet.setContent(status.getText());
                    tweet.setTime(status.getCreatedAt());
                    tweet.setClient(TWITTER_CLIENT);
                    tweet.getUserDataRef().setModel(userData);
                    tweet.setTwitterUser(status.getFromUser());

                    GlobalTransaction gtx = Datastore.beginGlobalTransaction();
                    Key tweetKey = Datastore.put(tweet);
                    gtx.commit();

                    TagTweet tagTweet = new TagTweet();
                    tagTweet.setTime(tweet.getTime());
                    tagTweet.getTweetRef().setKey(tweetKey);
                    tagTweet.setTagKey(fetchedHashTag.getTag().getKey());
                    gtx = Datastore.beginGlobalTransaction();
                    Datastore.put(tagTweet);
                    gtx.commit();

                    tweet.getTagList().add(fetchedHashTag.getTag().getModel());

                    gtx = Datastore.beginGlobalTransaction();
                    Datastore.put(tweet);
                    gtx.commit();

                    fetchedHashTag.setLastFetch(status.getCreatedAt());
                }
            }

            GlobalTransaction tx = Datastore.beginGlobalTransaction();
            Datastore.put(fetchedHashTag);
            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Twitterからハッシュタグに関するつぶやきを取得します。
     * 
     * @param ls
     *            LoginService
     * @param newTagOnly
     *            今までに取得されていないタグのみ取得
     */
    public void fetchHashTags(LoginService ls, boolean newTagOnly) {

        List<FetchedHashTag> asList =
            Datastore.query(FetchedHashTag.class).asList();

        for (FetchedHashTag fetchedHashTag : asList) {
            if (newTagOnly && fetchedHashTag.getLastFetch() != null) {
                continue;
            }
            fetchHashTagEntries(fetchedHashTag.getHashTag(), ls);
        }
    }

}
