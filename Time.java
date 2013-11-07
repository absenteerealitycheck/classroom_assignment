import java.io.IOException;
import java.util.*;

/*
 * TODO: bug15: Hey guys, we should totally make a hashtable of times so that we dont create the same time object 80 times
 * TODO: bug16: Fuck. We need to manage our DayOfWeek/Time fields manually; we can't use the Calendar object. Ask me why i would LOVE to tell you. Fuck. -MCM
 */



public class Time implements Comparable<Time>{
	int dayOfWeek; //0-4
	int startHour;
	int startMinute;
	int endHour;
	int endMinute;
	int blocks; //number of half hour blocks between start and end
	
	public Time(String dayOfWeek, int startHour, int startMinute, int endHour, int endMinute){
		this.dayOfWeek=convertDayOfWeek(dayOfWeek);
		this.startHour=startHour;
		this.startMinute=startMinute;
		this.endHour=endHour;
		this.endMinute=endMinute;
	}
	public Time(String dayOfWeek, String start, String end){
		this.dayOfWeek=convertDayOfWeek(dayOfWeek);
		String[]s=start.split(":");
		String[]e=end.split(":");
		this.startHour=Integer.parseInt(s[0]);
		this.startMinute=Integer.parseInt(s[1]);
		this.endHour=Integer.valueOf(e[0]);
		this.endMinute=Integer.valueOf(e[1]);
	}
	
	private int convertDayOfWeek(String dow){
		return -1;
	}
	
	public boolean overlaps(Time t){
		
		return false;
	}

	public int compareTo(Time t) {
		return 0;
	}
}