/* NewUserPrompt.java
 * Defines a new window to prompt for new user name
 * Question regarding threading is in the comments.
 * Current state: Runs as expected. Awaiting further tests.
 * Author: Qua Zi Xian
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//Defines a thread to prompt for new user name
class NewUserThread implements Runnable{
	private CAP_calculator returnTarget;

	//Constructor
	public NewUserThread(CAP_calculator rtnTgt) { returnTarget = rtnTgt; }

	//Runnable method
	public void run(){
		new NewUserPrompt(returnTarget);
		while(returnTarget.getUser()==null){
			/* Why is this a must?
			 * Without this, when new user prompt closes
			 * the loop still continues infinitely 
			 */
			try{
				Thread.sleep(1000);
			}
			catch(InterruptedException exp){}
		}
	}
}

//Defines the new user prompt window
public class NewUserPrompt extends JFrame implements ActionListener{
	private CAP_calculator returnTarget;
	private JPanel row1 = new JPanel();
	private JLabel nameLabel = new JLabel("Name: ");
	private JTextField name = new JTextField(20);
	private JPanel row2 = new JPanel();
	private JButton enterButton = new JButton("Enter");

	public NewUserPrompt(CAP_calculator returnTarget){
		super("New User");
		this.returnTarget = returnTarget;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getRootPane().setDefaultButton(enterButton);
		setVisible(true);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		row1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		row1.add(nameLabel);
		row1.add(name);
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(row1, gbc);

		row2.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		enterButton.addActionListener(this);
		row2.add(enterButton);
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(row2, gbc);

		pack();
	}

	//Implements ActionListener method
	public void actionPerformed(ActionEvent event){
		if(!name.getText().equals("")){
			returnTarget.addUser(name.getText());
			dispose();
		}
	}
}
