import java.util.Calendar;
import java.util.GregorianCalendar;

public class Time{
   
    private Calendar eventTime;
	private boolean isStartTime;

    public Time(char dow, String t,  boolean isStartTime){
    	
    	this.setEventTime(new GregorianCalendar());
    	this.isStartTime=isStartTime;
    	this.setDayOfWeek(dow);
    	this.setTime(t);
    	//parse the shit out of s to turn it into a Calendar
    }
    public Time(char[] dow, String t,  boolean isStartTime){
    	this.setEventTime(new GregorianCalendar());
    	this.isStartTime=isStartTime;
    	this.setDayThurs(dow);
    	this.setTime(t);
    	//parse the shit out of s to turn it into a Calendar
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
	public void setTime(String time){
		String[] timepieces=time.split(":",2);
		
		String ampm=timepieces[1].substring(timepieces[1].length()-2);
		String min= timepieces[1].substring(0, 2);
		if(ampm=="PM"){
		eventTime.set(Calendar.AM_PM, Calendar.PM);
		}
		else{
			eventTime.set(Calendar.AM_PM, Calendar.AM);
		}
		eventTime.set(Calendar.HOUR, Integer.parseInt(timepieces[0]));
		eventTime.set(Calendar.MINUTE, Integer.parseInt(min));
	}

    public int getHour() {
	int h = eventTime.get(Calendar.HOUR);
	if (h==0) h=12;
	return h;

    }

    public int getMinute() {
	int m = eventTime.get(Calendar.MINUTE);
      	return m;
    }
	public void setDayThurs(char[]c){
		eventTime.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
	}
	public void setDayOfWeek(char c){
		
			if(c=='M'){
				eventTime.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
			}
			if(c=='W'){
				eventTime.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
			}
			if(c=='F'){
				eventTime.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
			}
			if(c=='T'){
				
					eventTime.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
				
			}
		
	}
}