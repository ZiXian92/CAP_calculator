/* CAPCalcMethods.java
 * Backstage processes behind the GUI
 */

import java.io.*;
import java.util.*;

//Defines each module
class Module{
	private String code, grade;
	private int MC;
	public Module(String code, String grade, int mc){
		this.code = code;
		this.grade = grade;
		MC = mc;
	}
	public String getCode() { return code; }
	public String getGrade() { return grade; }
	public int getMC() { return MC; }
	public String toString() { return code+"\t"+grade+"\t"+MC; }

	//Pre-condition: grade is a valid grade
	//To-do: Handle possible exceptions
	public double getGradePoint(){
		double gradePoint;
		switch(grade.charAt(0)){
			case 'A': gradePoint = 5;	break;
			case 'B': gradePoint = 3.5;	break;
			case 'C': gradePoint = 2;	break;
			case 'D': gradePoint = 1;	break;
			default: gradePoint = 0;	break;
		}
		if(grade.length()>1)
			switch(grade.charAt(1)){
				case '+': if(grade.charAt(0)!='A')
						  gradePoint+=0.5;
					  break;
				case '-': gradePoint-=0.5;	break;
			}
		return gradePoint;
	}
}

//Defines each user
//Thurns out the program will only have 1 user
class User{
	/* Data members */
	private String name;
	private LinkedList<Module> modList = new LinkedList<Module>();
	private int totalMC = 0;
	private double totalGradePoint = 0;

	/* Constructor */
	public User(String name) { this.name = name; }

	/* Accessor Methods */
	public String getName() { return name; }
	public int getTotalMC() { return totalMC; }
	public double getCAP() { return totalGradePoint/totalMC; }
	public String toString(){
		String output = name+"\n"+modList.size()+"\n";
		Iterator<Module> itr = modList.iterator();
		while(itr.hasNext()){
			output+=itr.next();
			if(itr.hasNext())
				output+="\n";
		}
		return output;
	}

	/* Mutator Methods */
	public void addModule(String code, String grade, int mc){
		modList.add(new Module(code, grade, mc));
		updateCAP(new Module(code, grade, mc));
	}
	private void updateCAP(Module mod){
		totalMC+=mod.getMC();
		totalGradePoint+=(mod.getGradePoint()*mod.getMC());
	}
}

class ReadUserThread implements Runnable{
	User user;
	boolean complete = true;
	public boolean isComplete() { return complete;}
	public void run(){
		complete = false;
		user = CAPCalcMethods.readData();
		complete = true;
	}
	public User getUser() { return user; }
}

public class CAPCalcMethods{
	//Returns a user or null if input file is empty
	public static User readData(){
		File file = new File("usrlist.dat");
		String input;
		String[] inputs;
		User temp = null;
		int numMods;
		try{
			Scanner iStream = new Scanner(file);
			if(!iStream.hasNext())
				return temp;
			input = iStream.nextLine();
			if(input.equals(""))
				return temp;
			temp = new User(input);
			numMods = Integer.parseInt(iStream.nextLine());
			for(int i=0; i<numMods; i++){
				inputs = iStream.nextLine().split("\t");
				temp.addModule(inputs[0], inputs[1], Integer.parseInt(inputs[2]));
			}
			iStream.close();
		}
		catch(NumberFormatException exp){
			//Temporary handling policy, to be updated for GUI
			System.out.println("Corrupted input file.");
			System.exit(1);
		}
		catch(IOException exp){
			//Temporary handling policy, to be updated for GUI
			System.out.println("Input file not available.");
			System.exit(1);
		}
		return temp;
	}

	//Saves the user list
	public static void saveList(LinkedList<User> list){
		File file = new File("usrlist.dat");
		try{
			PrintWriter oStream = new PrintWriter(file);
			Iterator<User> itr = list.iterator();
			while(itr.hasNext())
				oStream.println(itr.next());
			oStream.close();
		}
		catch(FileNotFoundException exp){

		}
	}
}
