package niwaruka.controller.niwaruka;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import niwaruka.meta.TagMailMeta;
import niwaruka.model.Tag;
import niwaruka.model.TagMail;
import niwaruka.model.Tweet;
import niwaruka.service.LoginService;
import niwaruka.service.TagMailQueueService;
import niwaruka.service.TwitterService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import twitter4j.internal.logging.Logger;

import com.google.appengine.api.datastore.Key;

public class TagMailSendController extends Controller {

    TwitterService ts = new TwitterService();
    LoginService ls = new LoginService();
    TagMailQueueService tmqs = new TagMailQueueService();

    Logger LOG = Logger.getLogger(TagMailSendController.class);

    @Override
    public Navigation run() throws Exception {

        System.out.println("TagMailed:" + requestScope("tweetKey"));
        String tkey = requestScope("tweetKey");
        Key key = Datastore.stringToKey(tkey);
        Tweet tweet = Datastore.get(Tweet.class, key);

        Set<TagMail> tagMails = new HashSet<TagMail>();

        List<Tag> tl = tweet.getTagList();
        for (Tag tag : tl) {
            List<TagMail> tms =
                Datastore
                    .query(TagMail.class)
                    .filter(TagMailMeta.get().tags.in(tag.getKey()))
                    .asList();
            for (TagMail tagMail : tms) {
                if (!tagMails.contains(tagMail)) {
                    tagMails.add(tagMail);
                }
            }
        }

        for (TagMail tagMail : tagMails) {
            if (tagMail.getOption().equals("or")) {
                sendEmail(tagMail, tweet);
            } else if (tagMail.getOption().equals("and")) {
                List<Tag> al = new ArrayList<Tag>(tagMail.getTagList());
                al.removeAll(tl);
                if (al.isEmpty()) {
                    sendEmail(tagMail, tweet);
                }
            }
        }

        return null;
    }

    private void sendEmail(TagMail tagMail, Tweet t) {
        tmqs.enqueue(tagMail, t);

        // User u = Datastore.get(User.class, tagMail.getOwner());
        //
        // InternetAddress toAddress =
        // new InternetAddress(
        // tagMail.getEmail(),
        // u.getUserName(),
        // "ISO-2022-JP"); // 送信相手
        //
        // // niwaruka_100_300_400@sojotwitapps.appspotmail.com
        //
        // // niwaruka_userId_tagId_tagId_...@sojotwitapps.appspotmail.com
        //
        // String tts = "niwaruka_" + u.getKey().getId();
        // {
        // List<Tag> tls = tagMail.getTagList();
        // for (Tag tag : tls) {
        // if (!Tag.TagAccess.secretTag.name().equals(tag.getAccess())
        // || u.getKey().equals(tag.getCreatorRef().getKey())) {
        // tts += "_" + tag.getKey().getId();
        // }
        // }
        // }
        //
        // String from = tts + "@sojotwitapps.appspotmail.com";
        // InternetAddress fromAddress =
        // new InternetAddress(from, "にわるか", "ISO-2022-JP"); // 送信元（自分）
        //
        // Properties props = new Properties();
        // Session session = Session.getDefaultInstance(props, null);
        // MimeMessage message = new MimeMessage(session);
        // try {
        // message.setFrom(fromAddress);
        // message.addRecipient(Message.RecipientType.TO, toAddress);
        // String title = t.getUserDataRef().getModel().getUserName() + "の発言";
        // message.setSubject(title, "ISO-2022-JP");
        //
        // StringBuilder sb = new StringBuilder();
        //
        // sb.append(title);
        // sb.append("\n");
        // sb.append("\n");
        // sb.append(t.getContent());
        // sb.append("\n");
        // List<String> als = new ArrayList<String>();
        // List<Tag> tls = t.getTagList();
        // for (Tag tag : tls) {
        // if (!Tag.TagAccess.secretTag.name().equals(tag.getAccess())
        // || u.getKey().equals(tag.getCreatorRef().getKey())) {
        // als.add(tag.getTag());
        // }
        // }
        // sb.append("タグ : " + als);
        // sb.append("\n");
        // sb.append("\n");
        // sb.append("***このメールに返信すると「にわるか」に投稿できます ***\n");
        // sb.append("***450文字か１行以上の空白行でメッセージが終了と見なされます ***\n");
        // sb.append("\n");
        // sb
        // .append("---------------------------------------------------------------------\n");
        //
        // sb.append("\n");
        // sb.append(u.getUserName() + "様\n");
        // sb.append("にわるかをご利用いただきありがとうございます。\n");
        // sb.append("----\n");
        // sb.append("\"にわるか\"は崇城大学和泉研究室で開発されています\n");
        // sb.append("http://sojotwitapps.appspot.com/\n");
        // sb.append("http://www.cis.sojo-u.ac.jp/~izumi\n");
        // message.setText(sb.toString(), "ISO-2022-JP");
        //
        // Transport.send(message);
        // LOG.info("Tweet Mail has sent to : " + toAddress);
        // } catch (MessagingException e) {
        // e.printStackTrace();
        // }

    }
}
