import java.util.*;
import java.lang.*;
import java.io.*;
public class Driver{
	
	public static void main(String args[]){
		String[][] roomSpreadsheet = new String[100][20];
		//Read csv file into spreadsheet cell by cell
		//probably need to sanitize data as we read it in, i.e. use String currentBuilding
		//does it make sense to store nulls?
		ArrayList<Room> rooms= generateRooms(roomSpreadsheet);
		
		
	}
	
	public Driver(){
		
	}
	
	public static ArrayList<Room> generateRooms(String[][] rS){
		/*However we construct rS, write a visual representation of it below
		 * 
		 * rS:
		 * [BuildingName|...]
		 * [BuildingName|...]
		 * ...
		 * current columns are [buildingName|roomNumber|capacity|type|accessible] - jpham14:10/28
		 * 
		*/
		//after this is just pseudocode. change it to make it work.
		int numberOfRooms=rS.length;
		numberOfRooms=80;
		ArrayList<Room> rooms= new ArrayList<Room>(numberOfRooms);
		for(int row=0; row<rS.length; row++){
			//String type=coalesce(rS[row][2],rS[row][3],rS[row][4]);
			
			String type = rS[row][3] //for our test file, only worrying about 1 type for now - jpham:10/28
	
			//the above is just a guess, assuming rS is formatted [Building|Room|Seminar|SmallClassrm|Lecture|Seats...] 
			//it might make more sense to sanitize and construct rS as [Building|Room|Type|Capacity|...]
			Boolean accessible=Boolean.valueOf(rS[row][4]);
			String buildingName=rS[row][0];
			Integer capacity= Integer.valueOf(rS[row][2]);
			String roomNumber=rS[row][1];
			//these guys can change too. depends how we constuct rS
			//the next two lines should be ok 
			Room r = new Room(accessible,buildingName,capacity,roomNumber,type);
			//eventually implement the following line
			//r.setTech(...)			
			rooms.add(r);
		}		
		rooms.trimToSize();
		return rooms;
	}
	
	public static ArrayList<Professor> generateProfessors(){
		
	}
	
	public static ArrayList<Course> generateCourses(){
		
	}		
	
	public static void linkProfessorsAndCourses(ArrayList<Professor> professors, ArrayList<Course> courses, ArrayList<Tuple<Course,Professor>> pairs){
		//not exactly sure how to implement these loops at this point
		//maybe take a list of <professorName,courseName> pairs and
		for(Tuple t: pairs){
			Course c=courses.getCourseByName(t.getFirst());
			Professor p=professors.getProfessorByName(t.getSecond());
			c.addProfessor(p);
			p.addCourse(c);
		}
	}
	
	public static <T> T coalesce(T a, T b, T c) {
	    return a != null ? a : (b != null ? b : c);
	}
}
