/**
 * @author Sonali Chaudhry, Angela Laar
 * COEN 160 Winter 2017 Final Project
 * The class that defines the RCM (connects the display and the back-end stuff).
 */

package rcm;

import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;

public class RCM extends Observable {
	private String RCMID;
	private String location;
	private static int CAPACITY = 100;
	private HashMap<String, Double> itemsAccepted;
	RCMStatManager sm;
	private final double MAX = 5.00;
	private final double MIN = 0.01;
	private double sessionTotal = 0.00;
	RCMDisplay display;

	public RCM(String RCMID, String location) {
		this.RCMID = RCMID;
		this.location = location;
		itemsAccepted = new HashMap<String, Double>();
		itemsAccepted.put("glass", 1.00);
		itemsAccepted.put("paper", 0.80);
		itemsAccepted.put("plastic", 0.65);
		sm = new RCMStatManager("Recycle" + RCMID, "Empty" + RCMID);
		display = new RCMDisplay(this);
	} // RCM constructor, creates an RCM with the given ID and location
	// if data for the given ID already exists it will read in data from the file

	/**
	 * @return the RCM's ID
	 */
	public String getRCMID() {
		return RCMID;
	}

	/**
	 * @return the RCM's location
	 */
	public String getRCMLocation() {
		return location;
	}

	/**
	 * sets or changes the RCM's location
	 * @param location the new location
	 */
	public void setRCMLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the list of the type of items accepted at the RCM
	 */
	public HashMap<String, Double> getItemsAccepted() {
		return itemsAccepted;
	}
	
	/**
	 * checks whether the item type represented by key is accepted at the RCM
	 * @param key the type of the item
	 * @return true (item is acceptable) or false (item is not accepted)
	 */
	public boolean hasAcceptedItem(String key) {
		return this.itemsAccepted.containsKey(key);
	}

	/**
	 * adds a new item and price to Hash map of accepted recyclable items
	 * @param item the type of the item (for example, wires, paper, glass, etc.)
	 * @param price the per pound price given to the user for recycling an item of this type
	 */
	public void addToItemsAccepted(String item, Double price) {
		item = item.toLowerCase();
		itemsAccepted.put(item, price);
	}

	/**
	 * changes the price of existing item in Hash map of accepted recyclable items
	 * @param item the already existing acceptable item
	 * @param price the new price of the item ( per lb. price)
	 */
	public void updateItemsAccepted(String item, Double price) {
		item = item.toLowerCase();
		if (itemsAccepted.containsKey(item)) {
			itemsAccepted.replace(item, price);
		}
	}

	/**
	 * restocks the money at the RCM
	 */
	public void restockWithMoney() {
		sm.restockMoney();
	}
	
	/**
	 * empties the RCM
	 */
	public void emptyRCM() {
		sm.emptyRCM();
	}

	/**
	 * @return the generated random weight between MAX and MIN for when an item is recycled
	 */
	private double generateWeight() {
		Random rand = new Random();
		double range = MAX - MIN;
		double weight = rand.nextDouble() * range + MIN;
		weight = Math.round(weight * 100.00) / 100.00;
		return weight;
	}

	/**
	 * recycles the item if possible (if there is available capacity)
	 * @param type the type of item being recycled
	 * @return information about the recycled item if it worked (i.e. weight, money received)
	 */
	public String addRecyclable(String type) {
		double weight = generateWeight();
		if (weight > sm.getCurrentCapacity())
			return "Not enough room in the RCM. Please end the session to get your money.";
		double priceForWeight = Math.round(weight * itemsAccepted.get(type) * 100.00) / 100.00;
		priceForWeight = Math.round(priceForWeight * 100.00) / 100.00;
		sm.recycleAnItem(type, weight, priceForWeight);
		sessionTotal += priceForWeight;
		sessionTotal = Math.round(sessionTotal*100.00)/100.00;
		
		setChanged();
		this.notifyObservers();
		
		if (type.equals("paper"))
			return String.format("%14s", type) + "    " + String.format("%.2f", weight) + "      $" + String.format("%.2f", priceForWeight);
		else
			return String.format("%15s", type) + "    " + String.format("%.2f", weight) + "      $" + String.format("%.2f", priceForWeight);
	}

	/**
	 * @return the total capacity of the RCM
	 */
	public static int getCapacity() {
		return CAPACITY;
	}
	
	/**
	 * @return how much money the user is supposed to receive so far in the session
	 */
	public double getSessionTotal() {
		return sessionTotal;
	}
	
	/**
	 * sets the session total (how much money the user is set to receive thus far in the session)
	 */
	public void setSessionTotal(double s) {
		sessionTotal = s;
	}
	
	/**
	 * ends the session and notifies those observing the RCM that something has happened
	 */
	public void endSession() {
		setChanged();
		notifyObservers();
	}
	
	/**
	 * @return the RCM's GUI
	 */
	public RCMDisplay getDisplay () {
		return this.display;
	}
	
	/**
	 * @param date
	 * @return the weight of the items recycled at the RCM since date
	 */
	public double getWeightItems (Date date) {
		return sm.getWeightItems(date);
	}
	
	/**
	 * @param date
	 * @return the total money issued at the RCM since date
	 */
	public double getMoneyIssuedSince (Date date) {
		return sm.getMoneyIssuedSince(date);
	}
	
	/**
	 * @param date
	 * @return the number of times the RCM has been emptied since date
	 */
	public int getNumEmptiesSince (Date date) {
		return sm.getNumEmptiesSince(date);
	}
	
	/**
	 * for our purposes it made sense to calculate total use based on the total
	 * 	number of items ever recycled at the RCM because our weights are generated randomly
	 * @return the total use based on the total number of items recycled at the RCM ever
	 */
	public int getTotalUse() {
		return sm.getTotalUse();
	}
	
	/**
	 * @return the date the RCM was last emptied in String format
	 */
	public String getLastEmpty() {
		return sm.getLastEmpty();
	}
	
	/**
	 * @return the amount of money the RCM currently has
	 */
	public double getCurrentMoney() {
		return sm.getCurrentMoney();
	}
	
	/**
	 * @return the weight of the recycled items currently in the RCM
	 */
	public double getCurrentWeight() {
		return sm.getCurrentWeight();
	}
	
	/**
	 * writes the RCM recycle/empty data to the appropriate files when closing
	 */
	public void close() {
		this.sm.close();
	}
}
