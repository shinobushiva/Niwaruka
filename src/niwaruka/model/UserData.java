package niwaruka.model;

import java.io.Serializable;

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
public class UserData implements Serializable {

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
     * Userのアイコンのキー
     */
    private Key userIcon;

    /**
     * ユーザのキーを保持します。
     */
    private Key userKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        UserData other = (UserData) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public void setUserIcon(Key userIcon) {
        this.userIcon = userIcon;
    }

    public Key getUserIcon() {
        return userIcon;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserKey(Key userKey) {
        this.userKey = userKey;
    }

    public Key getUserKey() {
        return userKey;
    }

}
