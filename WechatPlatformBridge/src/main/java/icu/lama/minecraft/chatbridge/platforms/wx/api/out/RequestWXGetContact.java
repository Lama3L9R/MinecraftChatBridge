package icu.lama.minecraft.chatbridge.platforms.wx.api.out;

import com.google.gson.annotations.SerializedName;

public class RequestWXGetContact {
    @SerializedName("BaseRequest")
    private RequestBase baseRequest;

    public RequestWXGetContact(RequestBase baseRequest) {
        this.baseRequest = baseRequest;
    }

    public RequestBase getBaseRequest() {
        return baseRequest;
    }

    public void setBaseRequest(RequestBase baseRequest) {
        this.baseRequest = baseRequest;
    }
}
