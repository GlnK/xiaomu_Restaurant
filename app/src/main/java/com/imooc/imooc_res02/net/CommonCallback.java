package com.imooc.imooc_res02.net;

import com.imooc.imooc_res02.utils.GsonUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;

/**
 * Created by Gln on 2017/4/28.
 */
public abstract class CommonCallback<T> extends StringCallback {
    Type mType;

    public CommonCallback() {
        Class<? extends CommonCallback> clazz = getClass();
        Type genericsSuperclass = clazz.getGenericSuperclass();

        if (genericsSuperclass instanceof Class) {
            throw new RuntimeException("Miss Type Params");
        }
        ParameterizedType parameterizedType = (ParameterizedType) genericsSuperclass;
        mType = parameterizedType.getActualTypeArguments()[0];
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        onError(e);
    }
    @Override
    public void onResponse(String response, int id) {
        try {
            JSONObject resp = new JSONObject(response);
            int resultCode = resp.getInt("resultCode");
            if (resultCode == 1) {
                String data = resp.getString("data");
                onSuccess((T) GsonUtil.getGson().fromJson(data, mType));
            } else {
                onError(new RuntimeException(resp.getString("resultMessage")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            onError(e);
        }
    }

    public abstract void onError(Exception e);

    public abstract void onSuccess(T response);

}
