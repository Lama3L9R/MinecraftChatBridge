package icu.lama.minecraft.chatbridge.platforms.wx.api.out;

import com.google.gson.annotations.SerializedName;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXSyncKey;

import java.util.Date;

public class RequestWXSync {
    @SerializedName("BaseRequest")
    private RequestBase baseRequest;

    @SerializedName("SyncKey")
    private WXSyncKey syncKey;

    private final long rr = -new Date().getTime() / 1000;

    public RequestWXSync(RequestBase baseRequest, WXSyncKey syncKey) {
        this.baseRequest = baseRequest;
        this.syncKey = syncKey;
    }

    public RequestBase getBaseRequest() {
        return baseRequest;
    }

    public void setBaseRequest(RequestBase baseRequest) {
        this.baseRequest = baseRequest;
    }

    public WXSyncKey getSyncKey() {
        return syncKey;
    }

    public void setSyncKey(WXSyncKey syncKey) {
        this.syncKey = syncKey;
    }

    public long getRR() {
        return rr;
    }
}
