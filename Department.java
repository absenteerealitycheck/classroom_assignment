import java.util.*;
public class Department {
	private String longName;
	private String shortName;
	private ArrayList<Room> rooms;
	private ArrayList<Course> courses;
	private ArrayList<Professor> professors;
	
	public Department(String l, String s){
		longName=l;
		shortName=s;
		rooms=new ArrayList<Room>();
		courses=new ArrayList<Course>();
		professors=new ArrayList<Professor>();
	}
	public Department(String l, String s, ArrayList<Room> r, ArrayList<Course> c, ArrayList<Professor>p){
		longName=l;
		shortName=s;
		rooms=r;
		courses=c;
		professors=p;
	}
	public String getLongName() {
		return longName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public ArrayList<Room> getRooms() {
		return rooms;
	}
	public void setRooms(ArrayList<Room> rooms) {
		this.rooms = rooms;
	}
	public void addRoom(Room r){rooms.add(r);}
	public ArrayList<Course> getCourses() {
		return courses;
	}
	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}
	public void addCourse(Course c){courses.add(c);}
	public ArrayList<Professor> getProfessors() {
		return professors;
	}
	public void setProfessors(ArrayList<Professor> professors) {
		this.professors = professors;
	}
	public void addProfessor(Professor p){professors.add(p);}
	
	
}
