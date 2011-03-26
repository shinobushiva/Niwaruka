package niwaruka.controller.niwaruka.login;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import niwaruka.model.User;
import niwaruka.service.LoginService;
import niwaruka.service.TagService;
import niwaruka.service.TwitterService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.google.appengine.api.datastore.Key;

public class UserRegistrationController extends Controller {

    LoginService ls = new LoginService();
    TwitterService ts = new TwitterService();
    TagService tss = new TagService();

    @Override
    public Navigation run() throws Exception {

        UserForm f = sessionScope("registratingUser");

        User u = new User();
        u.setName(f.getName());
        u.setPassword(f.getPasswd1());
        u.setEmail(f.getEmail());
        u.setUserName(f.getUserName());
        u.setTags(Arrays.asList(new Key[] { tss.getPublicTag("niwaruka") }));

        ls.saveUser(u);
        ts.fetchHashTags(ls, true);

        sendMail(f, u);

        return forward("userRegistration.jsp");
    }

    private void sendMail(UserForm f, User u)
            throws UnsupportedEncodingException {
        InternetAddress ToAddress =
            new InternetAddress(f.getEmail(), f.getUserName(), "ISO-2022-JP"); // 送信相手
        InternetAddress FromAddress =
            new InternetAddress(
                "sojo.izumi.lab@gmail.com",
                "崇城大学 和泉研究室",
                "ISO-2022-JP"); // 送信元（自分）

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(FromAddress);
            message.addRecipient(Message.RecipientType.TO, ToAddress);
            message.setSubject("にわるかにご登録ありがとうございます", "ISO-2022-JP");

            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            sb.append("にわるかにご登録いただいてありがとうございます。\n");
            sb.append("\n");
            sb.append("にわるかは同じタグを持っている人達の間でつぶやきを共有できるサービスです。\n");
            sb.append("\n");
            sb
                .append("---------------------------------------------------------------------\n");
            sb.append("アカウント情報\n");
            sb
                .append("---------------------------------------------------------------------\n");
            sb.append("\n");
            sb.append("ご登録いただいたアカウントの情報は次の通りです\n");
            sb.append("にわるか ID：").append(" ").append(u.getName());
            sb.append("\n");
            sb.append("ユーザ名：").append(" ").append(u.getUserName());
            sb.append("\n\n");
            sb.append("----\n");
            sb.append("\"にわるか\"は崇城大学和泉研究室で開発されています\n");
            sb.append("http://sojotwitapps.appspot.com/\n");
            sb.append("http://www.cis.sojo-u.ac.jp/~izumi\n");
            message.setText(sb.toString(), "ISO-2022-JP");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
