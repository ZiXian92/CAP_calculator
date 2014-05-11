/* NewUserPrompt.java
 * Defines a new window to prompt for new user name
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NewUserPrompt extends JFrame{
	private JPanel row1 = new JPanel();
	private JLabel nameLabel = new JLabel("Name: ");
	private JTextField name = new JTextField(20);
	private JPanel row2 = new JPanel();
	private JButton enterButton = new JButton("Enter");

	public NewUserPrompt(){
		super("New User");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
		row2.add(enterButton);
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(row2, gbc);
	}
}
