package easyrun.bean;

import android.os.Parcel;
import android.os.Parcelable;


public class EventBean implements Parcelable
{

	private int eventID = -1;
	private String eventName ;
	private int eventStatus;
	public int describeContents() {return 0;}
	public void writeToParcel(Parcel out, int flags)
	{
		out.writeInt(eventID);
	}
	public static final Parcelable.Creator<EventBean> CREATOR = new Parcelable.Creator<EventBean>()
	{
		public EventBean createFromParcel(Parcel in)
		{
			return new EventBean(in);
		}

		public EventBean[] newArray(int size)
		{
			return new EventBean[size];
		}
	};
	public EventBean(){}
	private EventBean(Parcel in)
	{
		eventID=in.readInt();
		eventName=in.readString();
	}
	public int getEventID()
	{
		return eventID;
	}
	public void setEventID(int eventID)
	{
		this.eventID = eventID;
	}
	public String getEventName()
	{
		return eventName;
	}
	public void setEventName(String eventName)
	{
		this.eventName = eventName;
	}
	public int getEventStatus()
	{
		return eventStatus;
	}
	public void setEventStatus(int eventStatus)
	{
		this.eventStatus = eventStatus;
	}


	
}
