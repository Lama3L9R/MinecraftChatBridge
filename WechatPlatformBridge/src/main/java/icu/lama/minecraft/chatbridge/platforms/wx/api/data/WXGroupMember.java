package icu.lama.minecraft.chatbridge.platforms.wx.api.data;

import com.google.gson.annotations.SerializedName;

public class WXGroupMember {

	@SerializedName("UserName")
	private String userName;

	@SerializedName("KeyWord")
	private String keyWord;

	@SerializedName("DisplayName")
	private String displayName;

	@SerializedName("AttrStatus")
	private int attrStatus;

	@SerializedName("RemarkPYInitial")
	private String remarkPYInitial;

	@SerializedName("RemarkPYQuanPin")
	private String remarkPYQuanPin;

	@SerializedName("Uin")
	private int uin;

	@SerializedName("NickName")
	private String nickName;

	@SerializedName("MemberStatus")
	private int memberStatus;

	@SerializedName("PYInitial")
	private String pYInitial;

	@SerializedName("PYQuanPin")
	private String pYQuanPin;

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	public void setKeyWord(String keyWord){
		this.keyWord = keyWord;
	}

	public String getKeyWord(){
		return keyWord;
	}

	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}

	public String getDisplayName(){
		return displayName;
	}

	public void setAttrStatus(int attrStatus){
		this.attrStatus = attrStatus;
	}

	public int getAttrStatus(){
		return attrStatus;
	}

	public void setRemarkPYInitial(String remarkPYInitial){
		this.remarkPYInitial = remarkPYInitial;
	}

	public String getRemarkPYInitial(){
		return remarkPYInitial;
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

	public void setNickName(String nickName){
		this.nickName = nickName;
	}

	public String getNickName(){
		return nickName;
	}

	public void setMemberStatus(int memberStatus){
		this.memberStatus = memberStatus;
	}

	public int getMemberStatus(){
		return memberStatus;
	}

	public void setPYInitial(String pYInitial){
		this.pYInitial = pYInitial;
	}

	public String getPYInitial(){
		return pYInitial;
	}

	public void setPYQuanPin(String pYQuanPin){
		this.pYQuanPin = pYQuanPin;
	}

	public String getPYQuanPin(){
		return pYQuanPin;
	}

	@Override
 	public String toString(){
		return 
			"MemberListItem{" + 
			"userName = '" + userName + '\'' + 
			",keyWord = '" + keyWord + '\'' + 
			",displayName = '" + displayName + '\'' + 
			",attrStatus = '" + attrStatus + '\'' + 
			",remarkPYInitial = '" + remarkPYInitial + '\'' + 
			",remarkPYQuanPin = '" + remarkPYQuanPin + '\'' + 
			",uin = '" + uin + '\'' + 
			",nickName = '" + nickName + '\'' + 
			",memberStatus = '" + memberStatus + '\'' + 
			",pYInitial = '" + pYInitial + '\'' + 
			",pYQuanPin = '" + pYQuanPin + '\'' + 
			"}";
		}
}