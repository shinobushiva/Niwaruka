package niwaruka.model;

import java.io.Serializable;

import niwaruka.meta.UploadedDataFragmentMeta;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.Sort;

import com.google.appengine.api.datastore.Key;

@Model(schemaVersion = 1)
public class UploadedData implements Serializable {

	private static final long serialVersionUID = 1L;

	@Attribute(primaryKey = true)
	private Key key;

	@Attribute(version = true)
	private Long version = 0L;

	private String fileName;

	private int length;

	@Attribute(persistent = false)
	private InverseModelListRef<UploadedDataFragment, UploadedData> fragmentListRef = new InverseModelListRef<UploadedDataFragment, UploadedData>(
			UploadedDataFragment.class, UploadedDataFragmentMeta.get().uploadDataRef, this,
			new Sort(UploadedDataFragmentMeta.get().index));

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * @return the fragmentListRef
	 */
	public InverseModelListRef<UploadedDataFragment, UploadedData> getFragmentListRef() {
		return fragmentListRef;
	}
}