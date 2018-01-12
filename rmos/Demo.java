/**
 * @author Sonali Chaudhry, Angela Laar
 * COEN 160 Winter 2017 Final Project
 * The class that contains the main to run the simulation.
 */

package rmos;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import rcm.RCM;
import rcm.RCMDisplay;

/**
 * The Demo class sets up the simulation so it will start with 2 RCMs and 1 RMOS.
 */
public class Demo {
	RMOS rmos;
	
	public Demo(RMOS rmos) {
		// sets the RMOS GUIs
		this.rmos = rmos;
		new RMOSLogin(rmos);
		
		// the window that will contain the RCMs
		JFrame frame = new JFrame("RCM");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	for (RCM x : rmos.smRMOS.RCMs) {
		    		x.close();;
		    	}
		    }
		}); // when the rcm window is closed, each RCM records its recycle & empty history to their respective files

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = screenSize.height;
		int width = screenSize.width;
		System.out.println(width);
		frame.setSize(width/2, height/2);

		// center the mainFrame on screen
		frame.setLocation(0,height/2);
		
		// lets us see the RCMs in one window as separate tabs
		JTabbedPane tabbedPane = new JTabbedPane();
		RCMDisplay rcm1 = rmos.smRMOS.RCMs.get(0).getDisplay();
		tabbedPane.addTab(rmos.smRMOS.RCMs.get(0).getRCMID(), rcm1);
		RCMDisplay rcm2 = rmos.smRMOS.RCMs.get(1).getDisplay();
		tabbedPane.addTab(rmos.smRMOS.RCMs.get(1).getRCMID(), rcm2);

		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		// allows us to add more RCMs (and have them show up in the RCM GUI) during the demo
		@SuppressWarnings("serial")
		JButton refreshTabs = new JButton(new AbstractAction("Refresh Tabs") {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = tabbedPane.getTabCount() - 1; i < rmos.smRMOS.RCMs.size(); i++) {
					tabbedPane.addTab(rmos.smRMOS.RCMs.get(i).getRCMID(), rmos.smRMOS.RCMs.get(i).getDisplay());
				}
			}
		});
		frame.add(refreshTabs,BorderLayout.SOUTH);
		frame.setVisible(true);
	}
	
	// sets up and runs the demo of the simulation
	public static void main(String[] args) {
		RMOS r = new RMOS();
		RCM r1 = new RCM("RCM1","Carmel");
		RCM r2 = new RCM("RCM2","Big Sur");
		r.addRCM(r1);
		r.addRCM(r2);
		new Demo(r);
	}
}