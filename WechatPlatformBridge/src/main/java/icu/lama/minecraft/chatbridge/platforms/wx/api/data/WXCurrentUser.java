package icu.lama.minecraft.chatbridge.platforms.wx.api.data;

import com.google.gson.annotations.SerializedName;

public class WXCurrentUser{

	@SerializedName("UserName")
	private String userName;

	@SerializedName("StarFriend")
	private int starFriend;

	@SerializedName("HeadImgUrl")
	private String headImgUrl;

	@SerializedName("Sex")
	private int sex;

	@SerializedName("AppAccountFlag")
	private int appAccountFlag;

	@SerializedName("RemarkPYInitial")
	private String remarkPYInitial;

	@SerializedName("NickName")
	private String nickName;

	@SerializedName("HeadImgFlag")
	private int headImgFlag;

	@SerializedName("PYQuanPin")
	private String pYQuanPin;

	@SerializedName("WebWxPluginSwitch")
	private int webWxPluginSwitch;

	@SerializedName("SnsFlag")
	private int snsFlag;

	@SerializedName("HideInputBarFlag")
	private int hideInputBarFlag;

	@SerializedName("Signature")
	private String signature;

	@SerializedName("RemarkName")
	private String remarkName;

	@SerializedName("RemarkPYQuanPin")
	private String remarkPYQuanPin;

	@SerializedName("Uin")
	private int uin;

	@SerializedName("VerifyFlag")
	private int verifyFlag;

	@SerializedName("PYInitial")
	private String pYInitial;

	@SerializedName("ContactFlag")
	private int contactFlag;

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
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

	public void setSex(int sex){
		this.sex = sex;
	}

	public int getSex(){
		return sex;
	}

	public void setAppAccountFlag(int appAccountFlag){
		this.appAccountFlag = appAccountFlag;
	}

	public int getAppAccountFlag(){
		return appAccountFlag;
	}

	public void setRemarkPYInitial(String remarkPYInitial){
		this.remarkPYInitial = remarkPYInitial;
	}

	public String getRemarkPYInitial(){
		return remarkPYInitial;
	}

	public void setNickName(String nickName){
		this.nickName = nickName;
	}

	public String getNickName(){
		return nickName;
	}

	public void setHeadImgFlag(int headImgFlag){
		this.headImgFlag = headImgFlag;
	}

	public int getHeadImgFlag(){
		return headImgFlag;
	}

	public void setPYQuanPin(String pYQuanPin){
		this.pYQuanPin = pYQuanPin;
	}

	public String getPYQuanPin(){
		return pYQuanPin;
	}

	public void setWebWxPluginSwitch(int webWxPluginSwitch){
		this.webWxPluginSwitch = webWxPluginSwitch;
	}

	public int getWebWxPluginSwitch(){
		return webWxPluginSwitch;
	}

	public void setSnsFlag(int snsFlag){
		this.snsFlag = snsFlag;
	}

	public int getSnsFlag(){
		return snsFlag;
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

	public void setUin(int uin){
		this.uin = uin;
	}

	public int getUin(){
		return uin;
	}

	public void setVerifyFlag(int verifyFlag){
		this.verifyFlag = verifyFlag;
	}

	public int getVerifyFlag(){
		return verifyFlag;
	}

	public void setPYInitial(String pYInitial){
		this.pYInitial = pYInitial;
	}

	public String getPYInitial(){
		return pYInitial;
	}

	public void setContactFlag(int contactFlag){
		this.contactFlag = contactFlag;
	}

	public int getContactFlag(){
		return contactFlag;
	}

	@Override
 	public String toString(){
		return 
			"WXCurrentUser{" + 
			"userName = '" + userName + '\'' + 
			",starFriend = '" + starFriend + '\'' + 
			",headImgUrl = '" + headImgUrl + '\'' + 
			",sex = '" + sex + '\'' + 
			",appAccountFlag = '" + appAccountFlag + '\'' + 
			",remarkPYInitial = '" + remarkPYInitial + '\'' + 
			",nickName = '" + nickName + '\'' + 
			",headImgFlag = '" + headImgFlag + '\'' + 
			",pYQuanPin = '" + pYQuanPin + '\'' + 
			",webWxPluginSwitch = '" + webWxPluginSwitch + '\'' + 
			",snsFlag = '" + snsFlag + '\'' + 
			",hideInputBarFlag = '" + hideInputBarFlag + '\'' + 
			",signature = '" + signature + '\'' + 
			",remarkName = '" + remarkName + '\'' + 
			",remarkPYQuanPin = '" + remarkPYQuanPin + '\'' + 
			",uin = '" + uin + '\'' + 
			",verifyFlag = '" + verifyFlag + '\'' + 
			",pYInitial = '" + pYInitial + '\'' + 
			",contactFlag = '" + contactFlag + '\'' + 
			"}";
		}
}