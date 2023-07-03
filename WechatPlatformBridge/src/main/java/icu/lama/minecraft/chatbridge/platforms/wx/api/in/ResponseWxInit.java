package icu.lama.minecraft.chatbridge.platforms.wx.api.in;

import com.google.gson.annotations.SerializedName;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXContact;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXCurrentUser;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXSyncKey;

import java.util.List;

public class ResponseWxInit {

    @SerializedName("BaseResponse")
    private ResponseBase baseResponse;
    @SerializedName("Count")
    private Integer count;
    @SerializedName("ContactList")
    private List<WXContact> contactList;
    @SerializedName("SyncKey")
    private WXSyncKey syncKey;
    @SerializedName("User")
    private WXCurrentUser user;
    @SerializedName("ChatSet")
    private String chatSet;
    @SerializedName("SKey")
    private String sKey;
    @SerializedName("ClientVersion")
    private Integer clientVersion;
    @SerializedName("SystemTime")
    private Integer systemTime;
    @SerializedName("GrayScale")
    private Integer grayScale;
    @SerializedName("InviteStartCount")
    private Integer inviteStartCount;
    @SerializedName("MPSubscribeMsgCount")
    private Integer mPSubscribeMsgCount;

    @SerializedName("MPSubscribeMsgList")
    private List<?> mPSubscribeMsgList;
    @SerializedName("ClickReportInterval")
    private Integer clickReportInterval;

    public ResponseBase getBaseResponse() {
        return baseResponse;
    }

    public void setBaseResponse(ResponseBase baseResponse) {
        this.baseResponse = baseResponse;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<WXContact> getContactList() {
        return contactList;
    }

    public void setContactList(List<WXContact> contactList) {
        this.contactList = contactList;
    }

    public WXSyncKey getSyncKey() {
        return syncKey;
    }

    public void setSyncKey(WXSyncKey syncKey) {
        this.syncKey = syncKey;
    }

    public WXCurrentUser getUser() {
        return user;
    }

    public void setUser(WXCurrentUser user) {
        this.user = user;
    }

    public String getChatSet() {
        return chatSet;
    }

    public void setChatSet(String chatSet) {
        this.chatSet = chatSet;
    }

    public String getSKey() {
        return sKey;
    }

    public void setSKey(String sKey) {
        this.sKey = sKey;
    }

    public Integer getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(Integer clientVersion) {
        this.clientVersion = clientVersion;
    }

    public Integer getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(Integer systemTime) {
        this.systemTime = systemTime;
    }

    public Integer getGrayScale() {
        return grayScale;
    }

    public void setGrayScale(Integer grayScale) {
        this.grayScale = grayScale;
    }

    public Integer getInviteStartCount() {
        return inviteStartCount;
    }

    public void setInviteStartCount(Integer inviteStartCount) {
        this.inviteStartCount = inviteStartCount;
    }

    public Integer getMPSubscribeMsgCount() {
        return mPSubscribeMsgCount;
    }

    public void setMPSubscribeMsgCount(Integer mPSubscribeMsgCount) {
        this.mPSubscribeMsgCount = mPSubscribeMsgCount;
    }

    public List<?> getMPSubscribeMsgList() {
        return mPSubscribeMsgList;
    }

    public void setMPSubscribeMsgList(List<?> mPSubscribeMsgList) {
        this.mPSubscribeMsgList = mPSubscribeMsgList;
    }

    public Integer getClickReportInterval() {
        return clickReportInterval;
    }

    public void setClickReportInterval(Integer clickReportInterval) {
        this.clickReportInterval = clickReportInterval;
    }

    @Override
    public String toString() {
        return "ResponseWxInit{" +
                "baseResponse=" + baseResponse +
                ", count=" + count +
                ", contactList=" + contactList +
                ", syncKey=" + syncKey +
                ", user='" + user + '\'' +
                ", chatSet='" + chatSet + '\'' +
                ", sKey='" + sKey + '\'' +
                ", clientVersion=" + clientVersion +
                ", systemTime=" + systemTime +
                ", grayScale=" + grayScale +
                ", inviteStartCount=" + inviteStartCount +
                ", mPSubscribeMsgCount=" + mPSubscribeMsgCount +
                ", mPSubscribeMsgList=" + mPSubscribeMsgList +
                ", clickReportInterval=" + clickReportInterval +
                '}';
    }
}
