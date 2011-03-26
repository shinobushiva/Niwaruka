package jp.co.topgate.controller;

import java.util.Map;
import java.util.logging.Logger;

import net.arnx.jsonic.JSON;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.DatastoreTimeoutException;
import com.google.appengine.repackaged.com.google.common.collect.Maps;
import com.google.apphosting.api.ApiProxy.CapabilityDisabledException;
import com.google.apphosting.api.DeadlineExceededException;

public abstract class JsonController extends Controller {
    static final Logger logger = Logger.getLogger(JsonController.class
        .getName());
    static final String STATUS = "status";
    static final String ERRCODE = "errorCode";
    static final String ERRMESSAGE = "errorMessage";
    static final String CANRETRY = "canRetry";
    static final String STATUS_OK = "OK";
    static final String STATUS_NG = "NG";

    abstract protected Map<String, Object> handle() throws Exception;

    @Override
    protected Navigation run() throws Exception {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        Map<String, Object> map = handle();
        if (map == null) {
            throw new AssertionError("handle() must not be null.");
        }
        if (map.get(STATUS) == null) {
            map.put(STATUS, STATUS_OK);
        }
        JSON.encode(map, response.getOutputStream());
        response.flushBuffer();
        return null;
    }

    @Override
    protected Navigation handleError(Throwable error) throws Throwable {
        Map<String, Object> map = Maps.newHashMap();
        String errorCode;
        String errorMessage;
        boolean canRetry = false;
        if (error instanceof CapabilityDisabledException) {
            errorCode = "READONLY";
            errorMessage = "AppEngineのサービスが読み取り専用です";
        } else if (error instanceof DatastoreTimeoutException) {
            errorCode = "DSTIMEOUT";
            errorMessage = "データストアがタイムアウトしました。";
            canRetry = true;
        } else if (error instanceof DatastoreFailureException) {
            errorCode = "DSFAILURE";
            errorMessage = "データストアのアクセスに失敗しました。";
        } else if (error instanceof DeadlineExceededException) {
            errorCode = "DEE";
            errorMessage = "30秒を超えても処理が終了しませんでした。";
            canRetry = true;
        } else {
            errorCode = "UNKNOWN";
            errorMessage = "予期せぬエラーが発生しました。" + error.toString();
        }
        map.put(STATUS, STATUS_NG);
        map.put(ERRCODE, errorCode);
        map.put(ERRMESSAGE, errorMessage);
        map.put(CANRETRY, canRetry);
        JSON.encode(map, response.getOutputStream());
        response.flushBuffer();
        return null;
    }
}