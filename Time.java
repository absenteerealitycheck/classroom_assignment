import java.io.IOException;
import java.util.*;

/*
 * TODO: bug15: Hey guys, we should totally make a hashtable of times so that we dont create the same time object 80 times
 * TODO: bug16: Fuck. We need to manage our DayOfWeek/Time fields manually; we can't use the Calendar object. Ask me why i would LOVE to tell you. Fuck. -MCM
 */

public class Time{
   
    private Calendar eventTime;
	private boolean isStartTime;

	public Time(String t,boolean isStartTime){
		this.setEventTime(new GregorianCalendar());
		this.isStartTime=isStartTime;
		this.setTime(t);
	}
	
    public Time(char dow, String t,  boolean isStartTime){
    	this(t,isStartTime);
    	System.out.println("\t"+dow);
    	this.setDayOfWeek(dow);
    	//parse the shit out of s to turn it into a Calendar
    }
    public Time(char[] dow, String t,  boolean isStartTime){
    	this(t,isStartTime);
    	this.setDayThurs(dow);
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
		if(ampm.equals("PM")){
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
    
    public int getDay() {
	    int d = eventTime.get(Calendar.DAY_OF_WEEK);
	    return d;
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

	public int getMilitaryHour(){
		int t = this.eventTime.get(Calendar.HOUR);
		if(this.eventTime.get(Calendar.AM_PM)==1) {
			if(t<12) t=t+12;
		}
		else if (this.eventTime.get(Calendar.AM_PM)==0){
			if(t==0)t=t+12;
		}
		else System.out.println("You dun fucked up");
		return t;
	}
	
	public String toString(){
		int h=this.eventTime.get(Calendar.HOUR);
		int m=this.eventTime.get(Calendar.MINUTE);
		String min="";
		String hour="";
		if(h==0){
			hour="12";
		}
		else{
			hour=Integer.toString(h);
		}
		if(m==0){
			min="00";
		}
		else{
			min=Integer.toString(m);
		}
		if(this.isStartTime){
			return ""+this.eventTime.get(Calendar.DAY_OF_WEEK);
		}
			//return this.eventTime.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.SHORT, Locale.US)+" "+hour+":"+min;}
		else
			return hour+":"+min;
	}
}