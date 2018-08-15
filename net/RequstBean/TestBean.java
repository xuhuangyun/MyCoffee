package com.fancoff.coffeemaker.net.RequstBean;

/**
 * Created by apple on 2017/11/17.
 */

public class TestBean extends  BaseBean{
    public class Data {
        String createTime;
        String deviceType;
        String id;
        String modifyTime;
        String status;
        String url;
        String version;
        String versionInf;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(String modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getVersionInf() {
            return versionInf;
        }

        public void setVersionInf(String versionInf) {
            this.versionInf = versionInf;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "createTime='" + createTime + '\'' +
                    ", deviceType='" + deviceType + '\'' +
                    ", id='" + id + '\'' +
                    ", modifyTime='" + modifyTime + '\'' +
                    ", status='" + status + '\'' +
                    ", url='" + url + '\'' +
                    ", version='" + version + '\'' +
                    ", versionInf='" + versionInf + '\'' +
                    '}';
        }
    }

    int code;
    String message;
    Data data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data.toString() +
                '}';
    }
}
