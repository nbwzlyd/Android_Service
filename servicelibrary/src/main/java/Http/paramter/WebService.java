package Http.paramter;

import Http.Interface.IParameter;

/**
 * Created by lyd10892 on 2016/9/7.
 */

public class WebService implements IParameter {
    private IParameter parameter;

    public WebService(IParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public String getServiceName() {
        return this.parameter.getServiceName();
    }

    @Override
    public String getHost() {
        return parameter.getHost();
    }

    @Override
    public String getURL() {
        return parameter.getURL();
    }

}
