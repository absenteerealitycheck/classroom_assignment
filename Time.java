import java.util.Calendar;

public class Time{
   
    private Calendar eventTime;
	private boolean isStartTime;

    public Time(String s){

    }
   
    public Calendar getEventTime() {
		return eventTime;
	}

	public void setEventTime(Calendar eventTime) {
		this.eventTime = eventTime;
	}

	public boolean isStartTime() {
		return isStartTime;
	}

	public void setStartTime(boolean isStartTime) {
		this.isStartTime = isStartTime;
	}
}