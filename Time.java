import java.io.IOException;
import java.util.*;

/*
 * TODO: bug15: Hey guys, we should totally make a hashtable of times so that we dont create the same time object 80 times
 * TODO: bug16: Fuck. We need to manage our DayOfWeek/Time fields manually; we can't use the Calendar object. Ask me why i would LOVE to tell you. Fuck. -MCM
 */

//===================================================================
//Time
//againessmith13, jpham14, mmillian15, cbottum15
//Fall 2013
//===================================================================

//===================================================================
//Our Time class that holds a block of time where a given course will take place
public class Time implements Comparable<Time>{
// ===================================================================

	
    // ===============================================================
    // DATA MEMBERS
    // ===============================================================

	// ===============================================================
    // The day of the week, starting hour and minute, ending hour and minute,
	// And number of half-hour blocks each course takes up, and Class ID (Token)
	int dayOfWeek; //0-4
	int startHour;
	int startMinute;
	int endHour;
	int endMinute;
	int blocks; 
	String token;
    // ===============================================================

    // ===============================================================
    // The constructor.
	public Time(String dow, String start,String startPM, String end, String endPM) {//throws InstantiationException{
		//Set the day of the week for the time segment
		this.dayOfWeek=convertDayOfWeek(dow);
		
		//Check to confirm valid Day Of Week
		//if(dayOfWeek==-1){
		//	throw new InstantiationException();
		//}
		//Break up the start and end times into hour and minute 
		String[]s=start.split(":");
		String[]e=end.split(":");
		
		//Create the hour and minute blocks for the start and end
		this.startHour=Integer.parseInt(s[0]);
		if (startPM.equals("PM")){this.startHour+=12;}
		this.startMinute=Integer.parseInt(s[1]);
		this.endHour=Integer.valueOf(e[0]);
		if (endPM.equals("PM")){this.endHour+=12;}
		this.endMinute=Integer.valueOf(e[1]);
		
		//Establish the unique token for a given time segment
		this.token=dayOfWeek+start+end;
	}// Time
    // ===============================================================
	
	// ===============================================================
    // Take a string and determine which day of the week our time takes place in
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
	}// convertDayOfWeek
    // ===============================================================
	
	// ===============================================================
    // Determine whether the buffered data forms a complete frame.
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
		//TODO: I think this can be a one-liner -MCM
	}// overlaps
    // ===============================================================
	
	// ===============================================================
	// returns negative if one is earlier than two
	// returns positive if one is later than two
	// returns 0 for same start hour and minute
	public int compareTo(Time t) {

		if (this.startHour==t.startHour){
			return this.startMinute-t.startMinute;
		}
		
		return this.startHour-t.startHour;
	}// compareTo
    // ===============================================================

	// ===============================================================
	// return dayOfWeek
	public int getDayOfWeek() {
		return dayOfWeek;
	}// getDayOfWeek
    // ===============================================================

	// ===============================================================
	// set dayOfWeek
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}// setDayOfWeek
    // ===============================================================

	// ===============================================================
	// return startHour
	public int getStartHour() {
		return startHour;
	}// getStartHour
    // ===============================================================

	// ===============================================================
	// set startHour
	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}// setStartHour
    // ===============================================================

	// ===============================================================
	// return startMinute
	public int getStartMinute() {
		return startMinute;
	}// getStartMinute
    // ===============================================================

	// ===============================================================
	// set startMinute
	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}// setStartMinute
    // ===============================================================

	// ===============================================================
	// return endHour
	public int getEndHour() {
		return endHour;
	}// getEndHour
    // ===============================================================

	// ===============================================================
	// set endHour
	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}// setEndHour
    // ===============================================================

	// ===============================================================
	// return endMinute
	public int getEndMinute() {
		return endMinute;
	}// getEndMinute
    // ===============================================================

	// ===============================================================
	// set endMinute
	public void setEndMinute(int endMinute) {
		this.endMinute = endMinute;
	}// setEndMinute
    // ===============================================================

	// ===============================================================
	// return blocks
	public int getBlocks() {
		return blocks;
	}// getBlocks
    // ===============================================================

	// ===============================================================
	// set blocks
	public void setBlocks(int blocks) {
		this.blocks = blocks;
	}// setBlocks
    // ===============================================================

	// ===============================================================
	// return token
	public String getToken() {
		return token;
	}// getToken
    // ===============================================================

	// ===============================================================
	// set token
	public void setToken(String token) {
		this.token=token;
	}// setToken
    // ===============================================================

// ===================================================================
} // class Time
// ===================================================================