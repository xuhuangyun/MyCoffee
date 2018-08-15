package com.fancoff.coffeemaker.bean.heatbit;

/**
 * Created by apple on 2018/3/18.
 */

public class UpdatePushBean extends BasePushBean {
    String version;

    String url;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
