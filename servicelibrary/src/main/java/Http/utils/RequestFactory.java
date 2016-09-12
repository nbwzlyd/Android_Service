package Http.utils;

import Http.Interface.IParameter;
import Http.paramter.Requester;

/**
 * Created by lyd10892 on 2016/9/6.
 */

public class RequestFactory {

    public static Requester create(IParameter parameter,Object reqBody) {
        return  new Requester(parameter,reqBody);
    }

}
