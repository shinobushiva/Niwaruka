package niwaruka.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

/**
 * ユーザ情報を表すモデルです。
 * 
 * @author shiva
 * 
 */
@Model(schemaVersion = 1)
public class User implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    /**
     * ログインIDを表すフィールドです。
     */
    private String name;

    /**
     * ユーザの名前(実名やハンドル名)を表すフィールドです。
     */
    private String userName;

    /**
     * パスワード
     */
    private String password;

    /**
     * メールアドレス
     */
    private String email;

    /**
     * メールアドレス(予備)
     */
    private String email2 = "";

    /**
     * タグのキーリスト
     */
    private List<Key> tags = new ArrayList<Key>();

    /**
     * 無効化されたタグのキーリスト
     */
    private List<Key> disabledTags = new ArrayList<Key>();

    /**
     * ログイン状態保持のための文字列
     */
    private String cookieString;

    /**
     * パスワードリセットを行うときのキー
     */
    private String newPasswordKey;

    private Key userIcon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the key.
     * 
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Sets the key.
     * 
     * @param key
     *            the key
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * Returns the version.
     * 
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the version.
     * 
     * @param version
     *            the version
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public void setTags(List<Key> tgs) {
        this.tags = tgs;
    }

    public List<Key> getTags() {
        return tags;
    }

    public void setDisabledTags(List<Key> disabledTags) {
        this.disabledTags = disabledTags;
    }

    public List<Key> getDisabledTags() {
        return disabledTags;
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

    public void setCookieString(String cookieString) {
        this.cookieString = cookieString;
    }

    public String getCookieString() {
        return cookieString;
    }

    public void setNewPasswordKey(String newPasswordKey) {
        this.newPasswordKey = newPasswordKey;
    }

    public String getNewPasswordKey() {
        return newPasswordKey;
    }

    public void setUserIcon(Key userIcon) {
        this.userIcon = userIcon;
    }

    public Key getUserIcon() {
        return userIcon;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail2() {
        return email2;
    }

}
