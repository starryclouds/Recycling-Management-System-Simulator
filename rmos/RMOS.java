/**
 * @author Sonali Chaudhry, Angela Laar
 * COEN 160 Winter 2017 Final Project
 * The RMOS class. Mainly the homepage GUI that is seen upon login.
 */

package rmos;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rcm.RCM;

public class RMOS implements ActionListener, Observer {
	
	RMOSStatManager smRMOS; // the rmos stat manager
	String rcmID; // the current RCM ID selected in the combobox
	RMOSLogin lioScreen; // login screen
	
	RMOS(){
		smRMOS = new RMOSStatManager();
	} // constructor, creates the RMOSStatManager for this RMOS
	
	/**
	 * @param r adds the RCM r to the list of RCMs managed by the RMOS
	 */
	public void addRCM(RCM r){
		smRMOS.addRCM(r);
		r.addObserver(this);
	}
	
	//components after login
	JPanel RCMFrame, mainFrame, createRCM;
	BarChart graphFrame;
	JButton addItemChangePrice, addRCM, viewMoney, EmptyRCM,viewWeight, restockMoney, logout;
	JLabel lastEmptied, mostUsed;
	JFrame homepage = new JFrame();
	JComboBox<String> rCM;
	JTextField money, weight, newItem, newPrice, RCMID, locn, item, price;
	Color teal = new Color(0, 150, 136);
	
	/**
	 * Creates the homepage of the RMOS (not displayed until login) with all the appropriate buttons and text fields (and a barchart)
	 * @param s the login screen of the RMOS, set back to this after logout
	 */
	public void displayHomepage(RMOSLogin s){
		lioScreen = s;
				
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	int height = screenSize.height;
    	int width = screenSize.width;
    	
    	homepage.setSize(width, height/2);
    	homepage.setLocation(0+(width/2), 0);
    	
    	Color gray = new Color(250, 250, 250);
    	homepage.setBackground(gray);
    	homepage.setLayout(new GridLayout(2,2));
    	
    	mainFrame = new JPanel();
    	mainFrame.setLayout(new FlowLayout());
    	    		
    	JLabel Money = new JLabel("Money in RCM");
		Money.setForeground(teal);
		Money.setFont(new Font("Ariel", Font.PLAIN, 12));
		
		money = new JTextField(5);
		money.setEnabled(false);
		
		JLabel Weight = new JLabel("Weight of Items in RCM");
		Weight.setForeground(teal);
		Weight.setFont(new Font("Ariel", Font.PLAIN, 12));
		
		weight = new JTextField(5);
		weight.setEnabled(false);
    	
    	addItemChangePrice = new JButton("Add Item/Change Price");
		addItemChangePrice.setForeground(teal);
		addItemChangePrice.setBackground(Color.white);
		addItemChangePrice.setActionCommand("ADD");
		addItemChangePrice.addActionListener(this);
				
		viewMoney = new JButton("View Money in RCM");
		viewMoney.setForeground(teal);
		viewMoney.setBackground(Color.white);
		viewMoney.setActionCommand("VIEW_M");
		viewMoney.addActionListener(this);
		
		restockMoney = new JButton("Restock Money in RCM");
		restockMoney.setForeground(teal);
		restockMoney.setBackground(Color.white);
		restockMoney.setActionCommand("RESTOCK_M");
		restockMoney.addActionListener(this);
		
		viewWeight = new JButton("View Weight in RCM");
		viewWeight.setForeground(teal);
		viewWeight.setBackground(Color.white);
		viewWeight.setActionCommand("VIEW_W");
		viewWeight.addActionListener(this);
		
		EmptyRCM = new JButton("Empty");
		EmptyRCM.setForeground(teal);
		EmptyRCM.setBackground(Color.white);
		EmptyRCM.setActionCommand("EMPTY");
		EmptyRCM.addActionListener(this);	
		
		mostUsed = new JLabel("");
		
		//this label is set in the action listener
		lastEmptied = new JLabel(""); 
				
		mainFrame.add(Money);
		mainFrame.add(money);
		mainFrame.add(Weight);
		mainFrame.add(weight);
		
		mainFrame.add(viewMoney);
		mainFrame.add(restockMoney);
		mainFrame.add(viewWeight);
		mainFrame.add(lastEmptied);
		mainFrame.add(EmptyRCM);
		
		mainFrame.add(mostUsed); // NOTE: most used, NOT most recently used
		
		createRCM = new JPanel();
    	createRCM.setLayout(new FlowLayout());
		
    	JLabel id = new JLabel("ID");
		id.setForeground(teal);
		id.setFont(new Font("Ariel", Font.PLAIN, 12));
		
    	JLabel loc = new JLabel("Location");
		loc.setForeground(teal);
		loc.setFont(new Font("Ariel", Font.PLAIN, 12));
		
		JLabel itemToAdd = new JLabel("Item");
		itemToAdd.setForeground(teal);
		itemToAdd.setFont(new Font("Ariel", Font.PLAIN, 12));
		
		JLabel Price = new JLabel("Price");
		Price.setForeground(teal);
		Price.setFont(new Font("Ariel", Font.PLAIN, 12));
		
		RCMID = new JTextField(5);
		locn = new JTextField(10);
		item = new JTextField(10);
		price = new JTextField(10);
    	
    	addRCM = new JButton("Add an RCM");
		addRCM.setForeground(teal);
		addRCM.setBackground(Color.white);
		addRCM.setActionCommand("ADD_R");
		addRCM.addActionListener(this);
		
		createRCM.add(id);
		createRCM.add(RCMID);
		createRCM.add(loc);
		createRCM.add(locn);
		createRCM.add(itemToAdd);
		createRCM.add(item);
		createRCM.add(Price);
		createRCM.add(price);
		createRCM.add(addRCM);
		
		graphFrame = new BarChart(smRMOS, new Date(0)); 
		graphFrame.setLayout(new FlowLayout());
		
		String[] rcms = new String [smRMOS.RCMs.size()];
		
		ArrayList<String> ids = new ArrayList<String>();
		for (RCM r : smRMOS.RCMs) {
			ids.add(r.getRCMID());
		}
		
		rcms = ids.toArray(rcms);
		System.out.println(rcms);
		
		//Start of change/add item panel
		JPanel itemChange = new JPanel();
		itemChange.setLayout(new FlowLayout());
		itemChange.setPreferredSize(new Dimension(400,200));
			
		newItem = new JTextField(10);
		newPrice = new JTextField(8); 
		JLabel label1 = new JLabel("Item");
		label1.setForeground(teal);
		label1.setFont(new Font("Ariel", Font.PLAIN, 12));
				
		JLabel label2 = new JLabel("New Price");
		label2.setForeground(teal);
		label2.setFont(new Font("Ariel", Font.PLAIN, 12));
				
		itemChange.add(label1);
		itemChange.add(newItem);
		
		itemChange.add(label2);
		itemChange.add(newPrice);
		itemChange.add(addItemChangePrice);
		
		//LIST DISPLAY
		//JComboBox<String> rCM;
		rCM = new JComboBox<String>(rcms);
		rCM.addActionListener(this);
		rCM.setSelectedIndex(0);
		
		logout = new JButton("Logout");
		logout.setForeground(teal);
		logout.setBackground(Color.white);
		logout.setActionCommand("LOGOUT");
		logout.addActionListener(this);
		
		
		RCMFrame = new JPanel();
		RCMFrame.setLayout(new FlowLayout());
		
		RCMFrame.add(logout, JComponent.RIGHT_ALIGNMENT);
		RCMFrame.add(rCM);
		RCMFrame.add(itemChange);
		
		// adds everything to the main frame
		Container container = homepage.getContentPane();
		container.add(mainFrame);
		container.add(RCMFrame);
		container.add(createRCM);
		container.add(graphFrame);		
	}
	
	/**
	 * What happen when specific buttons in the RMOS GUI are clicked.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String selected = (String)rCM.getSelectedItem();
		for(RCM r: smRMOS.RCMs){
			if(r.getRCMID().equals(selected)) {
				rcmID = r.getRCMID(); 
				lastEmptied.setText("Last Empty: " + r.getLastEmpty());
				double current = 0;
				current = Math.round(r.getCurrentMoney()*100.00)/100.00;
				money.setText("$"+String.valueOf(current));
				current = 0;
				current = Math.round(r.getCurrentWeight()*100.00)/100.00;
				weight.setText(String.valueOf(current)+"lbs");
			}	
		} //sets the rcmID of focus for whichever button was clicked
		if(e.getActionCommand().equals("ADD")){
			if (newItem.getText().length() > 0 && newPrice.getText().length() > 0) {
				for(RCM r: smRMOS.RCMs){
					if(r.getRCMID().equals(rcmID)) {
						if (r.hasAcceptedItem(newItem.getText())) {
							r.updateItemsAccepted(newItem.getText(), Double.parseDouble(newPrice.getText()));
							r.getDisplay().updateButtons();
						} else {
							r.addToItemsAccepted(newItem.getText(), Double.parseDouble(newPrice.getText()));			
							r.getDisplay().makeBtn(newItem.getText());
							r.getDisplay().updateButtons();
						}
					}
				}
			}
			newItem.setText("");
			newPrice.setText("");
		} // adds the selected type of item accepted or changes the price if it already exists
		if(e.getActionCommand().equals("ADD_R")){
			if (RCMID.getText().length() > 0 && locn.getText().length() > 0) {
				RCM temp = new RCM(RCMID.getText(), locn.getText());
				this.addRCM(temp);
				//got new element
				if(item.getText().length() >0 && price.getText().length()>0 ){
					RCM newRCM = smRMOS.RCMs.get(smRMOS.RCMs.size()-1);
					newRCM.addToItemsAccepted(item.getText(), Double.parseDouble(price.getText()));	
				}
				rCM.addItem(temp.getRCMID());
				RCMID.setText("");
				locn.setText("");
				item.setText("");
				price.setText("");
				
				this.mostUsed.setText("Most Used: " + smRMOS.getMostUsedRCM());
			
				graphFrame.revalidate();
				graphFrame.repaint();
			}
		} // adds the RCM with the given information to the list of RCMS managed by this RMOS
		if(e.getActionCommand().equals("VIEW_M")){
			double current = 0;
			for(RCM r: smRMOS.RCMs){
				if(r.getRCMID().equals(rcmID)) {
					current = Math.round(r.getCurrentMoney()*100.00)/100.00;
					money.setText("$"+String.valueOf(current));
				}
			}
		} // refreshes the money shown for the selected RCM
		if(e.getActionCommand().equals("RESTOCK_M")){
			double current = 0;
			for(RCM r: smRMOS.RCMs){
				if(r.getRCMID().equals(rcmID)) {
					r.restockWithMoney();
					current = Math.round(r.getCurrentMoney()*100.00)/100.00;
					money.setText("$"+String.valueOf(current));
				}
			}
		} // restocks money in the selected RCM
		if(e.getActionCommand().equals("VIEW_W")){
			double current = 0;
			for(RCM r: smRMOS.RCMs){
				if(r.getRCMID().equals(rcmID)) {
					current = Math.round(r.getCurrentWeight()*100.00)/100.00;
					weight.setText(String.valueOf(current)+"lbs");
				}
			}
		} // refreshes the weight shown for the selected RCM
		if(e.getActionCommand().equals("EMPTY")){
			double current = 0;
			for(RCM r: smRMOS.RCMs){
				if(r.getRCMID().equals(rcmID)) {
					r.emptyRCM();
					if(r.getRCMID().equals(rcmID)) {
						current = Math.round(r.getCurrentWeight()*100.00)/100.00;
						weight.setText(String.valueOf(current)+"lbs");
						lastEmptied.setText("Last Empty: " + r.getLastEmpty());
					}
				}
			}
			graphFrame.revalidate();
			graphFrame.repaint();
		} // empties the selected RCM
		if(e.getActionCommand().equals("LOGOUT")){
			homepage.setVisible(false);
			this.lioScreen.setVisible(true);
		} // logs you out of the RMOS
	}
	
	/**
	 * The RMOS observes the RCMs it manages, so this is what occurs when a RCM notifies the RMOS of a change.
	 */
	@Override
	public void update(Observable o, Object arg) {
		String selected = (String)rCM.getSelectedItem();
		for(RCM r: smRMOS.RCMs){
			if(r.getRCMID().equals(selected)){
				rcmID = r.getRCMID(); 
				lastEmptied.setText("Last Empty: " + r.getLastEmpty());
			}
		}
		for(RCM r: smRMOS.RCMs){
			if(r.getRCMID().equals(rcmID)) {
				double current = 0;
				current = Math.round(r.getCurrentMoney()*100.00)/100.00;
				money.setText("$"+String.valueOf(current));
				current = 0;
				current = Math.round(r.getCurrentWeight()*100.00)/100.00;
				weight.setText(String.valueOf(current)+"lbs");
			}
		}
		this.mostUsed.setText("Most Used: " + smRMOS.getMostUsedRCM());
		
		graphFrame.revalidate();
		graphFrame.repaint();
	}
}