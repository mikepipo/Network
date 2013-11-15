/*
 *  (c) K.Bryson, Dept. of Computer Science, UCL (2013)
 */

package physical_network;

import java.util.Hashtable;

/**
 * 
 * %%%%%%%%%%%%%%%% YOU NEED TO IMPLEMENT THIS %%%%%%%%%%%%%%%%%%
 * 
 * Concrete implementation of the Twisted Wire Pair.
 *
 * This implementation will simply ADD TOGETHER all current voltages set
 * by different devices attached to the wire.
 * 
 * Thus you may have "Network Card A" device setting voltages to transfer bits
 * across the wire and at the same time a "Thermal Noise" device which
 * is setting random voltages on the wire. These voltages should then
 * be added together so that getVoltage() returns the sum of voltages
 * at any particular time.
 * 
 * Similarly any number of network cards may be attached to the wire and
 * each be setting voltages ... the wire should add all these voltages together.
 * 
 * @author K. Bryson
 */
class MyTwistedWirePair implements TwistedWirePair {
	
	// Hash table for storing voltages for different devices.
	Hashtable<String, Double> voltages = new Hashtable<String, Double>();
	
	// setVoltage and getVoltage not atomic, so synchronize them.
	
    public synchronized void setVoltage(String device, double voltage) {
    	
    	voltages.put(device, voltage);
 
    }
      
    public synchronized double getVoltage(String device) {
    	
    	double voltage = 0;
    	
    	// Sum all the voltage values in the hash table.
    	for (Double value : voltages.values()) {
    	    voltage += value;
    	}
    	
    	return voltage;
    }
}
