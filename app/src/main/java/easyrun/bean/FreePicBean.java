package easyrun.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class FreePicBean implements Parcelable
{
	private int eventID = 0;
	private String userID = "0";
	private String picID = "0";
	private int downloadCnt = 0;
	private long upTime = 0;
	private String userName = "0";
	private String eventName = "0";
	private String headImgUrl ="0"; // 用户头像链接

	public int describeContents() {return 0;}

	public void writeToParcel(Parcel out, int flags)
	{
		out.writeString(picID);
	}

	public static final Parcelable.Creator<FreePicBean> CREATOR = new Parcelable.Creator<FreePicBean>()
	{
		public FreePicBean createFromParcel(Parcel in)
		{
			return new FreePicBean(in);
		}

		public FreePicBean[] newArray(int size)
		{
			return new FreePicBean[size];
		}
	};

	public FreePicBean(){}

	private FreePicBean(Parcel in)
	{
		userID = in.readString();
		headImgUrl = in.readString();
		eventID = in.readInt();
		picID = in.readString();
		downloadCnt = in.readInt();
		upTime = in.readLong();
		userName = in.readString();
		eventName = in.readString();
	}

	public Bitmap getPic() {
		return pic;
	}

	public void setPic(Bitmap pic) {
		this.pic = pic;
	}

	private Bitmap pic=null;

	public int getEventID()
	{
		return eventID;
	}

	public void setEventID(int eventID)
	{
		this.eventID = eventID;
	}

	public String getPicID()
	{
		return picID;
	}

	public void setPicID(String picID)
	{
		this.picID = picID;
	}

	public int getDownloadCnt()
	{
		return downloadCnt;
	}

	public void setDownloadCnt(int downloadCnt)
	{
		this.downloadCnt = downloadCnt;
	}

	public long getUpTime()
	{
		return upTime;
	}

	public void setUpTime(long upTime)
	{
		this.upTime = upTime;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}



}
