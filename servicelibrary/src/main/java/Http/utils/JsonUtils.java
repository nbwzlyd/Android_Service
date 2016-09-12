package Http.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lyd10892 on 2016/9/7.
 */

public class JsonUtils {

    private JsonUtils() {

    }

    private static class JsonUtilHolder {
        private static Gson gson = new Gson();

    }

    public static Gson getInstance() {
        return JsonUtilHolder.gson;
    }

    public static JsonObject getJsonObj(Object object){
        JsonElement element = JsonUtils.getInstance().toJsonTree(object);
        return  element.getAsJsonObject();
    }

}
