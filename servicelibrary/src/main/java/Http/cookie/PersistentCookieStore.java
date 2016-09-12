package Http.cookie;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lyd10892 on 2016/5/13.
 */
public class PersistentCookieStore implements CookieStore {

    private Map<URI, List<HttpCookie>> mCookieMap = null;

    public PersistentCookieStore() {
        mCookieMap = new HashMap<>();
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        List<HttpCookie> list = new ArrayList<>();
        list.add(cookie);
        mCookieMap.put(uri, list);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        List<HttpCookie> list = mCookieMap.get(uri);
        if (list == null) {
            list = new ArrayList<>();
            mCookieMap.put(uri, list);
        }
        return list;
    }

    public List<HttpCookie> getCookies() {
        Collection<List<HttpCookie>> values = mCookieMap.values();
        List<HttpCookie> result = new ArrayList<>();
        for (List<HttpCookie> value : values) {
            result.addAll(value);
        }
        return result;
    }

    public List<URI> getURIs() {
        Set<URI> keys = mCookieMap.keySet();
        return new ArrayList<>(keys);

    }

    public boolean remove(URI uri, HttpCookie cookie) {
        List<HttpCookie> cookies = mCookieMap.get(uri);
        if (cookies == null) {
            return false;
        }
        return cookies.remove(cookie);
    }

    public boolean removeAll() {
        mCookieMap.clear();
        return true;
    }
}
