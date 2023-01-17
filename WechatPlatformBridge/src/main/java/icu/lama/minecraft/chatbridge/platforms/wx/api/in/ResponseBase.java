package icu.lama.minecraft.chatbridge.platforms.wx.api.in;

import com.google.gson.annotations.SerializedName;

public class ResponseBase {
    @SerializedName("Ret")
    private int ret;

    @SerializedName("ErrMsg")
    private String errMsg;

    public void setRet(int ret){
        this.ret = ret;
    }

    public int getRet(){
        return ret;
    }

    public void setErrMsg(String errMsg){
        this.errMsg = errMsg;
    }

    public String getErrMsg(){
        return errMsg;
    }

    @Override
    public String toString(){
        return
                "BaseResponse{" +
                        "ret = '" + ret + '\'' +
                        ",errMsg = '" + errMsg + '\'' +
                        "}";
    }

}
