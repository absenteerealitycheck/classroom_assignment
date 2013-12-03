import java.io.IOException;
import java.util.*;

/*
 * TODO: bug15: Hey guys, we should totally make a hashtable of times so that we dont create the same time object 80 times
 * TODO: bug16: Fuck. We need to manage our DayOfWeek/Time fields manually; we can't use the Calendar object. Ask me why i would LOVE to tell you. Fuck. -MCM
 * TODO: bug 20: Rewrite Time so that we can construnct ~400 Time objects of half hour blocks between M and F and add a list of times to every course
 * 
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
    /**
     * Allocates a new Time object 
     * @param dow
     * @param start
     * @param startsInPM
     * @param end
     * @param endsInPM
     */
	public Time(String dow, String start,String startsInPM, String end, String endsInPM) {//throws InstantiationException{
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
		if (startsInPM.equals("PM")){this.startHour+=12;}
		this.startMinute=Integer.parseInt(s[1]);
		this.endHour=Integer.valueOf(e[0]);
		if (endsInPM.equals("PM")){this.endHour+=12;}
		this.endMinute=Integer.valueOf(e[1]);
		
		//Establish the unique token for a given time segment
		this.token=dayOfWeek+start+end;
	}// Time
    // ===============================================================
	
	// ===============================================================
    // 
	/**
	 * Converts a String representation of the day of week and into an 
	 * int representation using 0 for Monday, 4 for Friday, and -1 for
	 * Saturday or Sunday.
	 * @param dow - String representation of the day of the week
	 * @return int representation of the day of the week 
	 */
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
    //
	/**
	 * Returns true if this Time overlaps with the specified Time. More 
	 * formally, returns true if and only if TODO:write this logic
	 * @param t - Time to compare to
	 * @return true if this Time overlaps the specified Time.
	 */
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
	// returns negative if one is earlier than two// returns positive if one is later than two// returns 0 for same start hour and minute
	/**
	 * 
	 * @param t 
	 * @return
	 */
	public int compareTo(Time t) {

		if (this.startHour==t.startHour){
			return this.startMinute-t.startMinute;
		}
		
		return this.startHour-t.startHour;
	}// compareTo
    // ===============================================================

	// ===============================================================
	// return dayOfWeek
	/**
	 * 
	 * @return
	 */
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