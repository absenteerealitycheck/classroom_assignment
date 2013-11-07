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
	String token;

	public Time(String dayOfWeek, String start, String end){
		this.dayOfWeek=convertDayOfWeek(dayOfWeek);
		String[]s=start.split(":");
		String[]e=end.split(":");
		this.startHour=Integer.parseInt(s[0]);
		this.startMinute=Integer.parseInt(s[1]);
		this.endHour=Integer.valueOf(e[0]);
		this.endMinute=Integer.valueOf(e[1]);
		this.token=dayOfWeek+start+end;
	}
	public enum Days{
		M,T,W,H,F
	}
	private int convertDayOfWeek(String dow){
	
		switch(Days.valueOf(dow)){
		case M: return 0;
		case T: return 1;
		case W: return 2;
		case H: return 3;
		case F: return 4;
		
		}
		return -1;
	}
	
	public boolean overlaps(Time t){
		
		return false;
	}

	public int compareTo(Time t) {
		//returns negative if one is earlier than two
		//returns positive if one is later than two
		//returns 0 for same start hour and minute
		if (this.startHour==t.startHour){
			return this.startMinute-t.startMinute;
		}
		
		return this.startHour-t.startHour;
	}
}