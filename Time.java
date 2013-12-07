import java.io.IOException;
import java.util.*;

//===================================================================
//Time
//againessmith13, jpham14, mmillian15, cbottum15
//Fall 2013
//===================================================================

//===================================================================
//Our Time class that holds a block of time where a given course will take place
public final class Time implements Comparable<Time>{
// ===================================================================

	// ===============================================================
	int dayOfWeek; //0-4
	int startTime; //use "military" time, i.e. 8:00am = 0800 = 800
	boolean isHalfHour;
	ArrayList<Course> courses= new ArrayList<Course>(); 
    // ===============================================================

    // ===============================================================
    /**
     * Allocates a new Time object which represents a half hour block of time.
     * @param dow - the day of the week
     * @param start - the start hour
     * @param halfHour - if this should be a half hour
     */
	public Time(int dow, int start, boolean halfHour) {
		this.dayOfWeek=dow;
		this.startTime=start;
		this.isHalfHour=halfHour;
	}
    // ===============================================================


	// ===============================================================
	public int getDayOfWeek() {
		return dayOfWeek;
	}
    // ===============================================================

	
	// ===============================================================
	public int getStartTime() {
		return (isHalfHour)?startTime+30:startTime;
	}
    // ===============================================================
	
	
	// ===============================================================
	public ArrayList<Course> getCourses() {
		return this.courses;
	}
	
	public void setCourses(ArrayList<Course> c) {
		this.courses=c;
	}
	public void addCourse(Course c){
		this.courses.add(c);
	}
	
	public String toString() {
		return (dayOfWeek+": "+this.getStartTime());
	}

	/**
	 * Compares two Time objects temporally.
	 * @param t - the Time to be compared.
	 * @return the value 0 if this Integer is equal to the argument Integer; a value less than 0 
	 * if this Integer is numerically less than the argument Integer; and a value greater than 0 
	 * if this Integer is numerically greater than the argument Integer (signed comparison).
	 */
	public int compareTo(Time t) {
		return (new Integer(this.getStartTime()+(this.getDayOfWeek()*10000))).compareTo(new Integer(t.getStartTime()+(this.getDayOfWeek()*10000)));
	}
	
// ===================================================================
} // class Time
// ===================================================================