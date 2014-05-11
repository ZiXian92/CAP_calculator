/* CAP_calculator.java
 * Defines the GUI interface of CAP calculator
 * To-do: Add scroll pane to outputArea
 * Decoupling the classes as much as possible and simplify/neaten
 * the code 
 * Current status: Runs as expected, awaiting further tests 
 * Author: Qua Zi Xian
 */

import java.text.DecimalFormat;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Iterator;

public class CAP_calculator extends JFrame implements ActionListener{
	User user;

	//Components
	//Menu bar
	JMenuBar menuBar = new JMenuBar();
	JMenu menu1 = new JMenu("File");
	JMenuItem save = new JMenuItem("Save");

	//Defines the 1st row components
	private JPanel row1 = new JPanel();

	//Panel containing the module code field
	private JPanel modPane = new JPanel();
	private JLabel module = new JLabel("Module: ", JLabel.RIGHT);
	private JTextField modField = new JTextField(7);

	//Panel containing the grade field
	private JPanel gradePane = new JPanel();
	private JLabel grade = new JLabel("Grade: ", JLabel.RIGHT);
	private JTextField gradeField = new JTextField(2);

	//Panel containing the MC field
	private JPanel mcPane = new JPanel();
	private JLabel mcLabel = new JLabel("MC: ", JLabel.RIGHT);
	private JTextField mcField = new JTextField(1);

	//Button to enter updated information
	JPanel addButtonPane = new JPanel();
	JButton add = new JButton("Add");

	//Defines the 2nd row components
	JPanel row2 = new JPanel();
	JButton listDetails = new JButton("List Modules");
	JButton showCAP = new JButton("Show CAP");

	//Defines the 3rd row components
	JPanel row3 = new JPanel();
	JLabel output = new JLabel("Output: ", JLabel.RIGHT);
	JTextArea outputArea = new JTextArea(10, 40);
	JScrollPane scroll = new JScrollPane(outputArea);

	//Constructor
	public CAP_calculator(){
		super("CAP Calculator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		getRootPane().setDefaultButton(add);

		//Starts the file input thread
		ReadUserThread reader = new ReadUserThread();
		Thread readThread = new Thread(reader);
		readThread.start();

		//Sets the menu bar
		save.addActionListener(this);
		menu1.add(save);
		menuBar.add(menu1);
		setJMenuBar(menuBar);

		//Use the GridBagLayout
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		//Renders the 1st row
		FlowLayout flo1 = new FlowLayout(FlowLayout.CENTER, 10, 10);
		row1.setLayout(flo1);
		modPane.setLayout(flo1);
		gradePane.setLayout(flo1);
		modPane.add(module);
		modPane.add(modField);
		gradePane.add(grade);
		gradePane.add(gradeField);
		mcPane.setLayout(flo1);
		mcPane.add(mcLabel);
		mcPane.add(mcField);
		addButtonPane.add(add);
		add.addActionListener(this);
		row1.add(modPane);
		row1.add(gradePane);
		row1.add(mcPane);
		row1.add(addButtonPane);
		//Specifies which grid cell to place this JPanel into
		c.gridx = 0;
		c.gridy = 0;
		add(row1, c);

		row2.setLayout(flo1);
		listDetails.addActionListener(this);
		showCAP.addActionListener(this);
		row2.add(listDetails);
		row2.add(showCAP);
		c.gridx = 0;
		c.gridy = 1;
		add(row2, c);

		//Renders the 3rd row
		row3.setLayout(flo1);
		row3.add(output);
		outputArea.setEnabled(false);
		row3.add(scroll);
		c.gridx = 0;
		c.gridy = 2;
		add(row3, c);

		pack();

		//Checks if the thread has ended
		//Because the outcome of the file input thread is needed here
		while(readThread.isAlive()){
			try{
				Thread.sleep(1000);
			}
			catch(InterruptedException exp){}
		}
		user = reader.getUser();
		if(user==null){
			outputArea.append("No user detected\n");
			readThread = new Thread(new NewUserThread(this));
			setInputs(false);
			readThread.start();
			while(readThread.isAlive()){
				/*try{
					Thread.sleep(1000);
				}
				catch(InterruptedException exp){}*/
			}
		}
		else	outputArea.append("Load successful\n");
		outputArea.append("Welcome, "+user.getName()+"\n");
		setInputs(true);
	}

	//Toggles on/off the inputs based on the boolean option provided
	//True for enabling and false for disabling
	public void setInputs(boolean sw){
		save.setEnabled(sw);
		modField.setEnabled(sw);
		gradeField.setEnabled(sw);
		mcField.setEnabled(sw);
		add.setEnabled(sw);
		listDetails.setEnabled(sw);
		showCAP.setEnabled(sw);
	}

	//Implements method to handle ActionEvents
	public void actionPerformed(ActionEvent event){
		Object source = event.getSource();
		if(save==source){
			boolean success = CAPCalcMethods.saveList(user);
			if(success)
				outputArea.append("Save successful!\n");
			else
				outputArea.append("Output file cannot be created or there is no user defined.\n");
		}
		if(add==source){
			if(modField.getText().equals("") || 
				gradeField.getText().equals("") ||
				mcField.getText().equals("")){
				outputArea.append("Module code, Grade, or MC field is empty.\n");
				return;
			}
			user.addModule(modField.getText(), gradeField.getText(), Integer.parseInt(mcField.getText()));
			modField.setText("");
			gradeField.setText("");
			mcField.setText("");
			outputArea.append("Module details added.\n");
		}
		if(listDetails==source){
			outputArea.append(user.toString()+"\n");
		}
		if(showCAP==source){
			try{
				outputArea.append("CAP: "+(new DecimalFormat("0.00")).format(user.getCAP())+"\n");	
			}
			catch(ArithmeticException exp){
				outputArea.append("Not applicable: No module taken\n");
			}

		}
	}

	//Creates a new user based on input user name
	public void addUser(String username) { user = new User(username); }

	User getUser() { return user; }
	
	//Main method to start program
	public static void main(String[] args){
		CAP_calculator prog = new CAP_calculator();	
	}
}
