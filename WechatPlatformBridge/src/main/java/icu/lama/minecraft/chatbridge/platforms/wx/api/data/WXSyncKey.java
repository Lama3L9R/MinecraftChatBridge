package icu.lama.minecraft.chatbridge.platforms.wx.api.data;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.annotations.SerializedName;

public class WXSyncKey{

	@SerializedName("List")
	private List<WXSyncKeyEntry> list;

	@SerializedName("Count")
	private int count;

	public void setList(List<WXSyncKeyEntry> list){
		this.list = list;
	}

	public List<WXSyncKeyEntry> getList(){
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
		return list.stream().map(it -> "" + it.key + "_" + it.val).collect(Collectors.joining("|"));
	}

	public static class WXSyncKeyEntry {

		@SerializedName("Val")
		private int val;

		@SerializedName("Key")
		private int key;

		public void setVal(int val){
			this.val = val;
		}

		public int getVal(){
			return val;
		}

		public void setKey(int key){
			this.key = key;
		}

		public int getKey(){
			return key;
		}

		@Override
		 public String toString(){
			return
				"ListItem{" +
				"val = '" + val + '\'' +
				",key = '" + key + '\'' +
				"}";
			}
	}
}