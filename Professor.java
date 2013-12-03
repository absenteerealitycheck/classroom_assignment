// ========================================================
// Professor
// againessmith13, jpham14, mmillian15, cbottum15
// Fall 2013
// ========================================================

import java.util.*;
import java.lang.*;
import java.io.*;

// ========================================================
// Stores professor information for the room assignment
// problem
public class Professor{
// ========================================================
	
	private String name;
    private ArrayList<Room> pastRooms; 
    private ArrayList<String> department;
    private ArrayList<Time> prefTimes;
     private ArrayList<Course> courses;
    private boolean needsAccess;
    
    // ====================================================
    // Name Only Constructor
    /**
     * 
     * @param s - professor name 
     */
    public Professor(String s){
    	name=s;
    	this.pastRooms=new ArrayList<Room>(5); //not actually going to build each room, just going to embed building in fake room
    	this.department=new ArrayList<String>(5);
    	this.prefTimes=new ArrayList<Time>(10);
    	this.courses= new ArrayList<Course>(5);
    	needsAccess=false;
    }
    // ====================================================
    
    // ====================================================
    // Constructor - adds offices, associated departments
    // and accessibility requirements
    /**
     * 
     * @param s - create a new professor for this name
     * @param r - add offices
     * @param d - add associated departments
     */
    public Professor(String s, ArrayList<Room> r, ArrayList<String> d){    	
    	// create a new Professor object with previous constructor
    	this(s);
    	pastRooms=r;
    	department=d;
    	// dummy setter for access
    	needsAccess=false;
    }
    // ====================================================
    // Constructor - adds preferred times, courses taught
    // by this professor, and sets access
    /**
     * 
     * @param s - professor name
     * @param r - pastRooms
     * @param d - departments 
     * @param t - preferredTimes
     * @param c - courses taught
     * @param b - accessibility setter
     */
    public Professor(String s, ArrayList<Room> r, ArrayList<String> d, ArrayList<Time> t, ArrayList<Course> c, boolean b){
    	// create a new Professor object using previous constructor
    	this(s,r,d);
    	prefTimes=t;
    	courses=c;
    	needsAccess=b;
    }
    // ====================================================
    
    // ====================================================
    // Getters and Setters
    /**
     * 
     * @return professor name
     */
    public String getName() {
		return name;
	}

    /**
     * 
     * @param name given to professor object
     */
	public void setName(String name) {
		this.name = name;
	}
	
	public void addRooms(Room r) {
		this.pastRooms.add(r);
	}
	
	public ArrayList<Room> getRooms() {
		return this.pastRooms;
	}

	/**
	 * 
	 * @return departments associated with this professor
	 */
	public ArrayList<String> getDepartment() {
		return department;
	}

	/**
	 * 
	 * @param department added to associated departments list
	 */
	public void addDepartment(String department) {
		this.department.add(department); 
	}
	
	/**
	 * 
	 * @param d is added to list of associated departments
	 */
	public void setDepartment(ArrayList<String> d){
		department=d;
	}

	/**
	 * 
	 * @return courses taught by this professor
	 */
	public ArrayList<Course> getCourses() {
		return courses;
	}

	/**
	 * 
	 * @param course added to courses taught by this professor
	 */
	public void addCourse(Course course) {
		this.courses.add(course);
	}
	
	/**
	 * 
	 * @param c is an ArrayList that sets courses
	 * taught by this professor
	 */
	public void setCourse(ArrayList<Course> c){
		courses=c;
	}
	
	/**
	 * 
	 * @return preferred times for professor
	 */
	public ArrayList<Time> getTime(){
		return prefTimes;
	}
	
	/**
	 * 
	 * @param t added to preferred time list
	 */
	public void addTime(Time t){
		prefTimes.add(t);
	}
	
	/**
	 * 
	 * @param t is an ArrayList that sets preferred times
	 * for this professor
	 */
	public void setTime(ArrayList<Time> t){
		prefTimes=t;
	}
	
	/**
	 * 
	 * @return whether or not this professor has an 
	 * accessibility requirement
	 */
	public boolean isNeedsAccess() {
		return needsAccess;
	}
	
	/**
	 * 
	 * @param needsAccess sets whether the professor
	 * has an accessibility requirement
	 */
	public void setNeedsAccess(boolean needsAccess) {
		this.needsAccess = needsAccess;
	}
	
	// returns professors name - calls getName above
	public String toString(){
		return this.getName();
	}
	// ====================================================
	
// ========================================================
}// Professor
// ========================================================