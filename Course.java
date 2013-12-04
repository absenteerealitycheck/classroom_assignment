// ========================================================
// Course.java 
// againessmith13, jpham14, mmillian15, cbottum15
// Fall 2013
// ========================================================

import java.util.*;
import java.lang.*;
import java.lang.reflect.Array;
import java.io.*;

// ========================================================
// Contains information for courses for room assignment 
// problem.
public class Course{
// ========================================================
	
	private String longName;
	private ArrayList<String> shortName; 
	private ArrayList<String> department;
	private ArrayList<Time> preferredTimes;
	private Time time;


	private int capacity;
	private ArrayList<Room> preferredRooms;
	private ArrayList<Professor> professors;
	private boolean discussionCourse;
	private boolean labCourse;
	private boolean lectureCourse;
	private boolean seminarCourse;
	private ArrayList<Course> concurrent;
	private ArrayList<Course> noncurrent;
	private Room assignment;
	private ArrayList<Course> edges;
	private boolean[] tech;//list of tech booleans
	//0=needsProjecter
	//1=needsDVD
	//2=needsVCR
	//3=needsSlides
	//4=needsOverhead
	private int numberOfSlides;

	// ====================================================
	// Constructor
	/**
	 * 
	 * @param capacity - course capacity
	 * @param name - full course title
	 * @param type - course type (lab, seminar, discussion, lecture)
	 */
	public Course(int capacity, String name, String type){
		this.capacity=capacity;
		this.longName=name;
	
		this.preferredRooms= new ArrayList<Room>(50);
		this.shortName=new ArrayList<String>(5);
		this.preferredTimes = new ArrayList<Time>(10);
		this.professors = new ArrayList<Professor>(2);

		// Course type setter.
		discussionCourse=labCourse=lectureCourse=seminarCourse=false;
		if (type.equals("discussion")){discussionCourse=true;}
		if (type.equals("lab")){labCourse=true;}
		if (type.equals("lecture")){lectureCourse=true;}
		if (type.equals("seminar")){seminarCourse=true;}
	}
	// ====================================================

	
	// ====================================================
	// Getters and Setters
	
	public Time getTime() {
		return time;
	}


	public void setTime(Time time) {
		this.time = time;
	}

	
	/**
	 * 
	 * @return all technology requirements
	 */
	public boolean[] getTech() {
		return tech;
	}

	/**
	 * 
	 * @param tech initialized
	 */
	public void setTech(boolean[] tech) {
		this.tech = tech;
	}

	/**
	 * 
	 * @return number of slideshow requirements
	 */
	public int getNumberOfSlides() {
		return numberOfSlides;
	}

	/**
	 * 
	 * @param numberOfSlides initialized
	 */
	public void setNumberOfSlides(int numberOfSlides) {
		this.numberOfSlides = numberOfSlides;
	}

	/**
	 * 
	 * @return preferredRooms list
	 */
	public ArrayList<Room> getPreferredRooms() {
		return this.preferredRooms;
	}
	
	/**
	 * 
	 * @return final room assignment
	 */
	public Room getAssignment() {
		return assignment;
	}

	/**
	 * 
	 * @param assignment set to given room assignment
	 */
	public void setAssignment(Room assignment) {
		this.assignment = assignment;
	}
	
	/**
	 * 
	 * @return course capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * 
	 * @param capacity initialized
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	/**
	 * 
	 * @return preferred times for this course
	 */
	public ArrayList<Time> getPreferredTimes() {
		return preferredTimes;
	}
	
	/**
	 * 
	 * @param preferredTime added to preferredTimes list
	 */
	public void addPreferredTime(Time preferredTime) {
		this.preferredTimes.add(preferredTime);
	}
	
	/**
	 * 
	 * @return number of preferred times
	 */
	public int getNumberOfPreferredTimes(){
		return this.preferredTimes.size();
	}


	/**
	 * 
	 * @return course type (lab, lecture, discussion, seminar)
	 */
	public boolean isLabCourse() {
		return labCourse;
	}

	/**
	 * 
	 * @param labCourse determines whether course is lab 
	 */
	public void setLabCourse(boolean labCourse) {
		this.labCourse = labCourse;
	}

	/**
	 * 
	 * @return true if course is seminar
	 */
	public boolean isSeminarCourse() {
		return seminarCourse;
	}

	/**
	 * 
	 * @param seminarCourse determines whether course is seminar
	 */
	public void setSeminarCourse(boolean seminarCourse) {
		this.seminarCourse = seminarCourse;
	}

	/**
	 * 
	 * @return true if course is lecture
	 */
	public boolean isLectureCourse() {
		return lectureCourse;
	}

	/**
	 * 
	 * @param lectureCourse determines whether course is lecture
	 */
	public void setLectureCourse(boolean lectureCourse) {
		this.lectureCourse = lectureCourse;
	}

	/**
	 * 
	 * @return true if course is discussion
	 */
	public boolean isDiscussionCourse() {
		return discussionCourse;
	}

	/**
	 * 
	 * @param discussionCourse determines whether course is discussion
	 */
	public void setDiscussionCourse(boolean discussionCourse) {
		this.discussionCourse = discussionCourse;
	}
	
	/**
	 * 
	 * @return courses type (discussion, lab, lecture, seminar) or Unlisted
	 */
	public String getType(){
		if (isDiscussionCourse()){return "Discussion";}
		if (isLabCourse()){return "Lab";}
		if (isLectureCourse()){return "Lecture";}
		if (isSeminarCourse()){return "Seminar";}
		return "Unlisted";
	}

	// numeric conversion for course type for constant 
	// search/comparison
	public int getTypeCode(){
		if (isDiscussionCourse()){return -4;}
		if (isLabCourse()){return -1;}
		if (isLectureCourse()){return -2;}
		if (isSeminarCourse()){return -3;}
		return -5;
	}

	// course title/name
	public String getLongName() {
		return longName;
	}

	/**
	 * 
	 * @param longName sets courses full title 
	 */
	public void setLongName(String longName) {
		this.longName = longName;
	}

	/**
	 * 
	 * @return course abbreviation shortName
	 */
	public ArrayList<String> getShortName() {
		return shortName;
	}

	/**
	 * 
	 * @param shortName added to list of course abbreviations
	 */
	public void addShortName(String shortName) {
		this.shortName.add(shortName);
	}

	/**
	 * 
	 * @return associated departments list
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
	 * @return list of professors teaching this course
	 */
	public ArrayList<Professor> getProfessors() {
		return professors;
	}

	/**
	 * 
	 * @param professor added to list of professors 
	 * teaching this course
	 */
	public void addProfessor(Professor professor) {
		this.professors.add(professor);
	}

	// ====================================================
	
	
	
	// ====================================================
	// techFilterRooms
	// Eliminates rooms from preferredRooms is 
	// not compatible based on technology requirements.
	/**
	 * 
	 * @param pR - preferred rooms placeholder
	 * @return list of rooms compatible for technology requirements
	 */
	public ArrayList<Room> techFilterRooms(ArrayList<Room> pR){//checks the room's Tech against the courses Tech needs
		
		// for each room in preferredRooms
		for(Iterator<Room> it = pR.iterator(); it.hasNext();){
			Room r=it.next();
			if(allTrue(this.getTech())&&(!compareArrays(this.getTech(), r.getTechnology()))){
				it.remove();
			}
		}
		
		if(!pR.isEmpty()){
			System.out.println("There are no rooms in that buiding that work");
		}
		return pR;
	}
	
	// ====================================================
	// general boolean array comparison
	// everything true in a1 must be true in a2
	// used by techFilterRooms
	
	public boolean compareArrays(boolean[] a1, boolean[] a2){
		for (int j = 0; j<a1.length; j++) {
		
		   if (a1[j] && !a2[j]) {
		      return false;
		   }
		}
		return true;
	}
	// ====================================================
	
	
	// ====================================================
	// general array checking?
	// checks whether or not every boolean is true
	// used by techFilterRooms

	public  boolean allTrue(boolean[] array) {
	    for(boolean b : array) if(b) return true;
	    return false;
	}
	// ====================================================
	
	
	
	// ====================================================
	// add individual room to preferredRooms
	public void addPreferredRoom(Room preferredRoom) {
		this.preferredRooms.add(preferredRoom);
	}

	// add a list of rooms to preferredRooms
	public void addPreferredRoomsList(ArrayList<Room> pR){
		for (Room r:pR){
			addPreferredRoom(r);
		}
	}
	// ====================================================
	
	
	
	// ====================================================
	// print course name, full course title with abbrev.
	public String toString(){
		return this.longName+" ("+this.shortName+")";
	}
	// ====================================================
	
	
	// ====================================================
	// checks rooms in preferredRooms list for compatibility
	// based on accessibility of course to room 
	public void cleanse(){
		for(Iterator<Room> it=this.getPreferredRooms().iterator();it.hasNext();){
			Room r=it.next();
			if(!r.isAccessible()){
				it.remove();			
			}
		}
	}
	// ====================================================

	
	// ====================================================
	// checks rooms in preferredRooms for compatibility 
	// based on course capacity to room capacity
	public void checkCapacity() { //split into separate methods?
		Room r;
		for (Iterator i = preferredRooms.iterator();i.hasNext();){
			r=(Room) i.next();
			// if room capacity is too small, remove room 
			// from preferredRooms
			if (r.getCapacity() < this.capacity) {
				i.remove();
			}
		}
	}
	// ====================================================
	
	
	// ====================================================
	// check rooms in preferredRooms for compatibility 
	// based on course type = lab, under the premise that
	// labs must be in labs (hard constraint), but other
	// course types are a soft constraint (a seminar can
	// be put in a lecture hall, for example) - 11/18
	
	public void checkLabs() {
		Room r;
		// if this course is a lab, check its preferredRooms
		if (this.getType().toLowerCase().equals("lab")){
			for (Iterator i = preferredRooms.iterator();i.hasNext();){
				r=(Room) i.next();
				// if room in preferred list is a "lab"
				// remove it
				if(!r.getType().toLowerCase().equals("lab")) {
					i.remove();
				}
			}
		}
	}
	// ====================================================
} // class Course
// ========================================================
