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

		FileManager fm=new FileManager();
		fm.addCSVFile(new File("proto-coursehistory.csv"), "historical");
		fm.loadFile("historical");
		Set<String> keys=fm.process("historical");
		fm.write(keys.toArray(new String[0]));
	
		System.out.println("Done!");
		boolean giveUp=true;
		if (giveUp){
			return;
		}


		fm.addCSVFile(new File("proto-roomsandprofslist.csv"), "historical");
		fm.addCSVFile(new File("proto-roomsanddeptslist.csv"), "historical");
		fm.addCSVFile(new File("proto-roomsandcourseslist.csv"), "historical");
		fm.addCSVFile(new File("proto-workingCourseList.csv"), "historical");
		fm.addCSVFile(new File("proto-profslist.csv"), "historical");
		fm.addCSVFile(new File("proto-deptrooms.csv"), "historical");
		fm.addCSVFile(new File("proto-courselist-easy.csv"), "historical");
		
		//load data
		
		//process data		
		fm.addData("times", generateTimes());

		/*ArrayList<Course> courses= generateCourses(courseSpreadsheet,roomsAndDeptsHash,roomsAndCoursesHash,courseHash,rooms, professorHash, timeHash,roomsAndProfsHash);
		//courses.trimToSize();



		boolean print=false;
		for (Course c: courses) {
			int startSize=c.getPreferredRooms().size();
			c.checkCapacity();//rename to ensureCapacity
			c.checkLabs();
			if (c.getPreferredRooms().size()!=startSize){
				print=true;
			}

			if (print){
				System.out.println(c.toString());

				for (Room r : c.getPreferredRooms()){
					System.out.print("\t"+r.toString());
				}
				System.out.println();
			}
			print=false;
		}*/


		//ArrayList<Course> setCourses= bruteForce(courses);
		/*		
		//shuffle to get different solutions
		Collections.shuffle(courses);
		linkRoomsToCourses(courses,rooms);//this is where the magic happens
		/*make a graph of courses - courses that overlap share an edge.
		 *this method will give us clusters of courses based on time
		 *go through each cluster and color separately?
		 *JP 10/29
		 */
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
		System.out.println(cl.length);
		System.in.read();
		ArrayList<Course> courseList=new ArrayList<Course>();
		Course temp;

		for(int row=0;row<cl.length;row++){

			/*
			 * Create all local variables
			 */

			String shortname=cl[row][0];
			String deptname=shortname.substring(0,4);
			String longname=cl[row][1];

			int capacity = (cl[row][8].isEmpty())?10:Integer.parseInt(cl[row][8]);
			String type=cl[row][4]; //must parse L/D, LAB, LEC, DIS

			temp= new Course(capacity,longname,type);
			temp.addShortName(shortname);

			//Check for crosslisting
			if(!cl[row][2].isEmpty()){
				String[] crossList=cl[row][2].split(",");
				for (String s: crossList) {
					if (!s.equals(shortname)){temp.addShortName(s);}
				}
				continue;
			}

			//Check professors
			String prof=(cl[row][3].isEmpty())?"Scott Kaplan":cl[row][3];
			String[] profs = prof.split("  ");
			for (String s:profs){
				Professor p = pH.get(s);
				temp.addProfessor(p);
				if (temp==null) System.out.println("TEMP");
				else if(p==null) System.out.println("PROF");
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
			String[] dayandTime=cl[row][5].split(" ");//TODO:HANDLE DAYS OF WEEK YOU FOOL
			String day = dayandTime[0];
			String time=dayandTime[1];
			String[] dow=day.split("");
			for(String s:dow){
				System.out.println(s+ "in dow");
			}
			Time t=null;
			ArrayList<Integer> dayOf=new ArrayList<Integer>(dow.length) ;
			System.out.println(dayOf.size());
			for (int i=0;i<dow.length;i++){
				System.out.println("print i "+i+ "dayOf"+ dayOf.size());
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
			System.out.println("thing 2: "+Integer.parseInt(begin.substring(3,begin.length()-2)));
			int start=(Integer.parseInt(begin.substring(0, 2)))*100+Integer.parseInt(begin.substring(3,begin.length()-2));
			int end=(Integer.parseInt(ending.substring(0, 2)))*100;
			int numOfBlocks=(endHour-startHours)*2;
			if(ending.subSequence(3,ending.length()-2).equals("20")){
				System.out.println("end is 30");
				end+=30;
				numOfBlocks++;
			}
			else if(ending.subSequence(3,ending.length()-2).equals("50")){
				System.out.println("end is 00");
				end+=100;
				numOfBlocks+=2;
			}
			else if(ending.subSequence(3,ending.length()-2).equals("00")) {
				end+=Integer.parseInt(ending.substring(3,ending.length()-2));

			}
			else if(ending.subSequence(3,ending.length()-2).equals("30")){
				System.out.println("end is 30");
				end+=30;
				numOfBlocks++;
			}

			if(begin.substring((begin.length()-2)).equals("PM")&&!(begin.subSequence(0, 2).equals("12"))){
				start+=1200;
			}
			if(ending.substring((ending.length()-2)).equals("PM")&&!(ending.subSequence(0, 2).equals("12"))){

				end+=1200;
			}
			System.out.println(" number of blocks: "+numOfBlocks);
			System.out.println(" start: "+start);
			System.out.println(" end: "+end);
			int basicend=start;
			System.out.println("thing: "+(String.valueOf(start).substring(String.valueOf(start).length()-2).equals("00")));
			if(String.valueOf(start).substring(String.valueOf(start).length()-2).equals("00")){
				System.out.println("in if>");

				basicend=start+30;
			}
			else if(String.valueOf(start).substring(String.valueOf(start).length()-2).equals("30")){
				basicend=start+70;
				numOfBlocks--;
			}
			int index=-1;
			//System.out.println(t.toString());
			System.out.println(" number of blocks2: "+numOfBlocks);
			for(int j=0;j<dayOf.size();j++){
				for(Time ti:times){
					if(ti.getDayOfWeek()==dayOf.get(j)&&ti.getStartTime()==start&&ti.getEndTime()==basicend){
						index=times.indexOf(ti);	
					}
				}
				for(int i=0;i<numOfBlocks;i++){
					System.out.println("index: "+(index+i)+ "contains? "+times.get(index).toString());
					times.get(index+i).addCourse(temp);//add to Time block in ArrayList of Times
				}
			}
			//temp.setTime(t);//add Time to Course
			//preferredTimes	
			//Making preferredRooms 
			ArrayList<Room> dRooms=new ArrayList<Room>();
			dRooms.addAll(drS.get(deptname));
			System.out.println(shortname+"\n");
			System.out.println("dRooms size: "+ dRooms.size());
			//System.out.println(crS.containsKey(shortname.substring(0, shortname.length()-3))+" for course "+shortname);
			ArrayList<Room> cRooms=crS.get(shortname.substring(0, shortname.length()-3));
			ArrayList<Room> pRooms=new ArrayList<Room>();
			System.out.println(temp.getProfessors().size());
			for(Professor p:temp.getProfessors()){
				//System.out.println("prof rooms "+rapH.get(p.getName()));
				pRooms.addAll(rapH.get(p.getName()));
			}
			System.out.println("sizes "+pRooms.size()+" "+cRooms.size());
			ArrayList<Room> tempD2=new ArrayList<Room>();
			ArrayList<Room> tempD=new ArrayList<Room>();
			tempD.addAll(dRooms);
			for(int j=0;j<dRooms.size();j++){
				//System.out.println("conatins c "+cRooms.contains(dRooms.get(j)));
				//System.out.println("conatins p "+pRooms.contains(dRooms.get(j)));
				if(!cRooms.contains(dRooms.get(j))&&!pRooms.contains(dRooms.get(j))){
					tempD2.add(dRooms.get(j));
				}
			}

			System.out.println("tempD2 "+tempD2.size());
			for(Room r2:tempD2){
				dRooms.remove(r2);
			}
			if(dRooms.isEmpty()){
				System.out.println("EMPTY");
				dRooms.addAll(tempD);
			}
			temp.addPreferredRoomsList(dRooms);
			//dRooms=drS.get(deptname);
			//System.out.println("dRooms size again "+dRooms.size()+"\n");
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

