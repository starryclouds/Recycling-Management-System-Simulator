/**
 * @author Sonali Chaudhry, Angela Laar
 * COEN 160 Winter 2017 Final Project
 * The class that reads/writes the data regarding the items recycled at a specific RCM from/to a file.
 */

package rcm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Defines the data getter interface
 */
interface DataGetter {
	public void readDataFromFile(String fileName);
}

/**
 * Defines the data putter interface
 */
interface DataSetter {
	public void writeDataToFile(String fileName, Object o);
}


/**
 * RecycledDataManager class reads RCM recycle data from a text file and loads it into an ArrayList,
 * 	and reads data from the ArrayList and writes it out to a text file.
 */
public class RecycledDataManager implements DataGetter, DataSetter {
	private ArrayList<Recycled> recycled;
	private double weight = 0;
	private double money = 100;
	
	public RecycledDataManager() {
		recycled = new ArrayList<Recycled>();
	}

	/**
	 * Reads data in from a file called whatever string is stored in filename.
	 * Adds the data to an ArrayList of Recycled items that stores what has been recycled and when.
	 */
	public void readDataFromFile(String fileName) {
		Recycled r;

		BufferedReader reader = null;
		try {
			File inFile = new File(fileName);
			
			// creates the file if it doesn't exist
			if (!inFile.isFile())
				inFile.createNewFile();

			reader = new BufferedReader(new FileReader(inFile));

			// ... Loop as long as there are input lines.
			String line = null;
			int count = 0;

			try {
				while ((line = reader.readLine()) != null) {
					count++; // counts number of lines read from file

					// split each line into tokens
					String[] fields = line.split(" ");

					if (count == 1) {
						this.weight = Double.parseDouble(fields[0].trim());
					} else if (count == 2) {
						this.money = Double.parseDouble(fields[0].trim());
					} else {
						// the String to parsed/converted to correct type
						int month = Integer.parseInt(fields[0].trim()) - 1;
						int day = Integer.parseInt(fields[1].trim());
						int year = Integer.parseInt(fields[2].trim());
						String type = fields[3].trim();
						double weight = Double.parseDouble(fields[4].trim());
						double price = Double.parseDouble(fields[5].trim());

						Calendar cal = Calendar.getInstance();
						cal.set(year, month, day);

						// create a new Recycled object
						r = new Recycled(cal.getTime(), type, weight, price);

						// add the Recycled object to the ArrayList
						recycled.add(r);
					}
				}
			} finally { }
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		} catch (NumberFormatException e) {
			System.out.println("NumberFormatException: " + e);
			System.exit(1);
		}
	}

	/**
	 * @return ArrayList<Recycled> full of the data read in from the file
	 */
	public ArrayList<Recycled> getData() {
		return recycled;
	}
	
	/**
	 * @return how much money was in the RCM after it was last closed
	 */
	public double getInitialMoney() { return money; }

	/**
	 * @return the weight of the items that were in the RCM after it was last closed
	 */
	public double getInitialWeight() { return weight; }
	
	/**
	 * writes the data of items recycled out to a file denoted by filename
	 * (data is from the ArrayList contained in the Object rc)
	 */
	public void writeDataToFile(String fileName, Object rc) {
		RCMStatManager sm = (RCMStatManager) rc;
		
		// updates the recycled object so it contains the most up-to-date information
		recycled = sm.getRecycledItems();
		
		BufferedWriter writer = null;
		try {
			File outFile = new File(fileName);
			writer = new BufferedWriter(new FileWriter(outFile));

			// ... Loop as long as there is something to be written to the file
			String line = null;

			try {
				line = new Double(sm.getCurrentWeight()).toString() + " weight";
				writer.write(line);
				writer.newLine();
				line = new Double(sm.getCurrentMoney()).toString() + " price";
				writer.write(line);
				writer.newLine();
				for (Recycled r : sm.getRecycledItems()) {
					DateFormat df = new SimpleDateFormat("MM dd yyyy");
					String le = df.format(r.date);
					line = le + " " + r.type + " " + r.weight + " " + r.price;
					writer.write(line);
					writer.newLine();
				}
			} finally {
				writer.close();
			}
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}
	}
}