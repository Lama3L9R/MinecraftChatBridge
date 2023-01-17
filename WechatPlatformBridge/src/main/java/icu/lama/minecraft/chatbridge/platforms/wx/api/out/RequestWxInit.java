package icu.lama.minecraft.chatbridge.platforms.wx.api.out;

import com.google.gson.annotations.SerializedName;

public class RequestWxInit {
    @SerializedName("BaseRequest")
    private RequestBase baseRequest;

    public RequestWxInit(RequestBase baseRequest) {
        this.baseRequest = baseRequest;
    }

    public RequestBase getBaseRequest() {
        return baseRequest;
    }

    public void setBaseRequest(RequestBase baseRequest) {
        this.baseRequest = baseRequest;
    }
}
