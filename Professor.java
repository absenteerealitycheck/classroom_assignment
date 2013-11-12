import java.util.*;
import java.lang.*;
import java.io.*;

public class Professor{
	
	private String name;
	//does it make sense to make this a room, or should it just be a string
	//if its a room you'll have to pass new Room(...) to the constructor or addOffice method
	//i feel like it will be easier to compare rooms with r.inSameBuilding(r2) than to compare strings to room
	
	//I feel like leaving it as rooms would probably be easier -cbb
    private ArrayList<Room> offices; 
    private ArrayList<String> department;
    private ArrayList<Time> prefTimes; //was a single time but made it an ArrayList in case there are multiple -cbb
    //private boolean stairs; //I don't think we need this, we can probably roll it into preferredRooms
    private ArrayList<Course> courses;
    private boolean needsAccess;
    
    public Professor(String s){
    	name=s;
    	this.offices=new ArrayList<Room>(5); //not actually going to build each room, just going to embed building in fake room
    	this.department=new ArrayList<String>(5);
    	this.prefTimes=new ArrayList<Time>(10);
    	this.courses= new ArrayList<Course>(5);
    	needsAccess=false;
    }
    public Professor(String s, ArrayList<Room> r, ArrayList<String> d){    	
    	this(s);
    	offices=r;
    	department=d;
    	needsAccess=false;
    }
    public Professor(String s, ArrayList<Room> r, ArrayList<String> d, ArrayList<Time> t, ArrayList<Course> c, boolean b){
    	this(s,r,d);
    	prefTimes=t;
    	courses=c;
    	needsAccess=b;
    }
    //Accessors and Modifiers Start
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
		this.department.add(department); //jw why are we using this.dep? -cbb
	}
	public void setDepartment(ArrayList<String> d){
		department=d;
	}

	public ArrayList<Course> getCourses() {
		return courses;
	}

	public void addCourse(Course course) {
		this.courses.add(course);
	}
	public void setCourse(ArrayList<Course> c){
		courses=c;
	}
	
	public ArrayList<Time> getTime(){
		return prefTimes;
	}
	
	public void addTime(Time t){
		prefTimes.add(t);
	}
	
	public void setTime(ArrayList<Time> t){
		prefTimes=t;
	}
	
	public boolean isNeedsAccess() {
		return needsAccess;
	}
	public void setNeedsAccess(boolean needsAccess) {
		this.needsAccess = needsAccess;
	}
	public String toString(){
		return this.getName();
	}
	
	//Accessors and Modifiers End
}