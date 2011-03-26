package niwaruka.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import niwaruka.meta.UploadedDataFragmentMeta;
import niwaruka.meta.UploadedDataMeta;
import niwaruka.model.UploadedData;
import niwaruka.model.UploadedDataFragment;

import org.slim3.controller.upload.FileItem;
import org.slim3.datastore.Datastore;
import org.slim3.util.ByteUtil;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesService.OutputEncoding;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;

public class UploadService {
    private static final int FRAGMENT_SIZE = 900000;

    private UploadedDataMeta d = UploadedDataMeta.get();

    private UploadedDataFragmentMeta f = UploadedDataFragmentMeta.get();

    private static final Logger LOG = Logger.getLogger(UploadService.class
        .getName());

    public List<UploadedData> getDataList() {
        return Datastore.query(d).asList();
    }

    public UploadedData upload(FileItem formFile) {
        if (formFile == null) {
            return null;
        }
        List<Object> models = new ArrayList<Object>();
        UploadedData data = new UploadedData();
        models.add(data);

        ImagesService imagesService = ImagesServiceFactory.getImagesService(); // …(D)
        Image showImage = null;

        try {
            showImage = ImagesServiceFactory.makeImage(formFile.getData());
            showImage.getHeight(); // check if it's image or not
        } catch (Exception e) {
            // e.printStackTrace();
            LOG.warning("The uploaded file is not an image.");
            return null;
        }

        int w = showImage.getWidth();
        double wp = 320.0 / w;
        int h = showImage.getHeight();
        double hp = 320.0 / h;

        if (wp < 1.0 || hp < 1.0) {
            if (wp < hp) {
                w = (int) (w * wp);
                h = (int) (h * wp);
            } else {
                w = (int) (w * hp);
                h = (int) (h * hp);
            }
        }

        Transform transform = ImagesServiceFactory.makeResize(w, h);
        showImage =
            imagesService.applyTransform(
                transform,
                showImage,
                OutputEncoding.PNG);

        data.setKey(Datastore.allocateId(d));
        data.setFileName(formFile.getShortFileName());
        byte[] bytes = showImage.getImageData();
        data.setLength(bytes.length);
        byte[][] bytesArray = ByteUtil.split(bytes, FRAGMENT_SIZE);
        Iterator<Key> keys =
            Datastore
                .allocateIds(data.getKey(), f, bytesArray.length)
                .iterator();
        for (int i = 0; i < bytesArray.length; i++) {
            byte[] fragmentData = bytesArray[i];
            UploadedDataFragment fragment = new UploadedDataFragment();
            models.add(fragment);
            fragment.setKey(keys.next());
            fragment.setBytes(fragmentData);
            fragment.setIndex(i);
            fragment.getUploadDataRef().setModel(data);
        }
        Transaction tx = Datastore.beginTransaction();
        for (Object model : models) {
            Datastore.put(tx, model);
        }
        tx.commit();
        return data;
    }

    public UploadedData getData(Key key, Long version) {
        return Datastore.get(d, key, version);
    }

    public UploadedData getData(Key key) {
        return Datastore.get(d, key);
    }

    public byte[] getBytes(UploadedData uploadedData) {
        if (uploadedData == null) {
            throw new NullPointerException(
                "The uploadedData parameter must not be null.");
        }
        List<UploadedDataFragment> fragmentList =
            uploadedData.getFragmentListRef().getModelList();
        byte[][] bytesArray = new byte[fragmentList.size()][0];
        for (int i = 0; i < fragmentList.size(); i++) {
            bytesArray[i] = fragmentList.get(i).getBytes();
        }
        return ByteUtil.join(bytesArray);
    }

    public void delete(Key key) {
        Transaction tx = Datastore.beginTransaction();
        List<Key> keys = new ArrayList<Key>();
        keys.add(key);
        keys.addAll(Datastore.query(f, key).asKeyList());
        Datastore.delete(tx, keys);
        tx.commit();
    }
}