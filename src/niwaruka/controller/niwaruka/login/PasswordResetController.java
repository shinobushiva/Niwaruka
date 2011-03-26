package niwaruka.controller.niwaruka.login;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import niwaruka.model.User;
import niwaruka.service.LoginService;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

public class PasswordResetController extends Controller {

    LoginService ls = new LoginService();

    @Override
    public Navigation run() throws Exception {

        boolean sent = false;

        String name = requestScope("name");
        if (name != null && name.length() > 0) {
            User user = ls.getUser(name);
            if (user != null) {
                sendEmail(user);
                sent = true;
            }

        } else {
            String email = requestScope("email");
            if (email != null && email.length() > 0) {
                User user = ls.getUserByEmail(email);
                if (user != null) {
                    sendEmail(user);
                    sent = true;
                }
            }
        }
        if (!sent) {
            errors.put("norecord", "登録された情報がありません。");
            return forward("passwordResetForm.jsp");
        }

        return forward("passwordReset.jsp");
    }

    private void sendEmail(User u) throws UnsupportedEncodingException {

        String uuid = UUID.randomUUID().toString();

        StringBuffer requestURL = request.getRequestURL();
        int li = requestURL.lastIndexOf("/");
        String url = requestURL.substring(0, li);
        url += "/" + "newPasswordForm?" + "uuid=" + uuid;

        u.setPassword(UUID.randomUUID().toString());
        u.setNewPasswordKey(uuid);
        ls.save(u);

        System.out.println(url);

        InternetAddress ToAddress =
            new InternetAddress(u.getEmail(), u.getUserName(), "ISO-2022-JP"); // 送信相手
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
            message.setSubject("にわるかのパスワードをリセットしました", "ISO-2022-JP");

            StringBuilder sb = new StringBuilder();
            sb.append(u.getUserName() + "様\n");
            sb.append("\n");
            sb.append("にわるかをご利用いただきありがとうございます。\n");
            sb.append("\n");
            sb.append("パスワードをリセット致しましたので、次のURLをクリックして新しいパスワードを設定してください。\n");
            sb.append("\n");
            sb
                .append("---------------------------------------------------------------------\n");
            sb.append(url);
            sb.append("\n");
            sb
                .append("---------------------------------------------------------------------\n");
            sb.append("\n");
            sb.append("アカウントの情報は次の通りです\n");
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
