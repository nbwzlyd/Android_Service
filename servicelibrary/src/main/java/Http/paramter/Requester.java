package Http.paramter;


import Http.Interface.IParameter;

/**
 * Created by lyd10892 on 2016/9/6.
 */

public class Requester {

    private IParameter parameter;
    private Object object;

    public Requester(IParameter parameter, Object reqBody) {
        this.parameter = parameter;
        this.object = reqBody;

    }

    public IParameter getParameter() {
        return parameter;
    }

    public Object getReqBody() {
        return this.object;
    }
}
