package icu.lama.minecraft.chatbridge.platforms.wx.api.out;

import com.google.gson.annotations.SerializedName;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXMessage;

public class RequestSendMessage{

	@SerializedName("Msg")
	private WXMessage msg;

	@SerializedName("BaseRequest")
	private RequestBase baseRequest;

	@SerializedName("Scene")
	private int scene = 0;

	public RequestSendMessage(RequestBase baseRequest, WXMessage msg) {
		this.baseRequest = baseRequest;
		this.msg = msg;
	}

	public void setMsg(WXMessage msg){
		this.msg = msg;
	}

	public WXMessage getMsg(){
		return msg;
	}

	public void setBaseRequest(RequestBase baseRequest){
		this.baseRequest = baseRequest;
	}

	public RequestBase getBaseRequest(){
		return baseRequest;
	}

	public void setScene(int scene){
		this.scene = scene;
	}

	public int getScene(){
		return scene;
	}

	@Override
 	public String toString(){
		return 
			"RequestSendMessage{" + 
			"msg = '" + msg + '\'' + 
			",baseRequest = '" + baseRequest + '\'' + 
			",scene = '" + scene + '\'' + 
			"}";
		}
}