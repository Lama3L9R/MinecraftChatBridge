package icu.lama.minecraft.chatbridge.platforms.wx.api.data;

import com.google.gson.annotations.SerializedName;

public class WXContactQueryItem {

	@SerializedName("UserName")
	private String userName = "";

	@SerializedName("EncryChatRoomId")
	private String encryChatRoomId = "";

	public WXContactQueryItem() { }

	public WXContactQueryItem(String userName) {
		this.userName = userName;
	}

	public WXContactQueryItem(String userName, String encryChatRoomId) {
		this.userName = userName;
		this.encryChatRoomId = encryChatRoomId;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	public void setEncryChatRoomId(String encryChatRoomId){
		this.encryChatRoomId = encryChatRoomId;
	}

	public String getEncryChatRoomId(){
		return encryChatRoomId;
	}

	@Override
 	public String toString(){
		return 
			"ListItem{" + 
			"userName = '" + userName + '\'' + 
			",encryChatRoomId = '" + encryChatRoomId + '\'' + 
			"}";
		}
}