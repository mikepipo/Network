/*
 *  (c) K.Bryson, Dept. of Computer Science, UCL (2013)
 */

package physical_network;

//import java.util.Hashtable;

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
	
	private double voltage = 0;
	
	//Hashtable<String, Double> voltages = new Hashtable<String, Double>();
	
    public synchronized void setVoltage(String device, double voltage) {
    	//voltages.put(device, voltage);
    	this.voltage = voltage;
    }
        
    public synchronized double getVoltage(String device) {
    	/*
    	double voltage_test = 0;
    	for (Double value : voltages.values()) {
    	    voltage_test += value;;
    	}
    	
    	return voltage_test;
    	*/
    	
    	return this.voltage;
    }
}
