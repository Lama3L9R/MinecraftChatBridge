package icu.lama.minecraft.chatbridge.platforms.wx.api.data;

import com.google.gson.annotations.SerializedName;

public class WXAppInfo {

	@SerializedName("Type")
	private int type;

	@SerializedName("AppID")
	private String appID;

	public void setType(int type){
		this.type = type;
	}

	public int getType(){
		return type;
	}

	public void setAppID(String appID){
		this.appID = appID;
	}

	public String getAppID(){
		return appID;
	}

	@Override
 	public String toString(){
		return 
			"AppInfo{" + 
			"type = '" + type + '\'' + 
			",appID = '" + appID + '\'' + 
			"}";
		}
}