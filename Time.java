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

	public Time(String dayOfWeek, String start,String startPM, String end, String endPM){
		this.dayOfWeek=convertDayOfWeek(dayOfWeek);
		String[]s=start.split(":");
		String[]e=end.split(":");
		this.startHour=Integer.parseInt(s[0]);
		if (startPM.equals("PM")){this.startHour+=12;}
		this.startMinute=Integer.parseInt(s[1]);
		this.endHour=Integer.valueOf(e[0]);
		if (endPM.equals("PM")){this.endHour+=12;}
		this.endMinute=Integer.valueOf(e[1]);
		this.token=dayOfWeek+start+end;
	}
	
	private int convertDayOfWeek(String dow){
	
		if(dow.equals("M")){
			return 0;
		}
		else if(dow.equals("T")){
			return 1;
		}
		else if(dow.equals("W")){
			return 2;
		}
		else if(dow.equals("H")){
			return 3;
		}
		else if(dow.equals("F")){
			return 4;
		}
		
		return -1;
	}
	
	public boolean overlaps(Time t){
		if (this.startHour<t.startHour){
			if ((this.startHour+this.blocks)>t.getStartHour()){
				return true;
			} else return false;
		} else {
			if ((t.getStartHour()+t.getBlocks())>this.startHour){
				return true;
			}
			return false;
		}
		//I think this can be a one-liner -MCM
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
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public int getStartHour() {
		return startHour;
	}
	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}
	public int getStartMinute() {
		return startMinute;
	}
	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}
	public int getEndHour() {
		return endHour;
	}
	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}
	public int getEndMinute() {
		return endMinute;
	}
	public void setEndMinute(int endMinute) {
		this.endMinute = endMinute;
	}
	public int getBlocks() {
		return blocks;
	}
	public void setBlocks(int blocks) {
		this.blocks = blocks;
	}
}