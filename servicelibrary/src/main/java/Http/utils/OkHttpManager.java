package Http.utils;

import android.os.Handler;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import Http.Interface.IParameter;
import Http.callBack.ResponseCallBack;
import Http.cookie.PersistentCookieStore;
import Http.paramter.Requester;

/**
 * Created by lyd10892 on 2016/5/13.
 */
public class OkHttpManager {

    private OkHttpClient mOkHttpClient = null;
    private PersistentCookieStore mCookieStore = null;
    private Handler mHandler = null;

    public static OkHttpClient getInstance() {
        return OkHttpManagerHolder.okHttpClient;
    }

    public static class OkHttpManagerHolder {
        private static OkHttpClient okHttpClient = new OkHttpClient();
    }

    public void setDefaultConfiguration() {
        mCookieStore = new PersistentCookieStore();
        getInstance().setConnectTimeout(3000, TimeUnit.MILLISECONDS);
        getInstance().setCookieHandler(new CookieManager(mCookieStore, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
    }

    public void getFromUrl(IParameter parameter, final ResponseCallBack callBack) {
        Request request = new Request.Builder().url(parameter.getServiceName()).build();
        handleRequest(request, callBack);
    }


    private OkHttpManager() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(3000, TimeUnit.SECONDS);
        mCookieStore = new PersistentCookieStore();
        mOkHttpClient.setCookieHandler(new CookieManager(mCookieStore, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mHandler = new Handler();
    }

    public void sendPostByKeyValue(Requester requester, final ResponseCallBack callBack) {

        JsonObject jsonObject = JsonUtils.getJsonObj(requester.getReqBody());
        Set<Map.Entry<String, JsonElement>> mapSet;
        final FormEncodingBuilder builder = new FormEncodingBuilder();
        if (jsonObject != null) {
            mapSet = jsonObject.entrySet();
            for (Map.Entry<String, JsonElement> entry : mapSet) {
                String key = entry.getKey();
                String value = entry.getValue().getAsString();
                builder.addEncoded(key,value);
            }
        }

        final RequestBody body = builder.build();
        final String url = requester.getParameter().getURL();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().url(url).post(body).build();
                handleRequest(request,callBack);
            }
        }).start();
    }

    public void sendGetByUrl(final String url, final ResponseCallBack callBack){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().url(url).get().build();
                handleRequest(request,callBack);
            }
        }).start();
    }



    private void handleRequest(final Request request, final ResponseCallBack callBack) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                deliverFailure(request, callBack, e);
            }

            @Override
            public void onResponse(Response Response) throws IOException {
                String responseBody = Response.body().string();
                Object object = JsonUtils.getInstance().fromJson(responseBody, callBack.mType);
                deliverSuccess(object, callBack);

            }
        });
    }

    private void deliverSuccess(final Object object, final ResponseCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onSuccess(object);
                }
            }
        });
    }


    private void deliverFailure(final Request request, final ResponseCallBack callBack, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onFailure(request, e);
                }

            }
        });
    }
}
