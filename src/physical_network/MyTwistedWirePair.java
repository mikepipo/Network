/*
 *  (c) K.Bryson, Dept. of Computer Science, UCL (2013)
 */

package physical_network;

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
	
    public void setVoltage(String device, double voltage) {
    }
        
    public double getVoltage(String device) {
    	return 0.0;
    }
}
