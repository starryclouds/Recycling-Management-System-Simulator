/**
 * @author Sonali Chaudhry, Angela Laar
 * COEN 160 Winter 2017 Final Project
 * The GUI to login to a RMOS. Returns to this screen upon logout.
 * username = admin
 * password = coen160
 */

package rmos;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * The RMOS's login GUI. Will display the RMOS homepage GUI if login information is correct.
 */
@SuppressWarnings("serial")
public class RMOSLogin extends JFrame implements ActionListener{
	RMOS rmos; 
	JTextField username;
	JPasswordField password;
	JButton login;
	JLabel userIcon, banner, label1, label2;
	int count;

	/**
	 * The constructor for the login window. 
	 * @param x the RMOS the login window is being constructed for
	 */
	RMOSLogin(RMOS x){
		super("RMOS");
		rmos = x;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	int height = screenSize.height;
    	int width = screenSize.width;
    	System.out.println(width);
    	setSize(width/2, height/2);
    	setLocation(0+(width / 2), 0);
    	
    	Container container = getContentPane();
		container.setLayout(new FlowLayout());
		Color teal = new Color(0, 150, 136);
		container.setBackground(teal);
		
		JPanel Login = new JPanel();
		Login.setPreferredSize(new Dimension(120,150));
		Login.setBackground(teal);
		
		// where the username & password are entered
		username = new JTextField(10);
		password = new JPasswordField(10); 
		
		// the login button
		login = new JButton("Login");
		login.setForeground(teal);
		login.setBackground(Color.white);
		login.setActionCommand("LOGIN");
		login.addActionListener(this);
		
		// login instructions
		banner = new JLabel("Please enter the username and password to access the RMOS System");
		banner.setForeground(Color.white);
		banner.setFont(new Font("Ariel", Font.PLAIN, 16));
		
		label1 = new JLabel("Username");
		label1.setForeground(Color.white);
		label1.setFont(new Font("Ariel", Font.PLAIN, 12));
		
		label2 = new JLabel("Password");
		label2.setForeground(Color.white);
		label2.setFont(new Font("Ariel", Font.PLAIN, 12));
		
		Login.add(label1);
		Login.add(username);
		Login.add(label2);
		Login.add(password);
		Login.add(login);
		
		container.add(banner, JComponent.TOP_ALIGNMENT);
		container.add(Login, JComponent.CENTER_ALIGNMENT);
		
		count = 0;
		
    	setVisible(true);
    	
    	// creates the RMOS homepage so it is ready to display upon login but hidden until then
		rmos.displayHomepage(this);
		rmos.homepage.setVisible(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("LOGIN")){
			if(isCorrectPassword(password.getPassword()) && isCorrectUsername(username.getText())){
				rmos.homepage.setVisible(true);
				this.setVisible(false);
				password.setText("");
			}
			else{
				JOptionPane.showMessageDialog(this, "Sorry you entered the wrong password/username");
			}
		}
	} // what happens when the login button is clicked

	/**
	 * @param username
	 * @return true or false based on whether the username is valid or not
	 */
	private boolean isCorrectUsername(String username) {
		boolean isCorrect = false;
		String name = "admin";
		
		if(username.equals(name))
			isCorrect = true;
		
		return isCorrect;
	}
	
	/**
	 * @param pw
	 * @return true or false based on whether the password is correct or not
	 */
	private boolean isCorrectPassword(char [] pw) {
		boolean isCorrect = false;
		String password = "coen160";
		
		if(String.valueOf(pw).equals(password))
			isCorrect = true;
		
		return isCorrect;
	}
}