/**
 * @author Sonali Chaudhry, Angela Laar
 * COEN 160 Winter 2017 Final Project
 * The class that defines the GUI for a single RCM.
 */

package rcm;

import java.awt.*;

import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

@SuppressWarnings("serial")
public class RCMDisplay extends JPanel implements ActionListener {
	RCM rcm;
	private JButton start, end; // the session buttons
	private JScrollPane scroll, scrollButtons;
	private JTextArea screen, total; // the text areas to display session information
	private ArrayList<JButton> buttons = new ArrayList<JButton>(); // to hold the buttons for each type of item acceptable item at an RCM
	private boolean inSession = false; // tells us whether we are currently in a session or not
	JPanel itemsToRecycle = new JPanel(); // the GUI for a single RCM

	/**
	 * constructs a new GUI for the RCM r
	 * @param r thr RCM the GUI is being constructed for
	 */
	public RCMDisplay(RCM r) {
		rcm = r;
		itemsToRecycle.setLayout(new FlowLayout());
		
		// allows the user to scroll through the types of items accepted at a given RCM
		scrollButtons = new JScrollPane(itemsToRecycle);
		scrollButtons.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollButtons.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// creates a button for each acceptable type of item for a RCM
		if (rcm.getItemsAccepted().size() > 0) {
			for (String item : rcm.getItemsAccepted().keySet()) {
				makeBtn(item);
			}
		}
		
		// adds the buttons to the GUI
		updateButtons();
		
		itemsToRecycle.setBackground(Color.WHITE);

		this.setLayout(new BorderLayout());
		this.setBackground(Color.gray);

		Color teal = new Color(0, 150, 136);
		Color cyan = new Color(224, 247, 250);

		// the other panel (the one not including the type of item buttons)
		JPanel recyclePanel = new JPanel();
		recyclePanel.setLayout(new FlowLayout());
		recyclePanel.setBackground(cyan);

		// defines the start session button
		start = new JButton("Start Session");
		start.setForeground(teal);
		start.setBackground(Color.white);
		start.setActionCommand("START");
		start.addActionListener(this);

		// defines the end session button
		end = new JButton("End Session");
		end.setForeground(teal);
		end.setBackground(Color.white);
		end.setActionCommand("END");
		end.addActionListener(this);
		
		// defines the main text area (the "receptacle" or the display that tells you what you have recycled & for how much money) 
		screen = new JTextArea(15, 40);
		screen.setEditable(false);
		screen.setBackground(Color.black);
		screen.setForeground(Color.white);
		screen.setFont(new Font("Courier", Font.PLAIN, 16));
		screen.append("\n");
		screen.append("\n");
		screen.append("\n");
		screen.append("     PRESS 'START SESSION' TO BEGIN");
		
		// makes the main text area scrollable if need be
		scroll = new JScrollPane(screen);

		total = new JTextArea(2, 30);
		total.setBackground(Color.black);

		recyclePanel.add(start);
		recyclePanel.add(end);
		recyclePanel.add(scroll);
		recyclePanel.add(total);

		// label the RCM with the RCM ID and location
		JPanel header = new JPanel();
		header.setLayout(new FlowLayout());
		JLabel rcmInfo = new JLabel(rcm.getRCMID() + " - " + rcm.getRCMLocation());

		header.setBackground(teal);
		header.add(rcmInfo);

		// adds everything 
		add(header, BorderLayout.NORTH);
		add(scrollButtons, BorderLayout.WEST);
		add(recyclePanel, BorderLayout.CENTER);
	}

	/**
	 * updates the buttons visible on the RCM GUI
	 */
	public void updateButtons() {
		itemsToRecycle.removeAll();
		itemsToRecycle.setLayout(new FlowLayout());

		for (JButton b : buttons) {
			String key = b.getActionCommand().toLowerCase();
			Double value = rcm.getItemsAccepted().get(key);
			JLabel price = new JLabel(key + " - $" + String.format("%.2f", value));
			itemsToRecycle.add(b);
			itemsToRecycle.add(price);
		}
		
		itemsToRecycle.setPreferredSize(new Dimension(150, 130*buttons.size()));
		itemsToRecycle.revalidate();
		itemsToRecycle.setVisible(true);

		revalidate();
		setVisible(true);
	}
	
	/**
	 * the ActionListener function to tell the buttons what to do if a specific button is clicked
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("GLASS")) {
			if (inSession) {
				String item = rcm.addRecyclable("glass");
				screen.append(item);
				screen.append("\n");
				total.setText("Total: $" + String.format("%.2f", rcm.getSessionTotal()));
			} // only works when in a session
		} // what happens when the glass button is clicked
		if (e.getActionCommand().equals("PLASTIC")) {
			if (inSession) {
				String item = rcm.addRecyclable("plastic");
				screen.append(item);
				screen.append("\n");
				total.setText("Total: $" + String.format("%.2f", rcm.getSessionTotal()));
			} // only works when in a session
		} // what happens when the plastic button is clicked
		if (e.getActionCommand().equals("PAPER")) {
			if (inSession) {
				String item = rcm.addRecyclable("paper");
				screen.append(item);
				screen.append("\n");
				total.setText("Total: $" + String.format("%.2f", rcm.getSessionTotal()));
			} // only works when in a session
		} // what happens when the paper button is clicked
		if (e.getActionCommand().equals("START")) {
			if (!inSession) {
				this.inSession = true;
				start.setEnabled(false);
				scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				screen.setBackground(Color.white);
				screen.setForeground(Color.black);
				screen.setFont(new Font("Arial", Font.PLAIN, 12));
				screen.setText("");
				screen.append(String.format("%1$15s","Item"));
				screen.append("   Weight    Price");
				screen.append("\n");
				total.append("Total:");
				total.setBackground(Color.white);
			} // can only start a session if not already in a session
		} // what happens when the start session button is clicked
		if (e.getActionCommand().equals("END")) {
			if (inSession) {
				if (rcm.sm.moneyWithSession >= 0) {
					disperseMoney(rcm.getSessionTotal());
					screen.setText("");
					total.setText("");
					rcm.setSessionTotal(0);
					screen.setBackground(Color.black);
					screen.setForeground(Color.white);
					screen.setFont(new Font("Courier", Font.PLAIN, 16));
					screen.append("\n");
					screen.append("\n");
					screen.append("\n");
					screen.append("     PRESS 'START SESSION' TO BEGIN");
					rcm.sm.money = rcm.sm.moneyWithSession;
				} else {
					disperseCoupon(rcm.getSessionTotal());
					screen.setText("");
					total.setText("");
					screen.setBackground(Color.black);
					screen.setForeground(Color.white);
					screen.setFont(new Font("Courier", Font.PLAIN, 16));
					screen.append("\n");
					screen.append("\n");
					screen.append("\n");
					screen.append("     PRESS 'START SESSION' TO BEGIN");
					rcm.setSessionTotal(0);
					rcm.sm.moneyWithSession = rcm.sm.money;
				}
				rcm.endSession();
			    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
				start.setEnabled(true);
				inSession = false;
			} // can only end a session if currently in a session
		} // what happens when the end session button is clicked
	}
	
	/**
	 * the popup letting the user know they got total amount money after ending a session
	 * @param total the amount of money the user receives at the end of the session
	 */
	public void disperseMoney(double total) {
		JOptionPane.showMessageDialog(this,
				"You have recieved $" + String.format("%.2f", total) + " for your recycled items");
	}

	/**
	 * the popup letting the user know they got a coupon for total amount money after ending a session
	 * @param total the amount of money the user receives at the end of the session
	 */
	public void disperseCoupon(double total) {
		JOptionPane.showMessageDialog(this, "There are insufficient funds in the RCM. Here is a coupon of $"
				+ String.format("%.2f", total) + " for your recyled Items");
	}
	
	/**
	 * the function that is called when a new recyclable item needs to be added to the RCM display
	 * @param item the item type the button is being made for
	 */
	public void makeBtn(String item) {
		item = item.toLowerCase();
		JButton btn;
		if (item.equals("plastic") || item.equals("glass") || item.equals("paper")) {
			ImageIcon img = new ImageIcon(this.getClass().getResource(item + ".png"));
			btn = new JButton(img);
		} else {
			ImageIcon img = new ImageIcon(this.getClass().getResource("recycle.png"));
			btn = new JButton(img);
			btn.setName(item);
		} // selects the image that is going to be the item's icon
		
		// sets the button's size
		btn.setPreferredSize(new Dimension(100, 100));
		btn.setSize(new Dimension(100, 100));
		
		// sets or defines the button's actionListener
		if (item.equals("glass") || item.equals("plastic") || item.equals("paper")) {
			btn.setActionCommand(item.toUpperCase());
			btn.addActionListener(this);
		} else {
			btn.setActionCommand(item.toUpperCase());
			btn.addActionListener(new AbstractAction(item.toUpperCase()) {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (inSession) {
						// recycles the item
						String it = rcm.addRecyclable(btn.getActionCommand().toLowerCase());
						
						// updates the RCM's screen to show the info for the item recycled
						screen.append(it);
						screen.append("\n");
						
						// updates the total money being received for the session
						total.setText("Total: $" + String.format("%.2f", rcm.getSessionTotal()));
					}
				}
				
			});
		}
		// adds the buttons to the RCM display's button panel
		buttons.add(btn);
	}
}