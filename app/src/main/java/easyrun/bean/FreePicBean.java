package easyrun.bean;

public class FreePicBean
{
	private int eventID = 0;
	private String userID = "0";
	private String picID = "0";
	private int downloadCnt = 0;
	private long upTime = 0;
	private String userName = "0";
	private String eventName = "0";
	private String headImgUrl ="0"; // 用户头像链接

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
