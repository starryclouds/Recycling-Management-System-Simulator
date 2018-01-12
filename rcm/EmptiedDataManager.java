/**
 * @author Sonali Chaudhry, Angela Laar
 * COEN 160 Winter 2017 Final Project
 * The class that reads/writes the data regarding the empties of a specific RCM from/to a file.
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
 * EmptiedDataManager class reads RCM empty data from a text file and loads it into an ArrayList
 */
public class EmptiedDataManager implements DataGetter, DataSetter {
	private ArrayList<Emptied> emptied = new ArrayList<Emptied>();

	/**
	 * reads the data from a text file and loads it into an ArrayList data structure
	 */
	public void readDataFromFile(String fileName) {
		Emptied empt;

		BufferedReader reader = null;
		try {
			File inFile = new File(fileName);
			
			// creates a new file if it doesn't exits
			if (!inFile.isFile())
				inFile.createNewFile();
			
			reader = new BufferedReader(new FileReader(inFile));

			// ... Loop as long as there are input lines.
			String line = null;

			try {
				while ((line = reader.readLine()) != null) {

					// split each line into tokens
					String[] fields = line.split(" ");

					// the String to number type conversion happens here
					int month = Integer.parseInt(fields[0].trim()) - 1;
					int day = Integer.parseInt(fields[1].trim());
					int year = Integer.parseInt(fields[2].trim());
					double weight = Double.parseDouble(fields[3].trim());

					Calendar cal = Calendar.getInstance();
					cal.set(year, month, day);

					// creates a new Emptied object to store the weight and date of the empty
					empt = new Emptied(cal.getTime(), weight);
					
					// adds the Emptied object to the ArrayList for future reference
					emptied.add(empt);
				}
			} finally { }
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		} catch (NumberFormatException e) {
			System.out.println("NumberFormatException: ");
			System.exit(1);
		}
	}

	/**
	 * the getter function for the data read in from the file
	 * @return ArrayList<Emptied> full of the empty data read in from the file
	 */
	public ArrayList<Emptied> getData() {
		return emptied;
	}

	/**
	 * writes the empty data to a file so the RCM can pick up where it left off even after it is closed
	 */
	@SuppressWarnings("unchecked")
	public void writeDataToFile(String fileName, Object em) {
		this.emptied = (ArrayList<Emptied>) em;
		BufferedWriter writer = null;
		try {
			File outFile = new File(fileName);
			writer = new BufferedWriter(new FileWriter(outFile));

			// ... Loop as long as there is something to be written to the file
			String line = null;

			try {
				for (Emptied e : emptied) {
					DateFormat df = new SimpleDateFormat("MM dd yyyy");
					String le = df.format(e.date);
					line = le + " " + e.weight;
					writer.write(line);
					writer.newLine();
				} // iterates through the array and writes the data to the file
			} finally {
				writer.close();
			}
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}
	} // reads the data from an ArrayList data structure and writes it to the text file denoted by filename			
}
