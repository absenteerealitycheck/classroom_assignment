import java.util.*;
import java.lang.*;
import java.lang.reflect.Array;
import java.io.*;
public class Course{
	private String longName;
	private ArrayList<String> shortName; //courses can be cross listed between departments but will have the same longName I think
	private ArrayList<String> department; // ^ for example, {CS321,MATH232}
    private ArrayList<Time> preferredTimes;
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

    public Course(int capacity, String name, String type){
    	this.capacity=capacity;
    	this.longName=name;
    	
    	this.concurrent = new ArrayList<Course>(10);
    	this.noncurrent = new ArrayList<Course>(10);
    	
      	this.preferredRooms= new ArrayList<Room>(50);
      	this.shortName=new ArrayList<String>(5);
      	this.preferredTimes = new ArrayList<Time>(10);
      	this.professors = new ArrayList<Professor>(2);
            	
    	discussionCourse=labCourse=lectureCourse=seminarCourse=false;
    	if (type.equals("discussion")){discussionCourse=true;}
    	if (type.equals("lab")){labCourse=true;}
    	if (type.equals("lecture")){lectureCourse=true;}
    	if (type.equals("seminar")){seminarCourse=true;}
    }
//make field for do not scehcule, last column will have that  vale when initializing just continue
    
	public Room getAssignment() {
		return assignment;
	}

	public void setAssignment(Room assignment) {
		this.assignment = assignment;
	}

	public ArrayList<Time> getPreferredTimes() {
		return preferredTimes;
	}
	
	public int getNumberOfPreferredTimes(){
		return this.preferredTimes.size();
	}
	
	public void addPreferredTime(Time preferredTime) {
		this.preferredTimes.add(preferredTime);
	}
	
	public ArrayList<Room> getPreferredRooms() {
		return this.preferredRooms;
	}

	public void addPreferredRoom(Room preferredRoom) {
		this.preferredRooms.add(preferredRoom);
	}

	public void addPreferredRoomsList(ArrayList<Room> pR){
		for (Room r:pR){
			addPreferredRoom(r);
		}
	}

	public ArrayList<Course> getConcurrent() {
		return concurrent;
	}

	public void addConcurrent(Course concurrent) {
		this.concurrent.add(concurrent);
	}

	public ArrayList<Course> getNoncurrent() {
		return noncurrent;
	}

	public void addNoncurrent(Course noncurrent) {
		this.noncurrent.add(noncurrent);
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public boolean isLabCourse() {
		return labCourse;
	}

	public void setLabCourse(boolean labCourse) {
		this.labCourse = labCourse;
	}

	public boolean isSeminarCourse() {
		return seminarCourse;
	}

	public void setSeminarCourse(boolean seminarCourse) {
		this.seminarCourse = seminarCourse;
	}

	public boolean isLectureCourse() {
		return lectureCourse;
	}

	public void setLectureCourse(boolean lectureCourse) {
		this.lectureCourse = lectureCourse;
	}

	public boolean isDiscussionCourse() {
		return discussionCourse;
	}

	public void setDiscussionCourse(boolean discussionCourse) {
		this.discussionCourse = discussionCourse;
	}
	
	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public ArrayList<String> getShortName() {
		return shortName;
	}

	public void addShortName(String shortName) {
		this.shortName.add(shortName);
	}

	public ArrayList<String> getDepartment() {
		return department;
	}

	public void addDepartment(String department) {
		this.department.add(department);
	}

	public ArrayList<Professor> getProfessors() {
		return professors;
	}

	public void addProfessor(Professor professor) {
		this.professors.add(professor);
	}

	public String getType(){
		if (isDiscussionCourse()){return "Discussion";}
		if (isLabCourse()){return "Lab";}
		if (isLectureCourse()){return "Lecture";}
		if (isSeminarCourse()){return "Seminar";}
		return "Unlisted";
	}
	
	public int getTypeCode(){
		if (isDiscussionCourse()){return -4;}
		if (isLabCourse()){return -1;}
		if (isLectureCourse()){return -2;}
		if (isSeminarCourse()){return -3;}
		return -5;
	}
	
	public String toString(){
		return this.longName+" ("+this.shortName+")";
	}
	
	public void cleanse(){
		for(Room r: preferredRooms){
			if(!r.isAccessible())
				preferredRooms.remove(r);
		}
	}
}