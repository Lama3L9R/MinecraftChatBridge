package icu.lama.minecraft.chatbridge.platforms.wx.api.in;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXNewMessage;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXSyncKey;

public class ResponseWXSync{

	@SerializedName("ModContactList")
	private List<Object> modContactList;

	@SerializedName("DelContactCount")
	private int delContactCount;

	@SerializedName("AddMsgCount")
	private int addMsgCount;

	@SerializedName("ContinueFlag")
	private int continueFlag;

	@SerializedName("SKey")
	private String sKey;

	@SerializedName("BaseResponse")
	private ResponseBase baseResponse;

	@SerializedName("DelContactList")
	private List<Object> delContactList;

	@SerializedName("ModChatRoomMemberList")
	private List<Object> modChatRoomMemberList;

	@SerializedName("ModContactCount")
	private int modContactCount;

	@SerializedName("ModChatRoomMemberCount")
	private int modChatRoomMemberCount;

	@SerializedName("SyncCheckKey")
	private WXSyncKey syncCheckKey;

	@SerializedName("AddMsgList")
	private List<WXNewMessage> addMsgList;

	@SerializedName("SyncKey")
	private WXSyncKey syncKey;

	public void setModContactList(List<Object> modContactList){
		this.modContactList = modContactList;
	}

	public List<Object> getModContactList(){
		return modContactList;
	}

	public void setDelContactCount(int delContactCount){
		this.delContactCount = delContactCount;
	}

	public int getDelContactCount(){
		return delContactCount;
	}

	public void setAddMsgCount(int addMsgCount){
		this.addMsgCount = addMsgCount;
	}

	public int getAddMsgCount(){
		return addMsgCount;
	}

	public void setContinueFlag(int continueFlag){
		this.continueFlag = continueFlag;
	}

	public int getContinueFlag(){
		return continueFlag;
	}

	public void setSKey(String sKey){
		this.sKey = sKey;
	}

	public String getSKey(){
		return sKey;
	}

	public void setBaseResponse(ResponseBase baseResponse){
		this.baseResponse = baseResponse;
	}

	public ResponseBase getBaseResponse(){
		return baseResponse;
	}

	public void setDelContactList(List<Object> delContactList){
		this.delContactList = delContactList;
	}

	public List<Object> getDelContactList(){
		return delContactList;
	}

	public void setModChatRoomMemberList(List<Object> modChatRoomMemberList){
		this.modChatRoomMemberList = modChatRoomMemberList;
	}

	public List<Object> getModChatRoomMemberList(){
		return modChatRoomMemberList;
	}

	public void setModContactCount(int modContactCount){
		this.modContactCount = modContactCount;
	}

	public int getModContactCount(){
		return modContactCount;
	}

	public void setModChatRoomMemberCount(int modChatRoomMemberCount){
		this.modChatRoomMemberCount = modChatRoomMemberCount;
	}

	public int getModChatRoomMemberCount(){
		return modChatRoomMemberCount;
	}

	public void setSyncCheckKey(WXSyncKey syncCheckKey){
		this.syncCheckKey = syncCheckKey;
	}

	public WXSyncKey getSyncCheckKey(){
		return syncCheckKey;
	}

	public void setAddMsgList(List<WXNewMessage> addMsgList){
		this.addMsgList = addMsgList;
	}

	public List<WXNewMessage> getAddMsgList(){
		return addMsgList;
	}

	public void setSyncKey(WXSyncKey syncKey){
		this.syncKey = syncKey;
	}

	public WXSyncKey getSyncKey(){
		return syncKey;
	}

	@Override
 	public String toString(){
		return 
			"ResponseWXSync{" + 
			"modContactList = '" + modContactList + '\'' + 
			",delContactCount = '" + delContactCount + '\'' + 
			",addMsgCount = '" + addMsgCount + '\'' + 
			",continueFlag = '" + continueFlag + '\'' + 
			",sKey = '" + sKey + '\'' + 
			",baseResponse = '" + baseResponse + '\'' + 
			",delContactList = '" + delContactList + '\'' + 
			",modChatRoomMemberList = '" + modChatRoomMemberList + '\'' + 
			",modContactCount = '" + modContactCount + '\'' + 
			",modChatRoomMemberCount = '" + modChatRoomMemberCount + '\'' + 
			",syncCheckKey = '" + syncCheckKey + '\'' + 
			",addMsgList = '" + addMsgList + '\'' + 
			",syncKey = '" + syncKey + '\'' + 
			"}";
		}
}