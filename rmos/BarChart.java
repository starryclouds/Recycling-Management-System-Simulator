/**
 * @author Sonali Chaudhry, Angela Laar
 * COEN 160 Winter 2017 Final Project
 * The class that defines the BarChart that gets displayed in the RMOS GUI.
 * To change the time span covered by the BarChart, call the setDate method prior to repainting the BarChart.
 */

package rmos;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.swing.JPanel;

/**
 * The BarChart class that creates and draws a bar graph
 */
@SuppressWarnings("serial")
public class BarChart extends JPanel {
	RMOSStatManager rmosSM;
	private ArrayList<Double> weights;
	private ArrayList<Double> money;
	private ArrayList<Integer> empties;
	private ArrayList<String> ids;
	private Date date;

	Color c_blue = new Color(10, 216, 244);
	Color c_green = new Color(67, 145, 8);
	Color c_pink = new Color(242, 25, 97);
	Color c_purple = new Color(162, 41, 236);

	/**
	 * The BarChart constructor, accepts an RMOS stat manager and a date to define the time span the bar chart covers
	 * @param sm the RMOSStatManager
	 */
	public BarChart(RMOSStatManager rsm, Date date) {
		this.rmosSM = rsm;
		this.weights = new ArrayList<Double>();
		this.money = new ArrayList<Double>();
		this.empties = new ArrayList<Integer>();
		this.ids = new ArrayList<String>();
		this.date = date;
		for (int i = 0; i < rsm.RCMs.size(); i++) {
			weights.add(Math.round(rsm.RCMs.get(i).getWeightItems(date) * 100.00) / 100.00);
			money.add(Math.round(rsm.RCMs.get(i).getMoneyIssuedSince(date) * 100.00) / 100.00);
			empties.add(rsm.RCMs.get(i).getNumEmptiesSince(date));
			ids.add(rsm.RCMs.get(i).getRCMID());
		}
	}

	/**
	 * Draws the bar graph by drawing rectangles. Updates the data to be drawn first.
	 */
	@Override
	protected void paintComponent(Graphics gp) {
		super.paintComponent(gp);
		this.updateValues();
		// Cast the graphics objects to Graphics2D
		Graphics2D g = (Graphics2D) gp;
		if (Collections.max(weights) > 0) {
			// determine longest bar
			double max = Double.MIN_VALUE;
			for (Double value : weights) {
				max = Math.max(max, value);
			}
			for (Double value : money) {
				max = Math.max(max, value);
			}
			for (Integer value : empties) {
				max = Math.max(max, value);
			}

			// paints the bars
			int width = ((getWidth() / (weights.size() + money.size() + empties.size())) - 3);
			int x = 1;
			for (int i = 0; i < weights.size(); i++) {
				double value = weights.get(i);
				int height = (int) ((getHeight() - 5) * ((double) value / max));
				g.setColor(c_blue);
				g.fillRect(x, getHeight() - height, width, height);
				g.setColor(Color.black);
				g.drawRect(x, getHeight() - height, width, height);

				g.setFont(new Font("Impact", Font.BOLD, 15));
				String str = weights.get(i) + "lbs.";
				g.drawString(str, x + width / 2 - g.getFontMetrics().stringWidth(str) / 2, getHeight() - height + 20);
				// adds labels to the bottom center of each rectangle

				x += width;
				value = money.get(i);
				height = (int) ((getHeight() - 5) * ((double) value / max));
				g.setColor(c_green);
				g.fillRect(x, getHeight() - height, width, height);
				g.setColor(Color.black);
				g.drawRect(x, getHeight() - height, width, height);

				str = ids.get(i);
				g.drawString(str, x + width / 2 - g.getFontMetrics().stringWidth(str) / 2, getHeight() - 10);
				str = "$" + money.get(i);
				g.drawString(str, x + width / 2 - g.getFontMetrics().stringWidth(str) / 2, getHeight() - height + 20);

				x += width;
				value = empties.get(i);
				height = (int) ((getHeight() - 5) * ((double) value / max));
				g.setColor(c_pink);
				g.fillRect(x, getHeight() - height, width, height);
				g.setColor(Color.black);
				g.drawRect(x, getHeight() - height, width, height);

				str = empties.get(i) + "empties";
				g.drawString(str, x + width / 2 - g.getFontMetrics().stringWidth(str) / 2, getHeight() - height - 2);

				x += (width + 2);
			}
		} else {
			g.setFont(new Font("Impact", Font.BOLD, 15));
			String str = "Nothing to display. Waiting...";
			g.drawString(str, 0 + getWidth() / 2 - g.getFontMetrics().stringWidth(str) / 2, getHeight() / 2);
		}
	}
	
	/**
	 * Updates the values stored in this object so that the most up-to-date information is displayed in the BarChart.
	 */
	public void updateValues() {
		weights.clear();
		money.clear();
		empties.clear();
		ids.clear();
		for (int i = 0; i < rmosSM.RCMs.size(); i++) {
			weights.add(Math.round(rmosSM.RCMs.get(i).getWeightItems(date) * 100.00) / 100.00);
			money.add(Math.round(rmosSM.RCMs.get(i).getMoneyIssuedSince(date) * 100.00) / 100.00);
			empties.add(rmosSM.RCMs.get(i).getNumEmptiesSince(date));
			ids.add(rmosSM.RCMs.get(i).getRCMID());
		}
	}
	
	/**
	 * sets the date so that the BarChart covers the time span from that date to today
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * returns the preferred size based on how many bars there are in the graph
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension((weights.size() + money.size() + empties.size()) * 10 + 2, 50);
	}
}