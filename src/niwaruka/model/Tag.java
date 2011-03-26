package niwaruka.model;

import java.io.Serializable;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Override
    public String toString() {
        return "Tag [key=" + key + ", tag=" + tag + "]";
    }

    @Attribute(version = true)
    private Long version;

    /**
     * タグの文字列
     */
    private String tag;

    /**
     * タグのアクセス情報を保持します。
     */
    private String access = TagAccess.publicTag.name();

    /**
     * タグの使用に許可が必要かどうかを保持します。
     */
    private boolean requirePermission = false;

    /**
     * タグの持ち主を保持します。
     */
    private ModelRef<UserData> creatorRef = new ModelRef<UserData>(
        UserData.class);

    /**
     * タグを保持しているユーザの概数を保持します。
     */
    private long userCount = 0;

    public static enum TagAccess {
        publicTag, sharedTag, secretTag
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
        Tag other = (Tag) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getAccess() {
        return access;
    }

    public void setRequirePermission(boolean requirePermission) {
        this.requirePermission = requirePermission;
    }

    public boolean isRequirePermission() {
        return requirePermission;
    }

    public void setUserCount(long userConunt) {
        this.userCount = userConunt;
    }

    public long getUserCount() {
        return userCount;
    }

    public ModelRef<UserData> getCreatorRef() {
        return creatorRef;
    }
}
