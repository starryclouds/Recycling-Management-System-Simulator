/**
 * @author Sonali Chaudhry, Angela Laar
 * COEN 160 Winter 2017 Final Project
 * The class that manages some RCM statistics for the RMOS.
 */

package rmos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import rcm.RCM;

/**
 * The class that manages a few RCM statistics for the RMOS.
 */
public class RMOSStatManager {
	ArrayList<RCM> RCMs;
	// stores which RCMS are managed by a given RMOS
	
	/**
	 * The constructor. The RMOS initially manages no RCMS.
	 */
	public RMOSStatManager() {
		RCMs = new ArrayList<RCM>();
	}
	
	/**
	 * @param r Adds the RCM r to the list of RCMs managed by the RMOS
	 */
	public void addRCM(RCM r) {
		RCMs.add(r);
	}
	
	/**
	 * @return the most used RCM out of the RCMs managed by the RMOS (based on total number of items ever recycled at each RCM)
	 */
	public String getMostUsedRCM() {
		RCM r = Collections.max(RCMs, new Comparator<RCM>() {
			@Override
			public int compare(RCM o1, RCM o2) {
				return o1.getTotalUse() - o2.getTotalUse();
			}
		});
		return r.getRCMID() + " - " + r.getRCMLocation();
	}
}