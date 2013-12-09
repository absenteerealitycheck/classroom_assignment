// ====================================================================================================================================================================================
// Driver
// againessmith13, jpham14, mmillian15, cbottum15
// Fall 2013 
// ====================================================================================================================================================================================

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

// =====================================================================================================================================================================================
// The driver that loads various .csv files, parses them to construct objects, and runs a heuristic solution
// to the classroom assignment problem.
public class Driver{
	// =====================================================================================================================================================================================

	public HashMap<String,ArrayList<Room>> buildingMap=new HashMap<String,ArrayList<Room>>(40);
	public ArrayList<Course> badCourses= new ArrayList<Course>(15);

	// =================================================================================================================================================================================
	/**
	 *  The entry point. Switch into non-static methods to start the program.
	 * @param args
	 * @throws IOException
	 */
	public static void main (String args[]) throws IOException{
		new Driver().go();

	} // main
	// =================================================================================================================================================================================



	// ================================================================================================================================================================================
	/**
	 *  The real non-static main method
	 * @throws IOException
	 */
	public void go() throws IOException{
		boolean testing=false;
		FileManager fm=new FileManager();
		int phase=2;
		String qux ="";
		switch(phase){
		case 1:
			System.out.println("Starting phase 1");
			qux=qux.concat(fm.addCSVFile(new File("proto-coursehistory.csv"), "historical"));
			break;
		case 2:
			System.out.println("Starting phase 2");
			qux=qux.concat(fm.addCSVFile(new File("proto-roomsandprofslist.csv"), "roomsandprofessors")+",");
			qux=qux.concat(fm.addCSVFile(new File("proto-roomsanddeptslist.csv"), "roomsanddepartments")+",");
			qux=qux.concat(fm.addCSVFile(new File("proto-roomsandcourseslist.csv"), "roomsandcourses")+",");
			qux=qux.concat(fm.addCSVFile(new File("workingCourseList.csv"), "workingcourselist")+",");
			qux=qux.concat(fm.addCSVFile(new File("proto-roomslist.csv"), "workingroomslist"));
			break;
		case 3:
			qux=qux.concat(fm.addCSVFile(new File("proto-recommendedroomslist.csv"), "recommendedroomslist")+",");
			qux=qux.concat(fm.addCSVFile(new File("workingCourseList.csv"), "workingcourselist")+",");
			qux=qux.concat(fm.addCSVFile(new File("proto-roomsList.csv"), "workingroomslist"));
			fm.addData("times", generateTimes());
			break;
		default:
			System.out.println(phase+ " is not a valid phase");
			break;
		}
		System.out.println("Qux is "+qux);
		String[] quux = qux.split(",");
		for (String quuux:quux){
			System.out.println("Loading "+quuux);
			fm.loadFile(quuux);
		}
		Set<String> keys=fm.process(phase);
		fm.write(keys.toArray(new String[0]));
		System.out.println("Done!");
		boolean giveUp=true;
		if (giveUp){
			return;
		}
		
		//load data
		
		//process data		
		

		
		//ArrayList<Professor> professors= generateProfessors(roomsAndProfsSpreadsheet,professorHash, rooms);
		//professors.trimToSize();
		//ArrayList<Course> courses= generateCourses(courseSpreadsheet,roomsAndDeptsHash,roomsAndCoursesHash,courseHash,rooms, professorHash, timeHash,roomsAndProfsHash);
		//courses.trimToSize();
	
		//shuffle to get different solutions
		//Collections.shuffle(courses);
		//linkRoomsToCourses(courses,rooms);//this is where the magic happens
	} //go
	// =================================================================================================================================================================================
	
	// ===================================================================
	/**
	 * makes times for each of the days in 30 min increments
	 */
	public HashMap<String,Time> generateTimes(){
		System.out.println("Generating Times");
		HashMap<String,Time> times=new HashMap<String,Time>();
		Time temp=null;
		for (int i=0;i<5;i++) {
			for (int j=800;j<1900;j+=100){
				temp=new Time(i,j,false);
				times.put(temp.toString(), temp);
				temp=new Time(i,j,true);
				times.put(temp.toString(), temp); 
			}
		}
		System.out.println("Done Generating Times");
		return times;
	}
	// =================================================================================================================================================================================

	// =================================================================================================================================================================================

	/**
	 * 
	 * @param cl courseList
	 * @param ch courseHash
	 * @param rH roomHash
	 * @param pH professorHash
	 * @param tH timeHash
	 * @return
	 * @throws IOException 
	 */
	
			
	
	// =================================================================================================================================================================================
	/**
	 * linkRoomsToCourses was an early solver
	 * @param courses The list of all course objects for this semester
	 * @param rooms The list of all room objects for this semester
	 */
	public void linkRoomsToCourses(ArrayList<Course> courses, ArrayList<Room> rooms){
		//Collections.shuffle(courses);
		/*courses.shuffle
		//OH GOD DONT TOUCH IT
		sort(courses,"getTypeCode",-1);
		sort(courses,"getCapacity",1);
		sort(courses,"getNumberOfPreferredTimes",1);
		//OK THANK YOU
		 */
	}// linkRoomsToCourses
	// =================================================================================================================================================================================

	// =================================================================================================================================================================================
	/**
	 * Use reflection to sort the list of courses based on some field in courses in asc or desc order.
	 * @param list The list of courses to sort
	 * @param getter Name of the getter method for the field name to sort by
	 * @param order Whether to sort in asc or desc order
	 */
	public <T> void sort(ArrayList<Course> list, String getterName, final int order){//help order makes no sense. please document how it works.
		try {
			final Method getter=Course.class.getMethod(getterName, (Class<?>[]) null);
			if (getter == null){
				//Do Nothing
			} else{
				Collections.sort(list, new Comparator<Course>(){
					public int compare(Course c1, Course c2){
						try {
							if((Integer)getter.invoke(c1, (Object[]) null) == (Integer)getter.invoke(c2, (Object[]) null)){
								return 0;
							}
							return ((Integer) getter.invoke(c1, (Object[]) null))<((Integer) getter.invoke(c2, (Object[]) null))?(-1*order):(1*order);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
						return -2;
					}
				});
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	} // sort

	// =====================================================================================================================================================================================
} // class Driver
// =====================================================================================================================================================================================

