package icu.lama.minecraft.chatbridge.platforms.wx.api.in;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXContact;

public class ResponseQueryContact {

	@SerializedName("BaseResponse")
	private ResponseBase baseResponse;

	@SerializedName("Count")
	private int count;

	@SerializedName("ContactList")
	private List<WXContact> contactList;

	public void setBaseResponse(ResponseBase baseResponse){
		this.baseResponse = baseResponse;
	}

	public ResponseBase getBaseResponse(){
		return baseResponse;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setContactList(List<WXContact> contactList){
		this.contactList = contactList;
	}

	public List<WXContact> getContactList(){
		return contactList;
	}

	@Override
 	public String toString(){
		return 
			"ResponseBatchGetContact{" + 
			"baseResponse = '" + baseResponse + '\'' + 
			",count = '" + count + '\'' + 
			",contactList = '" + contactList + '\'' + 
			"}";
		}
}