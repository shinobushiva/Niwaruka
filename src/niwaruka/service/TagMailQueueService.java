package niwaruka.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import niwaruka.meta.TagMailQueueEntryMeta;
import niwaruka.model.Tag;
import niwaruka.model.TagMail;
import niwaruka.model.TagMailQueueEntry;
import niwaruka.model.Tweet;
import niwaruka.model.User;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;

import twitter4j.internal.logging.Logger;

public class TagMailQueueService {

    Logger LOG = Logger.getLogger(TagMailQueueService.class);

    public void enqueue(TagMail tagMail, Tweet t) {
        TagMailQueueEntry e = new TagMailQueueEntry();
        e.getTagMailRef().setModel(tagMail);
        e.getTweetRef().setModel(t);
        e.setTime(new Date());

        GlobalTransaction tx = Datastore.beginGlobalTransaction();
        Datastore.put(e);
        tx.commit();
    }

    public void dequeueAndSend(int num) {
        List<TagMailQueueEntry> list =
            Datastore
                .query(TagMailQueueEntry.class)
                .sort(TagMailQueueEntryMeta.get().time.desc)
                .limit(num)
                .asList();

        for (TagMailQueueEntry tm : list) {
            try {
                sendEmail(tm.getTagMailRef().getModel(), tm
                    .getTweetRef()
                    .getModel());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } finally {
                GlobalTransaction gtx = Datastore.beginGlobalTransaction();
                Datastore.delete(tm.getKey());
                gtx.commit();
            }
        }
    }

    private void sendEmail(TagMail tagMail, Tweet t)
            throws UnsupportedEncodingException {

        User u = Datastore.get(User.class, tagMail.getOwner());

        InternetAddress toAddress =
            new InternetAddress(
                tagMail.getEmail(),
                u.getUserName(),
                "ISO-2022-JP"); // 送信相手

        // niwaruka_100_300_400@sojotwitapps.appspotmail.com

        // niwaruka_userId_tagId_tagId_...@sojotwitapps.appspotmail.com

        String tts = "niwaruka_" + u.getKey().getId();
        {
            List<Tag> tls = tagMail.getTagList();
            for (Tag tag : tls) {
                if (!Tag.TagAccess.secretTag.name().equals(tag.getAccess())
                    || u.getKey().equals(
                        tag.getCreatorRef().getModel().getUserKey())) {
                    tts += "_" + tag.getKey().getId();
                }
            }
        }

        String from = tts + "@sojotwitapps.appspotmail.com";
        InternetAddress fromAddress =
            new InternetAddress(from, "にわるか", "ISO-2022-JP"); // 送信元（自分）

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(fromAddress);
            message.addRecipient(Message.RecipientType.TO, toAddress);
            String title = t.getUserDataRef().getModel().getUserName() + "の発言";
            message.setSubject(title, "ISO-2022-JP");

            StringBuilder sb = new StringBuilder();

            sb.append(title);
            sb.append("\n");
            sb.append("\n");
            sb.append(t.getContent());
            sb.append("\n");
            List<String> als = new ArrayList<String>();
            List<Tag> tls = t.getTagList();
            for (Tag tag : tls) {
                if (!Tag.TagAccess.secretTag.name().equals(tag.getAccess())
                    || u.getKey().equals(tag.getCreatorRef().getKey())) {
                    als.add(tag.getTag());
                }
            }
            sb.append("タグ : " + als);
            sb.append("\n");
            sb.append("\n");
            sb.append("***このメールに返信すると「にわるか」に投稿できます ***\n");
            sb.append("***450文字か１行以上の空白行でメッセージが終了と見なされます ***\n");
            sb.append("\n");
            sb
                .append("---------------------------------------------------------------------\n");

            sb.append("\n");
            sb.append(u.getUserName() + "様\n");
            sb.append("にわるかをご利用いただきありがとうございます。\n");
            sb.append("----\n");
            sb.append("\"にわるか\"は崇城大学和泉研究室で開発されています\n");
            sb.append("http://sojotwitapps.appspot.com/\n");
            sb.append("http://www.cis.sojo-u.ac.jp/~izumi\n");
            message.setText(sb.toString(), "ISO-2022-JP");

            Transport.send(message);
            LOG.info("Tweet Mail has sent to : " + toAddress);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
