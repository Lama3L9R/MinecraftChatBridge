package icu.lama.minecraft.chatbridge.platforms.wx.api.data;

import com.google.gson.annotations.SerializedName;

public class WXMessage {

	@SerializedName("ClientMsgId")
	private String clientMsgId;

	@SerializedName("Type")
	private int type = 1;

	@SerializedName("LocalID")
	private String localID = "" + (System.currentTimeMillis() << 4) + ("" + Math.round(Math.random() * 10000));

	@SerializedName("MediaId")
	private String mediaId = "";

	@SerializedName("Content")
	private String content;

	@SerializedName("FromUserName")
	private String fromUserName;

	@SerializedName("ToUserName")
	private String toUserName;

	public WXMessage(String content, String fromUserName, String toUserName) {
		this.clientMsgId = localID;
		this.content = content;
		this.fromUserName = fromUserName;
		this.toUserName = toUserName;
	}

	public WXMessage(String content, String fromUserName, String toUserName, String mediaId) {
		this.clientMsgId = localID;
		this.content = content;
		this.fromUserName = fromUserName;
		this.toUserName = toUserName;
		this.mediaId = mediaId;
	}

	public void setClientMsgId(String clientMsgId){
		this.clientMsgId = clientMsgId;
	}

	public String getClientMsgId(){
		return clientMsgId;
	}

	public void setType(int type){
		this.type = type;
	}

	public int getType(){
		return type;
	}

	public void setLocalID(String localID){
		this.localID = localID;
	}

	public String getLocalID(){
		return localID;
	}

	public void setMediaId(String mediaId){
		this.mediaId = mediaId;
	}

	public String getMediaId(){
		return mediaId;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return content;
	}

	public void setFromUserName(String fromUserName){
		this.fromUserName = fromUserName;
	}

	public String getFromUserName(){
		return fromUserName;
	}

	public void setToUserName(String toUserName){
		this.toUserName = toUserName;
	}

	public String getToUserName(){
		return toUserName;
	}

	@Override
 	public String toString(){
		return 
			"Msg{" + 
			"clientMsgId = '" + clientMsgId + '\'' + 
			",type = '" + type + '\'' + 
			",localID = '" + localID + '\'' + 
			",mediaId = '" + mediaId + '\'' + 
			",content = '" + content + '\'' + 
			",fromUserName = '" + fromUserName + '\'' + 
			",toUserName = '" + toUserName + '\'' + 
			"}";
		}
}