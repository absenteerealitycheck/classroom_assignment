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
		switch(phase){
		case 1:
			System.out.println("Starting phase 1");
			fm.addCSVFile(new File("proto-coursehistory.csv"), "historical");
			fm.loadFile("historical");
			
			break;
		case 2:
			System.out.println("Starting phase 2");
			fm.addCSVFile(new File("proto-roomsandprofslist.csv"), "roomsandprofessors");
			fm.addCSVFile(new File("proto-roomsanddeptslist.csv"), "roomsanddepartments");
			fm.addCSVFile(new File("proto-roomsandcourseslist.csv"), "roomsandcourses");
			fm.addCSVFile(new File("workingCourseList.csv"), "workingcourselist");
			fm.addCSVFile(new File("proto-roomslist.csv"), "workingroomslist");
			String qux = "roomsandprofessors,roomsanddepartments,roomsandcourses,workingcourselist,workingroomslist";
			String[] quux = qux.split(",");
			for (String quuux:quux){
				fm.loadFile(quuux);
			}
			System.out.println("About to process...");
			System.in.read();
			break;
		case 3:
			break;
		default:
			System.out.println(phase+ " is not a valid phase");
			break;
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
		fm.addData("times", generateTimes());

		
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
	
			
	public ArrayList<Course> generateCourses(String[][] cl, HashMap<String,ArrayList<Room>> drS,HashMap<String,ArrayList<Room>> crS,
			Hashtable<String,Course> ch, ArrayList<Room> r, Hashtable<String,Professor> pH, Hashtable<String,Time> tH,HashMap<String,ArrayList<Room>> rapH) throws IOException{
	
		System.out.println("Generating Courses");
		ArrayList<Course> courseList=new ArrayList<Course>();
		Course temp;

		for(int row=0;row<cl.length;row++){

			/*
			 * Create all local variables
			 */

			String shortname=cl[row][0];
			if(row+1<cl.length){//ignore repeated sections
				if (cl[row+1][0].equals(shortname)) {
					System.out.println("SKIPPING REPEAT "+shortname);
					continue;
				}
			}
			String deptname=shortname.substring(0,4);
			String longname=cl[row][1];
			int capacity = (cl[row][8].isEmpty())?10:Integer.parseInt(cl[row][8]);
			String type=cl[row][4]; //must parse L/D, LAB, LEC, DIS
	
			temp= new Course(capacity,longname,type);
			temp.addShortName(shortname);
			//Check for crosslisting
			if(!cl[row][2].isEmpty()){
				System.out.println("CROSSLISTED: "+cl[row][2]);
				String[] crossList=cl[row][2].split(",");
				for (String s: crossList) { //add each cross-listed shortname
					if (!temp.getShortName().contains(s)) temp.addShortName(s);
				}
				//a course of this name already exists, do not make a duplicate Object
				//TODO: FIX FOR CROSSLISTS - CHECK MAP FOR LONG(?) KEY; IF EXISTS, CONTINUE
			/*	if (!!!) {
					System.out.println("ALREADY CONTAINS "+temp.toString());
					continue;
				}*/
			}
	
			//Check professors
			String prof=(cl[row][3].isEmpty())?"Scott Kaplan":cl[row][3];
			String[] profs = prof.split("  ");
			for (String s:profs){
				Professor p = pH.get(s);
				temp.addProfessor(p);
				p.addCourse(temp);
			}

			courseList.add(temp);
			ch.put(longname, temp);
			// TODO: handle tech please - must find out where getting info from
			/*boolean[]tech=new boolean[5];
			int slidesNeeded=0;
			for(int i=0;i<tech.length;i++){
				tech[i]=cl[row][i+9].isEmpty();
				if(i==3&&!(cl[row][i+9].isEmpty())){
					 slidesNeeded=Integer.parseInt(cl[row][12]);
				}
			}
			temp.setTech(tech);
			temp.setNumberOfSlides(slidesNeeded);
			 */

			//Making preferred Times Begins
			String[] dayandTime=cl[row][5].split(" ");
			String day = dayandTime[0];
			String time=dayandTime[1];
			String[] dow=day.split("");
			Time t=null;
			ArrayList<Integer> dayOf=new ArrayList<Integer>(dow.length) ;
			//System.out.println(dayOf.size());
			for (int i=0;i<dow.length;i++){
				//System.out.println("print i "+i+ "dayOf"+ dayOf.size());
				if(dow[i].equals("M")){
					dayOf.add(0);
				}
				else if(dow[i].equals("T")){

					if ((i+1)!=dow.length) {
						if (dow[i+1].equals("H")){
							dayOf.add(3);
						}
						else{
							dayOf.add(1);
						}
					}
					else{
						dayOf.add(1);
					}
				}
				else if(dow[i].equals("W")){
					dayOf.add(2);
				}
				else if(dow[i].equals("F")){
					dayOf.add( 4);
				}
				else if(dow[i].equals("H")){
					continue;
				}
			}
			String[] time2=time.split("-",2);	
			String begin=time2[0];
			String ending=time2[1];
			int startHours=(Integer.parseInt(begin.substring(0, 2)));
			int endHour=(Integer.parseInt(ending.substring(0, 2)));
			//System.out.println("thing 2: "+Integer.parseInt(begin.substring(3,begin.length()-2)));
			int start=(Integer.parseInt(begin.substring(0, 2)))*100+Integer.parseInt(begin.substring(3,begin.length()-2));
			int end=(Integer.parseInt(ending.substring(0, 2)))*100;
			
			//if after 12:00pm, adjust for military time
			if(begin.substring((begin.length()-2)).equals("PM")&&!(begin.subSequence(0, 2).equals("12"))){
				start+=1200;
				startHours+=12;
			}
			if(ending.substring((ending.length()-2)).equals("PM")&&!(ending.subSequence(0, 2).equals("12"))){
				end+=1200;
				endHour+=12;
			}
			int numOfBlocks=(endHour-startHours)*2;
			//adjust to complete blocks for endtimes of XX:20 and XX:50
			if(ending.subSequence(3,ending.length()-2).equals("20")){
				end+=30;
				numOfBlocks++; //add a half hour block
			}
			else if(ending.subSequence(3,ending.length()-2).equals("50")){
				end+=100;
				numOfBlocks+=2; //add an hour block
			}
			else if(ending.subSequence(3,ending.length()-2).equals("00")) {
				end+=Integer.parseInt(ending.substring(3,ending.length()-2));
			}
			else if(ending.subSequence(3,ending.length()-2).equals("30")){
				end+=30;
				numOfBlocks++;
			}
			//System.out.println(" number of blocks: "+numOfBlocks);
		/*	System.out.println(" start: "+start);
			System.out.println(" end: "+end);*/
			int basicend=start;
			if(String.valueOf(start).substring(String.valueOf(start).length()-2).equals("00")){
				basicend=start+30;
			}
			else if(String.valueOf(start).substring(String.valueOf(start).length()-2).equals("30")){
				basicend=start+70;
				numOfBlocks--;
			}
			int index=-1;
			//System.out.println(t.toString());
			//System.out.println(" number of blocks2: "+numOfBlocks);
	
			for(int j=0;j<dayOf.size();j++){
				for(Time ti:times){
					if(ti.getDayOfWeek()==dayOf.get(j)&&ti.getStartTime()==start&&ti.getEndTime()==basicend){
						index=times.indexOf(ti);	
					}
				}
				//System.out.println(temp.toString());
				//System.out.println("Index: "+index);
				Time timeT;
				//System.out.println("BLOCKS: "+numOfBlocks);
				for(int i=0;i<numOfBlocks;i++){
					//System.out.println("index: "+(index+i)+ " contains? "+times.get(index+i).toString());
					times.get(index+i).addCourse(temp);//add to Time block in ArrayList of Times
					timeT=times.get(index+i);
					if (temp==null) System.out.println("!!!");
					if (timeT==null) System.out.println("???");
					//System.out.print(timeT.toString() + " | ");
					temp.addTime(timeT);
				}
				//System.out.print("\n");
			}
			//Making preferredRooms 
			ArrayList<Room> dRooms=new ArrayList<Room>();
			dRooms.addAll(drS.get(deptname));
			//System.out.println(shortname+"\n");
			//System.out.println("dRooms size: "+ dRooms.size());
			//System.out.println(crS.containsKey(shortname.substring(0, shortname.length()-3))+" for course "+shortname);
			ArrayList<Room> cRooms=crS.get(shortname.substring(0, shortname.length()-3));
			ArrayList<Room> pRooms=new ArrayList<Room>();
			//System.out.println(temp.getProfessors().size());
			for(Professor p:temp.getProfessors()){
				//System.out.println("prof rooms "+rapH.get(p.getName()));
				pRooms.addAll(rapH.get(p.getName()));
			}
			//System.out.println("sizes "+pRooms.size()+" "+cRooms.size());
			ArrayList<Room> tempD2=new ArrayList<Room>();
			ArrayList<Room> tempD=new ArrayList<Room>();
			tempD.addAll(dRooms);
				for(int j=0;j<dRooms.size();j++){
					//if room in dRooms is not in Courses rooms or Professors rooms, add to a deletion list
					if(!cRooms.contains(dRooms.get(j))&&!pRooms.contains(dRooms.get(j))){
						tempD2.add(dRooms.get(j));
					}
				}
			//System.out.println("tempD2 "+tempD2.size());
			//delete rooms from department rooms that are not suitable for the course
			for(Room r2:tempD2){
				dRooms.remove(r2);
			}
			//if the list of preferred rooms for a course is empty, give it the department room list
			if(dRooms.isEmpty()){
				dRooms.addAll(tempD);
			}
			temp.addPreferredRoomsList(dRooms);
			//End preferredRooms
		}
		courseList.trimToSize();
	
		System.out.println("Done courses");
		return courseList;
	}// generateCourses

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

