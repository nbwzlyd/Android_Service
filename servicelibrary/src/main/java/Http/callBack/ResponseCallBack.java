package Http.callBack;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by lyd10892 on 2016/5/16.
 */
public abstract  class ResponseCallBack<T> {
    public Type mType;
    public  ResponseCallBack(){
        mType = getType(getClass());
    }

    private Type getType(Class<? extends ResponseCallBack> aClass) {

        Type cType = aClass.getGenericSuperclass();

        if (cType instanceof  Class){
              throw  new RuntimeException("Missing the class Type");
        }
        ParameterizedType parameterized = (ParameterizedType) cType;
        return  $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);

    }

    public abstract void  onSuccess(T response);
    public abstract void onFailure(Request request,IOException e);

}
