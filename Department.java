// ===================================================================
// Department
// Us
// Fall 2013
// ===================================================================


import java.util.*;

//===================================================================
// A Department of Amherst College which keeps track of which rooms are
// Allowed to be used for a specific set of courses, as well as
// The professors of that Department.
public class Department {
// ===================================================================
	
	// ===============================================================
    // DATA MEMBERS
    // ===============================================================



    // ===============================================================
    // The long and short names of each department,
	// As well as the rooms, courses, and professors of that department
	private String longName;
	private String shortName;
	private ArrayList<Room> rooms;
	private ArrayList<Course> courses;
	private ArrayList<Professor> professors;
    // ===============================================================

	// ===============================================================
    // The basic constructor, used when only given the name of a department.
	public Department(String l, String s){
		longName=l;
		shortName=s;
		rooms=new ArrayList<Room>();
		courses=new ArrayList<Course>();
		professors=new ArrayList<Professor>();
	}// Department
    // ===============================================================

	// ===============================================================
    // The full constructor
	public Department(String l, String s, ArrayList<Room> r, ArrayList<Course> c, ArrayList<Professor>p){
		longName=l;
		shortName=s;
		rooms=r;
		courses=c;
		professors=p;
	}// Department
    // ===============================================================

	// ===============================================================
    // return longName
	public String getLongName() {
		return longName;
	}// getLongName
    // ===============================================================

	// ===============================================================
    // set longName
	public void setLongName(String longName) {
		this.longName = longName;
	}// setLongName
    // ===============================================================

	// ===============================================================
    // return shortName
	public String getShortName() {
		return shortName;
	}// getShortName
    // ===============================================================

	// ===============================================================
    // set shortName
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}// setShortName
    // ===============================================================

	// ===============================================================
    // return rooms
	public ArrayList<Room> getRooms() {
		return rooms;
	}// getRooms
    // ===============================================================

	// ===============================================================
    // set rooms
	public void setRooms(ArrayList<Room> rooms) {
		this.rooms = rooms;
	}// setRooms
    // ===============================================================

	// ===============================================================
    // add a room to the ArrayList
	public void addRoom(Room r){rooms.add(r);}// addRoom
    // ===============================================================

	// ===============================================================
    // return courses
	public ArrayList<Course> getCourses() {
		return courses;
	}// getCourses
    // ===============================================================

	// ===============================================================
    // set courses
	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}// setCourses
    // ===============================================================

	// ===============================================================
    // add a course to the ArrayList
	public void addCourse(Course c){courses.add(c);}// addCourse
    // ===============================================================

	// ===============================================================
    // return professors
	public ArrayList<Professor> getProfessors() {
		return professors;
	}// getProfessors
    // ===============================================================

	// ===============================================================
    // set professors
	public void setProfessors(ArrayList<Professor> professors) {
		this.professors = professors;
	}// setProfessors
    // ===============================================================

	// ===============================================================
    // add a profesor to the ArrayList
	public void addProfessor(Professor p){professors.add(p);}// addProfessor
    // ===============================================================

		
// ===================================================================
} // class Department
// ===================================================================

