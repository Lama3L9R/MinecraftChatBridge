package icu.lama.minecraft.chatbridge.platforms.wx.api.in;

import com.google.gson.annotations.SerializedName;

public class ResponsePushLogin {

	@SerializedName("ret")
	private String ret;

	@SerializedName("msg")
	private String msg;

	@SerializedName("uuid")
	private String uuid;

	public String getRet(){
		return ret;
	}

	public String getMsg(){
		return msg;
	}

	public String getUuid(){
		return uuid;
	}
}