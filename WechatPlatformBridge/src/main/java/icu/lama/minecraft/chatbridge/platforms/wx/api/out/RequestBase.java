package icu.lama.minecraft.chatbridge.platforms.wx.api.out;

import com.google.gson.annotations.SerializedName;

public class RequestBase {

    @SerializedName("Uin")
    private Integer uin;
    @SerializedName("Sid")
    private String sid;
    @SerializedName("Skey")
    private String skey;
    @SerializedName("DeviceID")
    private String deviceID;

    public RequestBase(Integer uin, String sid, String skey, String deviceID) {
        this.uin = uin;
        this.sid = sid;
        this.skey = skey;
        this.deviceID = deviceID;
    }

    public Integer getUin() {
        return uin;
    }

    public void setUin(Integer uin) {
        this.uin = uin;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
