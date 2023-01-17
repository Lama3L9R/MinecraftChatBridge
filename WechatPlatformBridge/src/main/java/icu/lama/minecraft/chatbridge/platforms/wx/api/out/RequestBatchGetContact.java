package icu.lama.minecraft.chatbridge.platforms.wx.api.out;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXContactQueryItem;
import icu.lama.minecraft.chatbridge.platforms.wx.api.out.RequestBase;

public class RequestBatchGetContact{

	@SerializedName("BaseRequest")
	private RequestBase baseRequest;

	@SerializedName("List")
	private List<WXContactQueryItem> list;

	@SerializedName("Count")
	private int count;

	public RequestBatchGetContact(RequestBase baseRequest, List<WXContactQueryItem> list) {
		this.baseRequest = baseRequest;
		this.list = list;
		this.count = list.size();
	}

	public void setBaseRequest(RequestBase baseRequest){
		this.baseRequest = baseRequest;
	}

	public RequestBase getBaseRequest(){
		return baseRequest;
	}

	public void setList(List<WXContactQueryItem> list){
		this.list = list;
	}

	public List<WXContactQueryItem> getList(){
		return list;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	@Override
 	public String toString(){
		return 
			"RequestBatchGetContact{" + 
			"baseRequest = '" + baseRequest + '\'' + 
			",list = '" + list + '\'' + 
			",count = '" + count + '\'' + 
			"}";
		}
}