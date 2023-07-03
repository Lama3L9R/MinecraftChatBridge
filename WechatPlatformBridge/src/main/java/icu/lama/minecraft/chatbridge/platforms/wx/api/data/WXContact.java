package icu.lama.minecraft.chatbridge.platforms.wx.api.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WXContact{

	@SerializedName("ChatRoomId")
	private int chatRoomId;

	@SerializedName("Sex")
	private int sex;

	@SerializedName("AttrStatus")
	private long attrStatus;

	@SerializedName("Statues")
	private int statues;

	@SerializedName("PYQuanPin")
	private String pYQuanPin;

	@SerializedName("EncryChatRoomId")
	private String encryChatRoomId;

	@SerializedName("DisplayName")
	private String displayName;

	@SerializedName("VerifyFlag")
	private int verifyFlag;

	@SerializedName("UniFriend")
	private int uniFriend;

	@SerializedName("ContactFlag")
	private int contactFlag;

	@SerializedName("UserName")
	private String userName;

	@SerializedName("MemberList")
	private List<WXGroupMember> memberList;

	@SerializedName("StarFriend")
	private int starFriend;

	@SerializedName("HeadImgUrl")
	private String headImgUrl;

	@SerializedName("AppAccountFlag")
	private int appAccountFlag;

	@SerializedName("MemberCount")
	private int memberCount;

	@SerializedName("RemarkPYInitial")
	private String remarkPYInitial;

	@SerializedName("City")
	private String city;

	@SerializedName("NickName")
	private String nickName;

	@SerializedName("Province")
	private String province;

	@SerializedName("SnsFlag")
	private int snsFlag;

	@SerializedName("Alias")
	private String alias;

	@SerializedName("KeyWord")
	private String keyWord;

	@SerializedName("HideInputBarFlag")
	private int hideInputBarFlag;

	@SerializedName("Signature")
	private String signature;

	@SerializedName("RemarkName")
	private String remarkName;

	@SerializedName("RemarkPYQuanPin")
	private String remarkPYQuanPin;

	/**
	 * This is unique to all the users
	 */
	@SerializedName("Uin")
	private long uin;

	@SerializedName("OwnerUin")
	private long ownerUin;

	@SerializedName("IsOwner")
	private int isOwner;

	@SerializedName("PYInitial")
	private String pYInitial;

	public void setChatRoomId(int chatRoomId){
		this.chatRoomId = chatRoomId;
	}

	public int getChatRoomId(){
		return chatRoomId;
	}

	public void setSex(int sex){
		this.sex = sex;
	}

	public int getSex(){
		return sex;
	}

	public void setAttrStatus(long attrStatus){
		this.attrStatus = attrStatus;
	}

	public long getAttrStatus(){
		return attrStatus;
	}

	public void setStatues(int statues){
		this.statues = statues;
	}

	public int getStatues(){
		return statues;
	}

	public void setPYQuanPin(String pYQuanPin){
		this.pYQuanPin = pYQuanPin;
	}

	public String getPYQuanPin(){
		return pYQuanPin;
	}

	public void setEncryChatRoomId(String encryChatRoomId){
		this.encryChatRoomId = encryChatRoomId;
	}

	public String getEncryChatRoomId(){
		return encryChatRoomId;
	}

	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}

	public String getDisplayName(){
		return displayName;
	}

	public void setVerifyFlag(int verifyFlag){
		this.verifyFlag = verifyFlag;
	}

	public int getVerifyFlag(){
		return verifyFlag;
	}

	public void setUniFriend(int uniFriend){
		this.uniFriend = uniFriend;
	}

	public int getUniFriend(){
		return uniFriend;
	}

	public void setContactFlag(int contactFlag){
		this.contactFlag = contactFlag;
	}

	public int getContactFlag(){
		return contactFlag;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	public void setMemberList(List<WXGroupMember> memberList){
		this.memberList = memberList;
	}

	public List<WXGroupMember> getMemberList(){
		return memberList;
	}

	public void setStarFriend(int starFriend){
		this.starFriend = starFriend;
	}

	public int getStarFriend(){
		return starFriend;
	}

	public void setHeadImgUrl(String headImgUrl){
		this.headImgUrl = headImgUrl;
	}

	public String getHeadImgUrl(){
		return headImgUrl;
	}

	public void setAppAccountFlag(int appAccountFlag){
		this.appAccountFlag = appAccountFlag;
	}

	public int getAppAccountFlag(){
		return appAccountFlag;
	}

	public void setMemberCount(int memberCount){
		this.memberCount = memberCount;
	}

	public int getMemberCount(){
		return memberCount;
	}

	public void setRemarkPYInitial(String remarkPYInitial){
		this.remarkPYInitial = remarkPYInitial;
	}

	public String getRemarkPYInitial(){
		return remarkPYInitial;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setNickName(String nickName){
		this.nickName = nickName;
	}

	public String getNickName(){
		return nickName;
	}

	public void setProvince(String province){
		this.province = province;
	}

	public String getProvince(){
		return province;
	}

	public void setSnsFlag(int snsFlag){
		this.snsFlag = snsFlag;
	}

	public int getSnsFlag(){
		return snsFlag;
	}

	public void setAlias(String alias){
		this.alias = alias;
	}

	public String getAlias(){
		return alias;
	}

	public void setKeyWord(String keyWord){
		this.keyWord = keyWord;
	}

	public String getKeyWord(){
		return keyWord;
	}

	public void setHideInputBarFlag(int hideInputBarFlag){
		this.hideInputBarFlag = hideInputBarFlag;
	}

	public int getHideInputBarFlag(){
		return hideInputBarFlag;
	}

	public void setSignature(String signature){
		this.signature = signature;
	}

	public String getSignature(){
		return signature;
	}

	public void setRemarkName(String remarkName){
		this.remarkName = remarkName;
	}

	public String getRemarkName(){
		return remarkName;
	}

	public void setRemarkPYQuanPin(String remarkPYQuanPin){
		this.remarkPYQuanPin = remarkPYQuanPin;
	}

	public String getRemarkPYQuanPin(){
		return remarkPYQuanPin;
	}

	public void setUin(long uin){
		this.uin = uin;
	}

	public long getUin(){
		return uin;
	}

	public void setOwnerUin(long ownerUin){
		this.ownerUin = ownerUin;
	}

	public long getOwnerUin(){
		return ownerUin;
	}

	public void setIsOwner(int isOwner){
		this.isOwner = isOwner;
	}

	public int getIsOwner(){
		return isOwner;
	}

	public void setPYInitial(String pYInitial){
		this.pYInitial = pYInitial;
	}

	public String getPYInitial(){
		return pYInitial;
	}

	@Override
 	public String toString(){
		return 
			"WXContact{" + 
			"chatRoomId = '" + chatRoomId + '\'' + 
			",sex = '" + sex + '\'' + 
			",attrStatus = '" + attrStatus + '\'' + 
			",statues = '" + statues + '\'' + 
			",pYQuanPin = '" + pYQuanPin + '\'' + 
			",encryChatRoomId = '" + encryChatRoomId + '\'' + 
			",displayName = '" + displayName + '\'' + 
			",verifyFlag = '" + verifyFlag + '\'' + 
			",uniFriend = '" + uniFriend + '\'' + 
			",contactFlag = '" + contactFlag + '\'' +
			",userName = '" + userName + '\'' + 
			",memberList = '" + memberList + '\'' + 
			",starFriend = '" + starFriend + '\'' + 
			",headImgUrl = '" + headImgUrl + '\'' + 
			",appAccountFlag = '" + appAccountFlag + '\'' + 
			",memberCount = '" + memberCount + '\'' + 
			",remarkPYInitial = '" + remarkPYInitial + '\'' + 
			",city = '" + city + '\'' + 
			",nickName = '" + nickName + '\'' + 
			",province = '" + province + '\'' + 
			",snsFlag = '" + snsFlag + '\'' + 
			",alias = '" + alias + '\'' + 
			",keyWord = '" + keyWord + '\'' + 
			",hideInputBarFlag = '" + hideInputBarFlag + '\'' + 
			",signature = '" + signature + '\'' + 
			",remarkName = '" + remarkName + '\'' + 
			",remarkPYQuanPin = '" + remarkPYQuanPin + '\'' + 
			",uin = '" + uin + '\'' + 
			",ownerUin = '" + ownerUin + '\'' + 
			",isOwner = '" + isOwner + '\'' + 
			",pYInitial = '" + pYInitial + '\'' + 
			"}";
		}
}