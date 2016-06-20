package easyrun.bean;

public class Athlete {
	public static enum State
	{
		origin, succeed, failed;
		//�����ɹ����ʼĬ�ϣ�origin=0; �к����ƣ�succeed=1; ����ʧ�ܣ�failed=2
	}
	private int EventID;
	private String UserID = "";
	private String AthleteID = "";
	private State state;
	public int getEventID() {
		return EventID;
	}
	public void setEventID(int eventID) {
		EventID = eventID;
	}
	
	public String getAthleteID() {
		return AthleteID;
	}
	public void setAthleteID(String athleteID) {
		AthleteID = athleteID;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	
}
