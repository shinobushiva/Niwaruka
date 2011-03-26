package niwaruka.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class TagMail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    /**
     * ユーザのキーを保持します。
     */
    private Key owner;

    /**
     * タグのキーリスト
     */
    private List<Key> tags = new ArrayList<Key>();

    /**
     * And / Or
     */
    private String option;

    /**
     * 転送先メールアドレス
     */
    private String email;

    /**
     * 最後に転送したメッセージの時間
     */
    private Date lastMessage;

    /**
     * 設定した時点でのタグを保存します。 最新のタグとは異なる場合があることに注意してください。
     */
    @Attribute(lob = true)
    private List<Tag> tagList = new ArrayList<Tag>();

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
        TagMail other = (TagMail) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public void setOwner(Key owner) {
        this.owner = owner;
    }

    public Key getOwner() {
        return owner;
    }

    public void setTags(List<Key> tags) {
        this.tags = tags;
    }

    public List<Key> getTags() {
        return tags;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setLastMessage(Date lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Date getLastMessage() {
        return lastMessage;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public List<Tag> getTagList() {
        return tagList;
    }
}
