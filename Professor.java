import java.util.*;
import java.lang.*;
import java.io.*;
public class Professor{
	
	private String name;
	//does it make sense to make this a room, or should it just be a string
	//if its a room you'll have to pass new Room(...) to the constructor or addOffice method
	//i feel like it will be easier to compare rooms with r.inSameBuilding(r2) than to compare strings to room
    private ArrayList<Room> offices; 
    private ArrayList<String> department;
    private Time prefTimes;
    //private boolean stairs; //I don't think we need this, we can probably roll it into preferredRooms
    private ArrayList<Course> courses;
    
    public Professor(){}

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getDepartment() {
		return department;
	}

	public void addDepartment(String department) {
		this.department.add(department);
	}

	public ArrayList<Course> getCourses() {
		return courses;
	}

	public void addCourse(Course course) {
		this.courses.add(course);
	}


}