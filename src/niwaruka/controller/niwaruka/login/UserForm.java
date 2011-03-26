package niwaruka.controller.niwaruka.login;

import java.io.Serializable;

import niwaruka.controller.niwaruka.member.MypageConfigController;

/**
 * ユーザ登録フォームに入力された値を取得するためのクラスです。
 * 
 * @see UserRegistrationFormController
 * @see MypageConfigController
 * @see niwaruka/login/userRegistrationForm.jsp
 * @see niwaruka/member/mypage.jsp
 * 
 * @author shiva
 * 
 */
public class UserForm implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2668243349690232441L;
    private String name;
    private String userName;
    private String passwd1;
    private String passwd2;
    private String tags;
    private String email;
    private String email2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd1() {
        return passwd1;
    }

    public void setPasswd1(String passwd1) {
        this.passwd1 = passwd1;
    }

    public String getPasswd2() {
        return passwd2;
    }

    public void setPasswd2(String passwd2) {
        this.passwd2 = passwd2;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public UserForm() {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail2() {
        return email2;
    }
}