package icu.lama.minecraft.chatbridge.platforms.wx.api.data;

import com.google.gson.annotations.SerializedName;

public class WXNewMessage {

	@SerializedName("SubMsgType")
	private int subMsgType;

	@SerializedName("VoiceLength")
	private int voiceLength;

	@SerializedName("FileName")
	private String fileName;

	@SerializedName("ImgHeight")
	private int imgHeight;

	@SerializedName("ToUserName")
	private String toUserName;

	@SerializedName("HasProductId")
	private int hasProductId;

	@SerializedName("ImgStatus")
	private int imgStatus;

	@SerializedName("Url")
	private String url;

	@SerializedName("ImgWidth")
	private int imgWidth;

	@SerializedName("ForwardFlag")
	private int forwardFlag;

	@SerializedName("Status")
	private int status;

	@SerializedName("Ticket")
	private String ticket;

	@SerializedName("CreateTime")
	private int createTime;

	@SerializedName("NewMsgId")
	private long newMsgId;

	@SerializedName("MsgType")
	private int msgType;

	@SerializedName("EncryFileName")
	private String encryFileName;

	@SerializedName("MsgId")
	private String msgId;

	@SerializedName("StatusNotifyCode")
	private int statusNotifyCode;

	@SerializedName("AppInfo")
	private WXAppInfo appInfo;

	@SerializedName("AppMsgType")
	private int appMsgType;

	@SerializedName("PlayLength")
	private int playLength;

	@SerializedName("MediaId")
	private String mediaId;

	@SerializedName("Content")
	private String content;

	@SerializedName("FromUserName")
	private String fromUserName;

	@SerializedName("OriContent")
	private String oriContent;

	@SerializedName("FileSize")
	private String fileSize;

	public void setSubMsgType(int subMsgType){
		this.subMsgType = subMsgType;
	}

	public int getSubMsgType(){
		return subMsgType;
	}

	public void setVoiceLength(int voiceLength){
		this.voiceLength = voiceLength;
	}

	public int getVoiceLength(){
		return voiceLength;
	}

	public void setFileName(String fileName){
		this.fileName = fileName;
	}

	public String getFileName(){
		return fileName;
	}

	public void setImgHeight(int imgHeight){
		this.imgHeight = imgHeight;
	}

	public int getImgHeight(){
		return imgHeight;
	}

	public void setToUserName(String toUserName){
		this.toUserName = toUserName;
	}

	public String getToUserName(){
		return toUserName;
	}

	public void setHasProductId(int hasProductId){
		this.hasProductId = hasProductId;
	}

	public int getHasProductId(){
		return hasProductId;
	}

	public void setImgStatus(int imgStatus){
		this.imgStatus = imgStatus;
	}

	public int getImgStatus(){
		return imgStatus;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public void setImgWidth(int imgWidth){
		this.imgWidth = imgWidth;
	}

	public int getImgWidth(){
		return imgWidth;
	}

	public void setForwardFlag(int forwardFlag){
		this.forwardFlag = forwardFlag;
	}

	public int getForwardFlag(){
		return forwardFlag;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	public void setTicket(String ticket){
		this.ticket = ticket;
	}

	public String getTicket(){
		return ticket;
	}

	public void setCreateTime(int createTime){
		this.createTime = createTime;
	}

	public int getCreateTime(){
		return createTime;
	}

	public void setNewMsgId(long newMsgId){
		this.newMsgId = newMsgId;
	}

	public long getNewMsgId(){
		return newMsgId;
	}

	public void setMsgType(int msgType){
		this.msgType = msgType;
	}

	public int getMsgType(){
		return msgType;
	}

	public void setEncryFileName(String encryFileName){
		this.encryFileName = encryFileName;
	}

	public String getEncryFileName(){
		return encryFileName;
	}

	public void setMsgId(String msgId){
		this.msgId = msgId;
	}

	public String getMsgId(){
		return msgId;
	}

	public void setStatusNotifyCode(int statusNotifyCode){
		this.statusNotifyCode = statusNotifyCode;
	}

	public int getStatusNotifyCode(){
		return statusNotifyCode;
	}

	public void setAppInfo(WXAppInfo appInfo){
		this.appInfo = appInfo;
	}

	public WXAppInfo getAppInfo(){
		return appInfo;
	}

	public void setAppMsgType(int appMsgType){
		this.appMsgType = appMsgType;
	}

	public int getAppMsgType(){
		return appMsgType;
	}

	public void setPlayLength(int playLength){
		this.playLength = playLength;
	}

	public int getPlayLength(){
		return playLength;
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

	public void setOriContent(String oriContent){
		this.oriContent = oriContent;
	}

	public String getOriContent(){
		return oriContent;
	}

	public void setFileSize(String fileSize){
		this.fileSize = fileSize;
	}

	public String getFileSize(){
		return fileSize;
	}

	@Override
 	public String toString(){
		return 
			"AddMsgListItem{" + 
			"subMsgType = '" + subMsgType + '\'' + 
			",voiceLength = '" + voiceLength + '\'' + 
			",fileName = '" + fileName + '\'' + 
			",imgHeight = '" + imgHeight + '\'' + 
			",toUserName = '" + toUserName + '\'' + 
			",hasProductId = '" + hasProductId + '\'' + 
			",imgStatus = '" + imgStatus + '\'' + 
			",url = '" + url + '\'' + 
			",imgWidth = '" + imgWidth + '\'' + 
			",forwardFlag = '" + forwardFlag + '\'' + 
			",status = '" + status + '\'' + 
			",ticket = '" + ticket + '\'' + 
			",createTime = '" + createTime + '\'' + 
			",newMsgId = '" + newMsgId + '\'' + 
			",msgType = '" + msgType + '\'' + 
			",encryFileName = '" + encryFileName + '\'' + 
			",msgId = '" + msgId + '\'' + 
			",statusNotifyCode = '" + statusNotifyCode + '\'' + 
			",appInfo = '" + appInfo + '\'' + 
			",appMsgType = '" + appMsgType + '\'' + 
			",playLength = '" + playLength + '\'' + 
			",mediaId = '" + mediaId + '\'' + 
			",content = '" + content + '\'' + 
			",fromUserName = '" + fromUserName + '\'' + 
			",oriContent = '" + oriContent + '\'' + 
			",fileSize = '" + fileSize + '\'' + 
			"}";
		}
}