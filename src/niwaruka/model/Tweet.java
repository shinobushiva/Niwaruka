package niwaruka.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class Tweet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    private String content;

    private String client;

    // private ModelRef<User> userRef = new ModelRef<User>(User.class);

    private ModelRef<UserData> userDataRef = new ModelRef<UserData>(
        UserData.class);

    private Key replyTo;

    /**
     * Twitterからハッシュタグで持ってきたときのユーザ名
     */
    private String twitterUser;

    /**
     * つぶやいた時点でのタグを保存します。 最新のタグとは異なる場合があることに注意してください。
     */
    @Attribute(lob = true)
    private List<Tag> tagList = new ArrayList<Tag>();

    private Date time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        Tweet other = (Tweet) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    // public ModelRef<User> getUserRef() {
    // return userRef;
    // }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getTime() {
        return time;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getClient() {
        return client;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTwitterUser(String twitterUser) {
        this.twitterUser = twitterUser;
    }

    public String getTwitterUser() {
        return twitterUser;
    }

    public void setReplyTo(Key replyTo) {
        this.replyTo = replyTo;
    }

    public Key getReplyTo() {
        return replyTo;
    }

    public ModelRef<UserData> getUserDataRef() {
        return userDataRef;
    }

}
