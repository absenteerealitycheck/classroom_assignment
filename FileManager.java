import java.util.*;
import java.util.Map.Entry;
import java.io.*;

public class FileManager {

	private HashMap<String,File> files;
	private HashMap<String,String[][]> spreadsheets;
	private HashMap<String,Map<String,?>> data;

	public FileManager(){
		files=new HashMap<String,File>();
		spreadsheets=new HashMap<String,String[][]>();
		data=new HashMap<String,Map<String,?>>();
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Adds a File f to the FileManager and associate it with the specified name.
	 * @param f - the File to add
	 * @param name - the name to associate with the File
	 */
	public String addFile(File f, String name){
		files.put(name,f);
		return name;
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Adds a .csv File f to the FileManager and associate it with the specified name.
	 * @param f - the File to add
	 * @param name - the name to associate with the File
	 * @throws IllegalArgumentException - if the specified File is not a .csv file
	 */
	public String addCSVFile(File f, String name){
		System.out.println("Adding "+f.getName());
		if (!f.getName().endsWith(".csv")){
			throw new IllegalArgumentException(f.getName()+" is the incorrect file type");
		}
		return addFile(f,name);
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * TODO:Implement this?
	 */
	public void addAllCSVFiles(File f, String name){
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Returns the set of all the File names in FileManager.
	 * @return the set of all the File names in FileManager
	 */
	public Set<String> getFileNames(){
		return this.files.keySet();
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Returns the File associated with the specified name, or null if no such File exists.
	 * @param name - the name whose associated File is to be returned
	 * @return - the File associated with the specified name
	 */
	public File getFile(String name){
		return files.get(name);
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Returns the set of all the spreadsheet names in FileManager.
	 * @return the set of all the spreadsheet names in FileManager
	 */
	public Set<String> getSpreadsheetNames(){
		return this.spreadsheets.keySet();
	}
	// ================================================================================================	

	// ================================================================================================
	/**
	 * Returns the spreadsheet representation associated with the specified name, or null if no such 
	 * spreadsheet exists.
	 * @param name - the name whose associated spreadsheet is to be returned
	 * @return - the two-dimensional array of Strings representation of the spreadsheet
	 */
	public String[][] getSpreadsheet(String name){
		return spreadsheets.get(name);
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Returns the set of all the data names in FileManager.
	 * @return the set of all the data names in FileManager
	 */
	public Set<String> getDataNames(){
		return this.data.keySet();
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Returns the data map associated with the specified name, or null if no such data exists.
	 * @param name - the name whose associated data map is to be returned
	 * @return - the data map, which uses String keys to access its values
	 */
	public Map<String,?> getData(String name){
		return data.get(name);
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Loads the File associated with the specified name into a two-dimensional array of Strings and 
	 * returns the two-dimensional array. 
	 * @param name - the name associated with the File to load
	 * @return true if the File is loaded successfully, false otherwise 
	 */
	public boolean loadFile(String name) {
		try{
			String[][] newSpreadsheet = makeSpreadsheet(getFile(name));
			this.spreadsheets.put(name, newSpreadsheet);
			return true;
		} catch(IOException e){
			return false;
		}
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Converts the contents of a .csv file to a String [][]
	 * @param file - the file to convert
	 * @return the String[][] representation of file
	 * @throws IOException
	 */
	private static String[][] makeSpreadsheet(File file) throws IOException{
		System.out.println("Reading "+file.getName());
		String[][] csv = null;
		try {
			//Determine the dimensions of the two-dimensional array
			BufferedReader b = new BufferedReader(new FileReader(file));
			String next=b.readLine(); 
			int cols=next.split(";").length+1;
			int rows=1;
			while ((next=b.readLine())!=null){
				rows++;
			}
			b.close();
			csv = new String[rows][cols];

			//Load the file into the two-dimensional array
			BufferedReader readIn = new BufferedReader(new FileReader(file));
			String line="";
			String[] row=new String[csv[0].length];
			int count=0;
			while((line=readIn.readLine())!=null){
				String [] temp= line.split(";",row.length);
				int n = Math.min(temp.length, row.length);
				for(int i=0;i<n;i++){
					if(count<csv.length){
						csv[count][i]=temp[i];
					}
					else break;
				}
				count++;
			}
			readIn.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("You Done Goofed");
			e.printStackTrace();
		}
		return csv;
	} // makeSpreadsheet
	// ================================================================================================

	// ================================================================================================
	/**
	 * Adds the specified custom data map to the File Manager. 
	 * @param name - the name to associate with the specified data map
	 * @param d - the custom data map
	 */
	public void addData(String name, Map<String,?> d){
		data.put(name, d);
	}
	// ================================================================================================



	// ================================================================================================
	/**
	 * Transforms the spreadsheet associated with the specified name into some number of data maps, and
	 * returns the set of keys needed to access the newly created data maps.
	 * @param name - the name associated with the spreadsheet to process
	 * @return the set of keys needed to access the newly created data maps.
	 * @respect calls to data.put() are made directly inside process, not from the methods it calls
	 */
	public <N> Set<String> process(int phase){
		//TODO:Use reflection on name
		String name="";
		switch(phase){
		case 1:
			name="historical";
			System.out.println("Processing Hisorical data");
			data.put("roomsandcourses", associateFieldAndRooms(getSpreadsheet(name),1));
			data.put("roomsanddepartments", associateFieldAndRooms(getSpreadsheet(name),2));
			data.put("roomsandprofessors", associateFieldAndRooms(getSpreadsheet(name),3));
			//generateUnionSpecs(getSpreadsheet("workingcourselist"));
			data.put("unionspecs", generateUnionSpecs(getSpreadsheet("workingcourselist"), data.keySet()));
			return data.keySet();
		case 2:
			String spreadsheetNames="roomsandprofessors,roomsandcourses,roomsanddepartments,workingcourselist,workingroomlist";
			data.put("recommendedroomslist",generateRecommendedRooms(spreadsheetNames));
			return data.keySet();
		case 3:
			//data has the "times" key already, see Driver
			System.out.println("[FM-P1]"+"Spreadsheets are "+this.spreadsheets.size()+" many");
			System.out.println("[FM-P2]"+"Spreadsheets are "+this.getSpreadsheetNames());
			data.put("workingroomlist", generateRooms(getSpreadsheet("workingroomslist")));
			data.put("workingcourselist",generateCourses(getSpreadsheet("workingcourselist")));
			data.put("recommendedroomslist", readRecs(getSpreadsheet("recommendedroomslist")));

			drawEdges("workingcourselist", "workingroomlist", "recommendedroomslist", "times");

			TreeMap<String,Course> courseMap = (TreeMap<String, Course>) data.get("workingcourselist"); 
			System.out.println("[P1]"+"number of course nodes is "+courseMap.size());

			try {
				BufferedWriter bw=new BufferedWriter(new FileWriter(new File("test-drawnedges.csv")));
				for (Course n:courseMap.values()){
					String foo = n.getPreferredRooms().toString();
					String bar= n.getShortName().toString();
					//bw.write(bar.substring(1,bar.length()-1)+";"+foo.substring(1,foo.length()-1));
					//bw.newLine();
					//bw.write(n.getShortName()+";"+n.getPreferredRooms());
				}
				bw.close();
				//System.in.read();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			TreeMap<String,Room> roomMap = (TreeMap<String, Room>) data.get("workingroomlist");
			System.out.println("[P2]"+"number of room nodes is "+roomMap.size());
			//TODO:Make a Node class
			//TODO:Make a getNodeMap method
			//TODO:Make a getMapMap method
			TreeMap<String,Node> nodeMap = new TreeMap<String,Node>(roomMap);
			nodeMap.putAll(courseMap);
			System.out.println("[P3]"+"number of graph nodes is "+nodeMap.size());
			HashMap<Course,Room> ansLF=LF((TreeMap<String,Node>)nodeMap.clone());
			//HashMap<Course,Room> ansRND=RND((TreeMap<String,Node>)nodeMap.clone());
			HashMap<Course,Room> ans=ansLF;
			//HashMap<Course,Room> ansAMIS=AMIS((TreeMap<String,Node>)nodeMap.clone());
			System.out.println("Hello "+ans.size());
			for (Entry e :ans.entrySet()){
				System.out.println(e);
			}

			System.out.println(ans);

			return data.keySet();
		default:
			break;
		}
		return null;
	}
	// ================================================================================================

	// ================================================================================================
	/**
	 * Finds every room that a unique value in the historical spreadsheet has used. The field to search 
	 * search for unique values is indicated by control.
	 * @param cHS - the courseHistorySpreadsheet
	 * @param control - indicates which field to associate with rooms
	 * @return a map of every room this <?> has used
	 */
	private TreeMap<String,ArrayList<String>> associateFieldAndRooms(String[][] cHS, int control){
		TreeMap<String,ArrayList<String>> h = new TreeMap<String,ArrayList<String>>();
		ArrayList<String> forbiddenRooms = new ArrayList<String>();
		forbiddenRooms.add("NEWP 100");
		forbiddenRooms.add("CPRD 101A");
		forbiddenRooms.add("FROS BARKER");
		forbiddenRooms.add("GROS 11");
		forbiddenRooms.add("GROS 12");
		forbiddenRooms.add("PRDT LR");
		forbiddenRooms.add("MERR");
		for (int i=1; i<cHS.length; i++){
			String field="";
			String room=cHS[i][7];
			if (forbiddenRooms.contains(room)){
				continue;
			}
			switch(control){
			case 1://course
				field=cHS[i][2].replaceAll("&", " and ").replaceAll("[^a-zA-Z]", "").toLowerCase();
				break;
			case 2://department
				field=cHS[i][1].substring(0, 4);
				break;
			case 3://professor
				field=cHS[i][4];
				break;
			}
			String[] splitField = field.split("  ");
			for (String f:splitField){
				if (!h.containsKey(f)){
					h.put(f, new ArrayList<String>());
					if(control==1){
						String cshort = cHS[i][1];
						String[] cShortList = cshort.split("-");
						h.get(f).add(cShortList[0]+"-"+cShortList[1]+";");
					}
				} else {
					if(control==1){ //group by long name
						boolean toAdd=true;
						String cshort = cHS[i][1];
						String[] cShortList = cshort.split("-");
						String newCourse = cShortList[0]+"-"+cShortList[1];
						String[] courseList=h.get(f).get(0).split(";|,");
						String soFar=""; //keep track of short names
						for (String cL:courseList){
							soFar=soFar+cL+",";
							if (cL.equals(newCourse)){
								toAdd=false;
							}
						}
						if (toAdd){
							h.get(f).set(0,soFar+newCourse+";");
						}
					}
				}
				if (!h.get(f).contains(room)){
					h.get(f).add(room);
				}
			}
		}

		if (control==1){ //convert long name into short name
			TreeMap<String,ArrayList<String>> g = new TreeMap<String,ArrayList<String>>();
			Set<String> oldKeys = h.keySet();
			for (String oldKey:oldKeys){
				ArrayList<String> oldValues = h.get(oldKey);
				String newKeys = oldValues.get(0);
				ArrayList<String> newValuesList = new ArrayList<String>();
				for (int i=1; i<oldValues.size(); i++){
					String newValue = oldValues.get(i);
					newValuesList.add(newValue);
				}
				newKeys=newKeys.substring(0,newKeys.length()-1);
				String[] newKeysList = newKeys.split(",");
				for (String newKey:newKeysList){
					String[] newKeyParts = newKey.split("-");
					if (newKeyParts[1].length()!=2){ //dont include old 2 digit course codes
						g.put(newKey,newValuesList);
					}
				}
			}
			return g;
		} else {
			return h;
		}
	} // associateFieldAndRooms
	// ================================================================================================


	// ================================================================================================
	/**
	 * 
	 * @param wCL - workingCourseList
	 * @return
	 */
	private TreeMap<String,ArrayList<String>> generateUnionSpecs(String[][] wCL, Set<String> dataKeys){
		//System.out.println(dataKeys);
		Map<String,?> rc=data.get("roomsandcourses");
		Map<String,?> rd=data.get("roomsanddepartments");
		Map<String,?> rp=data.get("roomsandprofessors");
		TreeMap<String,ArrayList<String>> unionSpecs = new TreeMap<String,ArrayList<String>>();
		System.out.println("[GUS]"+"keys are "+rc.keySet());
		for (String[] course:wCL){
			String specs="";
			String courseName=course[0];
			System.out.println("[GUS]start course "+courseName.substring(0,7));
			String cSpec=checkLength(courseName.substring(0,8),rc,1);
			System.out.println("[GUS]end course");
			specs=specs.concat(cSpec+";");
			String profName=course[3];
			String[] profs = profName.split("  ");
			String pSpec="";
			for (String p:profs){
				pSpec=checkLength(p,rp,1);
				if (pSpec.equals("1")){
					break;
				}
			}
			specs=specs.concat(pSpec+";");
			String dSpec="";
			specs=specs.concat(dSpec+";");
			if (cSpec.equals("1")){
				System.out.println("[GUS]"+courseName+";"+profName+";"+cSpec);
			}
			ArrayList<String> allSpecs = new ArrayList<String>();
			allSpecs.add(profName+";"+specs);
			unionSpecs.put(courseName, allSpecs);
			//System.out.println(rc.get(courseName));
		}

		return unionSpecs;
	}

	private String checkLength(String key, Map<String,?> map, int length){
		String spec="";
		if (map.containsKey(key)){
			System.out.println("[CL]"+((ArrayList<String>) map.get(key)).size()+"\t\t"+map.get(key));
			if (((ArrayList<String>) map.get(key)).size()==length){
				spec="1";
			}
		}
		return spec;
	}

	// ================================================================================================

	// ================================================================================================

	/**
	 * Returns a map of recommended rooms to courses where the Course.toString() is the key
	 * @param spreadsheetNames - a comma separated list of "spreadsheets" to run this method on
	 * @return a map of Courses to their recommended rooms with Course.toString() as the key
	 */
	@SuppressWarnings("unchecked")
	private TreeMap<String,?> generateRecommendedRooms(String spreadsheetNames){
		String[] names = spreadsheetNames.split(",");
		String[][] roomsAndProfessors=getSpreadsheet(names[0]);
		String[][] roomsAndCourses=getSpreadsheet(names[1]);
		String[][] roomsAndDepartments=getSpreadsheet(names[2]);
		String[][] workingCourseList=getSpreadsheet(names[3]);
		String[][] workingRoomList=getSpreadsheet(names[4]);

		String[][] recommendedRoomList=new String[workingCourseList.length][8];

		TreeMap<String,ArrayList<String>> t=new TreeMap<String,ArrayList<String>>(); 
		ArrayList<String> info=new ArrayList<String>();
		System.out.println(spreadsheetStringify(roomsAndCourses));
		for(int i=0; i<workingCourseList.length; i++){
			info.add(workingCourseList[i][1]+";");
			info.add(workingCourseList[i][2]+";");
			info.add(workingCourseList[i][3]+";");

			TreeSet<String> pList=findRooms(workingCourseList[i][3],roomsAndProfessors);
			String[] courseKeyParts=workingCourseList[i][0].split("-");
			String courseKey=courseKeyParts[0]+"-"+courseKeyParts[1];
			TreeSet<String> cList=findRooms(courseKey,roomsAndCourses);
			System.out.println("[GRR]"+workingCourseList[i][0]);
			//TODO: Handle dLists for Cross listed Courses
			TreeSet<String> dList=findRooms(workingCourseList[i][0].substring(0,4),roomsAndDepartments);
			TreeSet<String> cup =new TreeSet<String>();
			cup.addAll(cList);
			cup.addAll(pList);
			TreeSet<String> notdncup = new TreeSet<String>();
			TreeSet<String> rList = new TreeSet<String>();
			notdncup.addAll(dList);
			rList.addAll(dList);
			if (!cup.isEmpty()){
				notdncup.removeAll(cup);
				rList.removeAll(notdncup);
			}
			System.out.println("[GRR]"+cList);
			info.add(rList.toString().substring(1,rList.toString().length()-1)+";");			
			info.add(pList.toString().substring(1,pList.toString().length()-1)+";");
			info.add(cList.toString().substring(1,cList.toString().length()-1)+";");
			info.add(dList.toString().substring(1,dList.toString().length()-1)+";");

			t.put(workingCourseList[i][0], ((ArrayList<String>)info.clone()));
			info.clear();
		}
		return t;
	}

	/**
	 * 
	 * @param key - String representation of the Object to associate courses with
	 * @param source - a String[][] of Objects associated with their historical room data
	 * @return a Set of rooms associated with key in historical data
	 */
	private TreeSet<String> findRooms(String key, String[][] source){
		TreeSet<String> rooms = new TreeSet<String>();
		//if (key.contains("-")){
		//	System.out.println(spreadsheetStringify(source));
		//}
		String[] keys = key.split("  ");
		for (String k:keys){

			for (String[] row:source){
				if (row[0].equals(k)){
					String[] qux = row[1].split(", ");
					for (String quux:qux){
						rooms.add(quux);
					}
				}
			}
		}
		//remove last , from rooms 
		return rooms;
	}

	// ================================================================================================
	/**
	 * Creates the map of Room objects, using the toString() method of each Room as its key.
	 * @param rS - the spreadsheet of rooms to use to generate Rooms
	 * @return the data map which uses Room.toString() keys to access Room objects
	 */
	private Map<String,Room> generateRooms(String[][] rS){
		System.out.println("Generating Rooms");
		TreeMap<String,Room> rooms= new TreeMap<String,Room>();
		for(int row=1; row<rS.length; row++){
			Room r = new Room(rS[row]);
			rooms.put(r.toString(),r);
		}		
		System.out.println("Done Generating Rooms");
		return rooms;
	}
	// ================================================================================================


	// ================================================================================================
	/**
	 * Creates the map of Course objects, using the toString() method of each Course as its key.
	 * @param cS - the spreadsheet of rooms to use to generate Course objects
	 * @return the data map which uses Room.toString() keys to access Room objects
	 */
	private Map<String,Course> generateCourses(String[][] cS) {
		System.out.println("Generating Courses");
		TreeMap<String,Course> courses= new TreeMap<String,Course>();
		for(int row=1; row<cS.length; row++){
			Course c = new Course(cS[row]);
			courses.put(c.toString(),c);
		}		
		System.out.println("Done Generating Courses");
		return courses;	
	}

	// ================================================================================================

	// ================================================================================================
	/**
	 * 
	 * @param rS
	 * @return
	 */
	private Map<String,HashSet<String>> readRecs(String[][] rS){
		TreeMap<String,HashSet<String>> recs = new TreeMap<String,HashSet<String>>();
		for (String[] row:rS){
			recs.put(row[0], new HashSet<String>(Arrays.asList(row[4])));
		}
		return recs;
	}

	private HashMap<Course,Room> RND(Map<String,Node> nodeMap){
		HashSet<Node> nodeSet=new HashSet<Node>(nodeMap.values());
		SequentialColoring(nodeSet);
		return null;
	}

	private HashMap<Course,Room> LF(Map<String,Node> nodeMap){
		TreeSet<Node> nodeSet=new TreeSet<Node>(new Comparator<Node>(){
			public int compare(Node a, Node b){
				if (a.getNeighborCount()>b.getNeighborCount()){
					return -1;
				}
				return 1;
			}
		});
		Collection<Node> col=nodeMap.values();
		for (Node blivit: col){
			nodeSet.add(blivit);
		}
		SequentialColoring(nodeSet);

		return null;
	}


	// ================================================================================================
	private HashMap<Course,Room> SequentialColoring(Set<Node> nodeSet){                                
		int color=0;
		for(Node n:nodeSet){
			//System.out.println("[SC1]"+"Looking at Node ("+n.getNeighborCount()+") "+n);
			color=0;
			Set<Node> neighbors = n.getNeighbors();

			boolean assignedColor=false;
			while(!assignedColor){
				boolean reset=false;
				if (n instanceof Course){
					int roomCount=0;
					//System.out.print("[SC2]"+n+"room neighbor count is ");
					for (Node thud:neighbors){
						if (thud instanceof Room){
							roomCount++;
						}
					}
					//System.out.println(roomCount);
				}
				for (Node m:neighbors){
					if (m.getColor()==color){
						color++;
						reset=true;
						break;
					}
				}                                
				if (!reset){
					//System.out.println("[SC3]"+"Assigning color "+color);
					n.setColor(color);
					assignedColor=true;
				}
			}
		}

		HashMap<Integer,ArrayList<Node>> sol = new HashMap<Integer,ArrayList<Node>>();
		ArrayList<Course> overflow = new ArrayList<Course>();
		ArrayList<Course> skipped = new ArrayList<Course>();
		for (Node n:nodeSet){
			if (n instanceof Room){
				sol.put(n.getColor(), new ArrayList<Node>());
				sol.get(n.getColor()).add(n);
			}
		}
		TreeMap<String,Room> roomMap = (TreeMap<String, Room>) data.get("workingroomlist");
		for (Node n:nodeSet){
			if (n instanceof Course){
				if (sol.containsKey(n.getColor())){
					sol.get(n.getColor()).add(n);
				} else {
					int countBadsRooms=0;
					for (String pr:((Course)n).getPreferredRooms()){
						if (roomMap.get(pr)==null){
							countBadsRooms++;
						}
					}
					if (((Course)n).getPreferredRooms().size()==countBadsRooms){
						skipped.add((Course)n);
						//System.out.println("[SCCheck]"+"phantom room case");
					} else {
						overflow.add((Course)n);
					}
				}
			}
		}
		boolean overlapError=true;
		if (overlapError){

			//System.out.println("[SCEnd]"+"overflow is "+overflow);
			System.out.println("[SCEnd]"+"overflow is "+overflow.size()+" long");

			int countBad=0;
			for (Node bad:overflow){

				int countBadsRooms=0;
				for (String pr:((Course)bad).getPreferredRooms()){
					if (roomMap.get(pr)==null){
						countBadsRooms++;
						//System.out.println("\t[SCEnd]"+pr+ " is not a room");
					}
				}
				if (((Course)bad).getPreferredRooms().size()!=countBadsRooms){
					System.out.println("[SCEnd]"+"("+bad.getColor()+") "+bad
							+":("+((Course)bad).getDow()+")"
							+":"+((Course)bad).getStartTime()+"-"+((Course)bad).getEndTime()
							+":"+((Course)bad).getCapacity()
							+":"+((Course)bad).getType());
					System.out.print("[SCEnd]"+"\t\t[");
					for (String waldo:((Course)bad).getPreferredRooms()){
						if (roomMap.get(waldo)==null){
							System.out.print("X:"+waldo+"\n\t\t");
						} else if (false){

							System.out.print(waldo);
							Room waldoRoom =roomMap.get(waldo); 
							int rColor=waldoRoom.getColor();
							System.out.print("("+rColor+"):"+waldoRoom.getCapacity()+":"+waldoRoom.getType());
							TreeSet<Time> times = new TreeSet<Time>(((Course)bad).getTimes());

							ArrayList<Node> ralph = sol.get(rColor);
							for (Node ral:ralph){
								if (ral instanceof Course){
									System.out.print("\n\t\t\t\t["
											+((Course)ral).getShortName().toArray()[0]+"]"
											+":("+((Course)ral).getDow()+")"
											+":"+((Course)ral).getStartTime()+"-"+((Course)ral).getEndTime());
								}
							}


							System.out.println("\n\t\t\t\t====================================");
							for (Time t:times){
								ArrayList<Course> concCourses=t.getCourses();
								for (Course cc:concCourses){
									if (cc.getColor()==rColor){
										System.out.print("\n\t\t\t\t["+cc.getShortName().toArray()[0]+"/"+t+"]");
									}
								}
							}


							System.out.print("\n\t\t");
						}

					}
					System.out.println("]");


				} else {
					countBad++;
					System.out.println("[SCEnd]"+"(skipping) "+bad+":"+((Course)bad).getPreferredRooms());
				}

				/*for (Room thud:((Course)bad).getPreferredRooms()){
                        int foo = thud.getColor();
                        System.out.println("[SCEnd]"+"\t"+thud+" has color "+foo);
                        System.out.print("[SCEnd]"+"\t\t"+foo+" colored things are ");
                        for (Node bar:nodeSet){
                                if (bar instanceof Course && bar.getColor()==foo){
                                        System.out.print(((Course)bar).getShortName()+", ");
                                }
                        }
                        System.out.println();
                }*/
			}
		}
		System.out.println("[SCEnd]"+"overflow is "+overflow.size()+" long");
		System.out.println("[SCEnd]"+"skipped is "+skipped.size()+" long");
		return null;
	}




	// ================================================================================================
	private HashMap<Course,Room> AMIS(Map<String,Node> nodeMap){
		HashMap<Course,Room> assignments = new HashMap<Course,Room>();//C
		int color=0;//col

		HashMap<Node,Integer> colorMap = new HashMap<Node,Integer>();//C
		HashSet<Node> coloredSet=new HashSet<Node>();

		TreeSet<Node> nodeSet=new TreeSet<Node>(new Comparator<Node>(){
			public int compare(Node a, Node b){
				if (a.getNeighborCount()>b.getNeighborCount()){
					return 1;
				}
				return -1;
			}
		});

		Collection<Node> col=nodeMap.values();
		System.out.println("=================================================");
		int count=0;
		for (Node blivit: col){
			System.out.println("[AMIS]"+(count++)+"."+blivit.getNeighborCount()+","+blivit);
			nodeSet.add(blivit);
		}
		System.out.println("=================================================");
		count=0;
		System.out.println("=================================================");
		for (Node blivit: nodeSet){
			System.out.println("[AMIS]"+(count++)+"."+blivit.getNeighborCount()+","+blivit);
		}
		System.out.println("=================================================");

		boolean roomColored=false;
		while(!nodeSet.isEmpty()){
			Node room=null;
			for(Node n:nodeSet){
				if (n instanceof Room){
					room=n;
					roomColored=true;
					colorMap.put(n,color);
					coloredSet.add(n);
					break;
				}
			}
			if (!roomColored){
				//System.out.println("[FM1]"+nodeSet.size());
				//System.out.println("[FM2]"+nodeSet);
				break;
			}
			for (Node n:nodeSet){
				//System.out.println("[LF1]"+"n is "+n);
				if (n instanceof Room){
					//System.out.println("[LF3]"+"Skipping the room");
					continue;
				}

				for (Node m:coloredSet){
					//System.out.println("[LF2]"+"m is "+m);
					//System.out.println("[LF4]"+"neighbors are "+n.getNeighbors());
					if (!n.isNeighbor(m)){
						assignments.put((Course)n, (Room)room);
						//System.out.println("[LF4]"+"ans size is "+assignments.size());
						colorMap.put(n,color);
						coloredSet.add(n);
					}
				}		
				roomColored=false;
				room=null;
			}
			nodeSet.removeAll(coloredSet);
			color++;
		}

		return assignments;
	}

	// ================================================================================================
	/* 		
  		TreeMap<Node,Integer> neighborMap= new TreeMap<Node,Integer>();
		for (Node n: nodeMap.values()){
			neighborMap.put(n, n.getNeighborCount());			
		}
	 */
	// ================================================================================================
	/**
	 * Populates the Sets inside each of the data objects whose names are specified.
	 * @param workingcourselist - map of Course.toString to Course object
	 * @param workingroomlist - map of Room.toString() to Room object
	 * @param recommendedroomslist - the results of phase 2
	 * @param times - a map of all Times with a key of Time.toString()
	 */
	@SuppressWarnings("unchecked")
	private void drawEdges(String workingcourselist, String workingroomlist, String recommendedroomslist, String times){
		TreeMap<String,Course> courseMap = (TreeMap<String, Course>) data.get(workingcourselist); 
		TreeMap<String,Room> roomMap = (TreeMap<String, Room>) data.get(workingroomlist);
		TreeMap<String,HashSet<String>> recMap = (TreeMap<String, HashSet<String>>) data.get(recommendedroomslist);
		TreeMap<String,Time> timeMap = (TreeMap<String, Time>) data.get(times);

		for (Room r:roomMap.values()){
			TreeMap<String,Room> tmr=((TreeMap<String,Room>)roomMap.clone());
			tmr.remove(r.toString());
			Set<Room> rooms = new HashSet<Room>(tmr.values());
			for (Node n:rooms){
				r.addNeighbor(n);
			}

		}

		for (Course c:courseMap.values()){
			Set<String> keys = new HashSet<String>(roomMap.keySet());
			//System.out.println("[DE-C1]"+c.getShortName());
			//System.out.println("[DE-C1]"+c.getShortName().contains("ARHA-226-01"));
			String[] recKeys=((String)(recMap.get(c.getShortName().toArray()[0]).toArray()[0])).split(", ");

			for (String rk:recKeys){
				Room r=roomMap.get(rk);
				if (r!=null){
					if (!c.getType().equals("lab")){
						c.addPreferredRoom(rk);
					}
					else if (c.getType().equals("lab") && r.getType().equals("lab")){
						c.addPreferredRoom(rk);
					}
				} else {
					c.addPreferredRoom(rk);
				}
			}

			for (String k:recKeys){
				//System.out.println("[DE-] Trying to remove "+k+" from recKeys of size "+recKeys.length);
				Room r=roomMap.get(k);
				if (r!=null){
					boolean removeEdge=true;
					if (c.getType().equals("lab") && !r.getType().equals("lab")){
						removeEdge=false;
					}

					if (r.getCapacity()!=-1 && c.getCapacity()>r.getCapacity()){
						removeEdge=false;
						//System.out.println("[DE2]"+"Course can't go in this room");						
					}
					if (c.getShortName().contains("GEOL-111L-02")){
						//System.out.println("[DE1]"+c);
						//System.out.println("[DE2]"+r);
						//System.out.println("[DE3]"+"removing r is "+removeEdge);
					}

					//System.out.println("[DE3]"+"keys is size "+keys.size());
					if (removeEdge){
						keys.remove(k); //no edge ie possible coloring
					}
					//System.out.println("[DE4]"+"keys is now size "+keys.size());
				}
			}
			//System.out.println("[DE3]"+keys.size()+" keys is now"+keys);
			for (String k:keys){
				Room r=roomMap.get(k);
				r.addNeighbor(c.addNeighbor(r));
			}



			double startTime=c.getStartTime();
			double endTime=c.getEndTime();
			double blocks=0;
			ArrayList<Integer> dow = c.getDow();
			if(startTime%100==30){
				startTime+=20;
			}
			if(endTime%100==30){
				endTime+=20;
			} else if(endTime%100==20){
				endTime+=30;
			} else if(endTime%100==50){
				endTime+=50;
			}
			blocks=(endTime-startTime)/50;
			startTime=c.getStartTime();
			for (int i=0; i<blocks; i++){				
				for (Integer j:dow){
					String timeName=j+":"+(int)startTime;
					//System.out.println("[FM-DE1]"+"name of time is "+timeName);
					Time t=timeMap.get(timeName);
					//System.out.println("[FM-DE2]"+"Newly gotten time is "+t);
					t.addCourse(c.addTime(t));
					//System.out.println("[FM-DE3]"+"List of times is "+c.getTimes());
					for(Course course:t.getCourses()){
						if(course!=c){
							c.addNeighbor(course.addNeighbor(c));
						}
					}
				}
				startTime+=30;
				if (startTime%100==60){
					startTime+=40;
				}
			}
			//System.out.println("[FM-DE4]"+c+": "+c.getTimes());
			//System.out.println("[FM-DE5]"+c+": "+c.getDow()+"|"+c.getStartTime()+"|"+c.getEndTime());




		}
	}

	// ================================================================================================
	// ================================================================================================
	public void colorNode(Course c){//Removes all of the edges to the course and assigns a color(room)

	}
	// ================================================================================================
	// ================================================================================================
	public Object makeU1(){
		return null;
	}
	// ================================================================================================
	// ================================================================================================
	public Object makeU2(){
		return null;
	}
	// ================================================================================================
	// ================================================================================================
	@SuppressWarnings("unchecked")
	public void write(String[] names){
		for (String s:names){
			File f = new File("gen-"+s+"list.csv");
			writeHashToCSV((TreeMap<String,ArrayList<String>>)data.get(s), f);
		}
	}

	// ================================================================================================
	// ================================================================================================
	/**
	 * Writes Map d to File f as a .csv
	 * @param d - the data to write 
	 * @param f - the file to be written to
	 * @return a boolean which is true if the file wrote correctly and false otherwise
	 */
	private boolean writeHashToCSV(Map<String,ArrayList<String>> d, File f){
		try{
			BufferedWriter bw=new BufferedWriter(new FileWriter(f));
			for (Entry<String, ArrayList<String>> es :d.entrySet()){
				String ts2=es.getValue().toString();
				ts2=ts2.substring(1,ts2.length()-1);
				//System.out.println("[1]"+ts2);
				//System.out.println(es.getKey()+";"+ts2+"\n");
				ts2=ts2.replace(";, ", ";");
				System.out.println("[1]"+es.getKey()+";"+ts2);
				bw.write(es.getKey()+";"+ts2+"\n");
			}
			bw.close();
			System.out.println("Done writing to file "+f.getName());
			return true;
		} catch (IOException e){
			return false;
		}
	}
	// ================================================================================================
	// ================================================================================================
	/**
	 * 
	 * @param s
	 * @return
	 */
	public String spreadsheetStringify(String[][] s){
		String result="";
		for(int row=1;row<s.length;row++){
			for(int col=0;col<s[0].length;col++){
				if(s[row][col]!=null){result+=(s[row][col]);}
				else continue;
			}
			result+="\n";
		}
		return result;
	}
	// ================================================================================================
	// ================================================================================================
	//TODO:Deprecate if never used
	private <E> void typeCheck(Class<E> type, Object o){
		if (o!=null&&!type.isInstance(o)){
			throw new ClassCastException(badElementMsg(type, o));
		}
	}
	// ================================================================================================
	// ================================================================================================
	private <E> String badElementMsg(Class<E> type, Object o){
		return "Attempt to use "+o.getClass()+" element with element type "+type;
	}
}
//================================================================================================