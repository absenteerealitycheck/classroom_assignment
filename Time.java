import java.io.IOException;
import java.util.*;

/*
 * TODO: bug15: Hey guys, we should totally make a hashtable of times so that we dont create the same time object 80 times
 * TODO: bug16: Fuck. We need to manage our DayOfWeek/Time fields manually; we can't use the Calendar object. Ask me why i would LOVE to tell you. Fuck. -MCM
 * TODO: bug 20: Rewrite Time so that we can construct ~400 Time objects of half hour blocks between M and F and add a list of times to every course
 *
 * !!instead of a list of times per course, consider list of courses per time - easier graph building - JP
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
    // The day of the week, starting time and end time
	// And an Array List of concurrent courses
	int dayOfWeek; //0-4
	int startTime; //use "military" time, i.e. 8:00am = 0800 = 800
	int endTime;
	//String token;
	ArrayList<Course> courses; 
    // ===============================================================

    // ===============================================================
    /**
     * Allocates a new Time object 
     * @param dow
     * @param start
     * @param end
     */
	public Time(int dow, int start, int end) {//throws InstantiationException{
		//Set the day of the week for the time segment
		this.dayOfWeek=dow;
		
		//Check to confirm valid Day Of Week
		//if(dayOfWeek==-1){
		//	throw new InstantiationException();
		//}

		this.startTime=start;
		this.endTime=end;
		
		//Establish the unique token for a given time segment
		//this.token=dayOfWeek+start+end;
	}// Time
    // ===============================================================

/*	
	// ===============================================================
	// returns negative if one is earlier than two// returns positive if one is later than two// returns 0 for same start hour and minute
	*//**
	 * 
	 * @param t 
	 * @return
	 *//*
	public int compareTo(Time t) {

		if (this.startHour==t.startHour){
			return this.startMinute-t.startMinute;
		}
		
		return this.startHour-t.startHour;
	}// compareTo
    // ===============================================================
*/
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
	// return startTime
	public int getStartTime() {
		return startTime;
	}// getStartTime
    // ===============================================================

	// ===============================================================
	// set startTime
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}// setStartTime
    // ===============================================================

	// ===============================================================
	// return endTime
	public int getEndTime() {
		return endTime;
	}// getEndTime
    // ===============================================================

	// ===============================================================
	// set endTime
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}// setEndTime
    // ===============================================================

	/*// ===============================================================
	// return token
	public String getToken() {
		return token;
	}// getToken
    // ===============================================================
*/
	/*// ===============================================================
	// set token
	public void setToken(String token) {
		this.token=token;
	}// setToken
    // ===============================================================
*/
	
	public ArrayList<Course> getCourses() {
		return this.courses;
	}
	
	public void setCourses(ArrayList<Course> c) {
		this.courses=c;
	}
	
	public String toString() {
		return (dayOfWeek+": "+startTime+", "+endTime);
	}
	
// ===================================================================
} // class Time
// ===================================================================