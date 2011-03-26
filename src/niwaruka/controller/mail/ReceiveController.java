package niwaruka.controller.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import niwaruka.model.Tag;
import niwaruka.model.Tweet;
import niwaruka.model.User;
import niwaruka.model.UserData;
import niwaruka.service.LoginService;
import niwaruka.service.TweetService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

public class ReceiveController extends Controller {

    TweetService ts = new TweetService();
    LoginService ls = new LoginService();

    @Override
    public Navigation run() throws Exception {

        String address = requestScope("address");
        System.out.println(address);
        address = address.split("@")[0];
        if (address.startsWith("niwaruka_")) {
            niwarukaReply(address);
        } else if (address.startsWith("niwaruka")) {
            niwarukaSend(address);
        }

        return null;
    }

    private void niwarukaReply(String address) throws MessagingException,
            IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message =
            new MimeMessage(session, request.getInputStream());

        String sender = message.getFrom()[0].toString();

        String niwaruka = "";
        Object obj = message.getContent();
        if (obj instanceof Multipart) {
            Multipart content = (Multipart) obj;
            BodyPart bodyPart = content.getBodyPart(0);
            niwaruka = (String) bodyPart.getContent();
        } else if (obj instanceof String) {
            niwaruka = (String) obj;
        }
        niwaruka = niwaruka.split("\n\n")[0];
        if (niwaruka.length() > 450) {
            niwaruka = niwaruka.substring(0, 450);
        }
        System.out.println(niwaruka);

        String[] split = address.split("_");

        List<Tag> tl = new ArrayList<Tag>();
        for (int i = 2; i < split.length; i++) {
            Long l = Long.parseLong(split[i]);
            Key key = Datastore.createKey(Tag.class, l);
            Tag tag = Datastore.get(Tag.class, key);
            tl.add(tag);
        }

        Long userId = Long.parseLong(split[1]);
        Key key = Datastore.createKey(User.class, userId);
        User user = Datastore.get(User.class, key);
        UserData userData = ls.getUserData(user);

        System.out.println(sender);

        Tweet t = new Tweet();
        t.setContent(niwaruka);
        t.setClient("メール");
        t.setTagList(tl);
        t.setTime(new Date());
        t.getUserDataRef().setModel(userData);

        ts.addTweet(t, user, ls);
    }

    private void niwarukaSend(String address) throws MessagingException,
            IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message =
            new MimeMessage(session, request.getInputStream());

        Address addr = message.getFrom()[0];
        System.out.println(addr.getClass());
        String sender = addr.toString();
        if (addr instanceof InternetAddress) {
            sender = ((InternetAddress) addr).getAddress();
        }
        String niwaruka = "";

        Object obj = message.getContent();
        if (obj instanceof Multipart) {
            Multipart content = (Multipart) obj;
            BodyPart bodyPart = content.getBodyPart(0);
            niwaruka = (String) bodyPart.getContent();
        } else if (obj instanceof String) {
            niwaruka = (String) obj;
        }
        niwaruka = niwaruka.split("\n\n")[0];
        if (niwaruka.length() > 450) {
            niwaruka = niwaruka.substring(0, 450);
        }
        System.out.println(niwaruka);
        System.out.println(sender);

        User user = ls.getUserByEmail(sender);
        System.out.println(user);
        if (user == null) {
            return;
        }

        UserData userData = ls.getUserData(user);

        List<Key> tags = new ArrayList<Key>(user.getTags());
        tags.removeAll(user.getDisabledTags());

        List<Tag> list = Datastore.get(Tag.class, tags);

        Tweet t = new Tweet();
        t.setContent(niwaruka);
        t.setClient("メール");
        t.setTagList(list);
        t.setTime(new Date());
        t.getUserDataRef().setModel(userData);

        ts.addTweet(t, user, ls);
    }
}
