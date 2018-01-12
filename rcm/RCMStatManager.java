/**
 * @author Sonali Chaudhry, Angela Laar
 * COEN 160 Winter 2017 Final Project
 * The class that handles the back-end data management stuff for a specific RCM.
 */

package rcm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * The Recycled class, defines a data type to store the type of item recycled and its date
 */
class Recycled {
	Date date;
	String type;
	double weight;
	double price;

	public Recycled(Date date, String type, double weight, double price) {
		this.date = date;
		this.type = type;
		this.weight = weight;
		this.price = price;
	}

	@Override
	public String toString() {
		return "Type of item: " + type + ", weight: " + weight + ", date recycled: " + date
				+ ", price: $" + price;
	} // defines what is shown if a Recycled object is printed
}

/**
 * The Emptied class, defines a data type to store the date and weight an RCM was emptied
 */
class Emptied {
	Date date;
	double weight;

	public Emptied(Date date, double weight) {
		this.date = date;
		this.weight = weight;
	}
}

// the class that takes care of the behind the scenes data management / calculation for a specific RCM
public class RCMStatManager {
	private RecycledDataManager recyc = new RecycledDataManager();
	private EmptiedDataManager em = new EmptiedDataManager();
	private ArrayList<Recycled> recycled;
	private ArrayList<Emptied> emptied;
	public static double CAPACITY; // how much the RCM can initially hold
	private double weight = 0;
	double money = 100;
	double moneyWithSession = 100;
	private String rFile;
	private String eFile;
	
	/**
	 * default capacity of every RCM is 100 pounds
	 * if not set from the file, default current weight = 0 pounds, default money = $100.00
	 * @param rFilename where the information about a specific RCM's recycle history is stored
	 * @param eFilename where the information about a specific RCM's empty history is stored
	 */
	public RCMStatManager (String rFilename, String eFilename) {
		this.rFile = rFilename;
		this.eFile = eFilename;
		recyc.readDataFromFile(rFilename);
		em.readDataFromFile(eFilename);
		this.recycled = recyc.getData();
		this.emptied = em.getData();
		
		CAPACITY = 100.00;
		
		this.weight = recyc.getInitialWeight();
		this.money = recyc.getInitialMoney();
	}
	
	/**
	 * sets the date of the Recycled object to the current date
	 * @param type the type of item being recycled
	 * @param weight the weight of the item being recycled
	 * @param price the money recieved for the item
	 */
	public void recycleAnItem(String type, double weight, double price) {
		if (this.weight + weight <= CAPACITY) {
			this.recycled.add(new Recycled(new Date(), type, weight, price));
			//System.out.println(this.recycled.get(this.recycled.size()-1));
			this.weight += weight;
			this.moneyWithSession -= price;
		}
	}
	
	/**
	 * empties the RCM only if it holds minimum 75 lbs.
	 */
	public void emptyRCM() {
		if (weight > 75) {
			emptied.add(new Emptied(new Date(), weight));
			weight = 0;
		}
	}
	
	/**
	 * restocks the RCM's money so it has $100.00
	 */
	public void restockMoney() { this.money = 100.00; }
	
	/**
	 * @return how much money a RCM currently has
	 */
	public double getCurrentMoney() { return money; }

	/**
	 * @return the weight of the items the RCM currently holds
	 */
	public double getCurrentWeight() { return weight; }
	
	/**
	 * @return the free space a RCM currently has
	 */
	public double getCurrentCapacity() { return CAPACITY - weight; }
	
	/**
	 * @return the list of things ever recycled at a RCM
	 */
	public ArrayList<Recycled> getRecycledItems() { return this.recycled; }
	
	/**
	 * @param date
	 * @return the number of items recycled since date
	 */
	public int getNumItems(Date date) {
		int count = 0;
		for (Recycled r : recycled) {
			if (r.date.compareTo(date) >= 0)
				count++;
		}
		return count;
	}
	
	/**
	 * @return the total number of items recycled at that RCM since the beginning of computer time
	 */
	public int getTotalUse() {
		return getNumItems(new Date(0));
	}

	/**
	 * @param date
	 * @return the weight of the items recycled since date
	 */
	public double getWeightItems(Date date) {
		double wt = 0;
		for (Recycled r : recycled) {
			if (r.date.compareTo(date) >= 0)
				wt = wt + r.weight;
		}
		return wt;
	}
	
	/**
	 * @return the weight of all items ever recycled at a specific RCM
	 */
	public double getLifetimeWeight() {
		return getWeightItems(new Date(0));
	}
	
	/**
	 * @param date
	 * @return the total amount of money a specific RCM has issued since a sepcific date
	 */
	public double getMoneyIssuedSince (Date date) {
		double m = 0;
		for (Recycled r : recycled) {
			if (r.date.compareTo(date) >= 0)
				m += r.price;
		}
		return m;
	}
	
	/**
	 * @return the total amount of money a specific RCM has issued ever
	 */
	public double getTotalMoneyIssued () {
		return getMoneyIssuedSince(new Date(0));
	}
	
	/**
	 * @param date
	 * @return the number of times a RCM has been emptied since date
	 */
	public int getNumEmptiesSince (Date date) {
		int n = 0;
		for (Emptied e : emptied) {
			if (e.date.compareTo(date) >= 0)
				n++;
		}
		return n;
	}
	
	/**
	 * @return the total number of times a RCM has been emptied
	 */
	public int getTotalEmpties() {
		return getNumEmptiesSince(new Date(0));
	}
	
	/**
	 * @return the date (in string format) the last time a specific RCM was emptied
	 */
	public String getLastEmpty() {
		ArrayList<Date> d = new ArrayList<Date>();
		for (Emptied e : emptied) {
			d.add(e.date);
		}
		if (d.isEmpty()) {
			return "Never been emptied";
		} else {
			Date lastEmptied = Collections.max(d);
			DateFormat df = new SimpleDateFormat("EEE MMM dd, yyyy");
			String le = df.format(lastEmptied);
			return le;
		}
	}
	
	/**
	 * writes the recycled & empty data history of the RCM to a file for persistence
	 */
	public void close() {
		this.recyc.writeDataToFile(rFile, this);
		this.em.writeDataToFile(eFile, this.emptied);
	}
}