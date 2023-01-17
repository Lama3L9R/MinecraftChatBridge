package icu.lama.minecraft.chatbridge.platforms.wx.api.in;

import com.google.gson.annotations.SerializedName;

public class ResponseSendMessage {

	@SerializedName("BaseResponse")
	private ResponseBase baseResponse;

	@SerializedName("LocalID")
	private String localID;

	@SerializedName("MsgID")
	private String msgID;

	public void setBaseResponse(ResponseBase baseResponse){
		this.baseResponse = baseResponse;
	}

	public ResponseBase getBaseResponse(){
		return baseResponse;
	}

	public void setLocalID(String localID){
		this.localID = localID;
	}

	public String getLocalID(){
		return localID;
	}

	public void setMsgID(String msgID){
		this.msgID = msgID;
	}

	public String getMsgID(){
		return msgID;
	}

	@Override
 	public String toString(){
		return 
			"RequestSendMessage{" + 
			"baseResponse = '" + baseResponse + '\'' + 
			",localID = '" + localID + '\'' + 
			",msgID = '" + msgID + '\'' + 
			"}";
		}
}