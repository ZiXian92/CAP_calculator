
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Iterator;

public class CAP_calculator extends JFrame{
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

	//Button to enter updated information
	JPanel addButtonPane = new JPanel();
	JButton add = new JButton("Add");

	//Defines the 2nd row components
	JPanel row2 = new JPanel();
	JLabel output = new JLabel("Output: ", JLabel.RIGHT);
	JTextArea outputArea = new JTextArea(10, 40);

	//Constructor
	public CAP_calculator(){
		super("CAP Calculator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		//Starts the file input thread
		ReadUserThread reader = new ReadUserThread();
		Thread readThread = new Thread(reader);
		readThread.start();

		//Sets the menu bar
		menu1.add(save);
		menuBar.add(menu1);
		setJMenuBar(menuBar);

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		//Renders the 1st row
		FlowLayout flo1 = new FlowLayout(FlowLayout.CENTER, 10, 10);
		row1.setLayout(flo1);
		modPane.setLayout(flo1);
		gradePane.setLayout(flo1);
		modField.setEnabled(false);
		modPane.add(module);
		modPane.add(modField);
		gradeField.setEnabled(false);
		gradePane.add(grade);
		gradePane.add(gradeField);
		add.setEnabled(false);
		addButtonPane.add(add);
		row1.add(modPane);
		row1.add(gradePane);
		row1.add(addButtonPane);
		c.gridx = 0;
		c.gridy = 0;
		add(row1, c);

		//Renders the 2nd row
		FlowLayout flo2 = new FlowLayout(FlowLayout.CENTER, 10, 10);
		row2.setLayout(flo2);
		row2.add(output);
		outputArea.setEnabled(false);
		row2.add(outputArea);
		c.gridx = 0;
		c.gridy = 1;
		add(row2, c);

		pack();

		while(!readThread.isAlive()){}
		user = reader.getUser();
		if(user==null){
			System.out.println("No user data found.");
		}
	}

	public static void main(String[] args){
		CAP_calculator prog = new CAP_calculator();	
	}
}
