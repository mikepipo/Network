/*
 *  (c) K.Bryson, Dept. of Computer Science, UCL (2013)
 */

package physical_network;


/**
 * 
 * %%%%%%%%%%%%%%%% YOU NEED TO IMPLEMENT THIS %%%%%%%%%%%%%%%%%%
 * 
 * Represents a network card that can be attached to a particular wire.
 * 
 * It has only two key responsibilities:
 * i) Allow the sending of data frames consisting of arrays of bytes using send() method.
 * ii) If a data frame listener is registered during construction, then any data frames
 *     received across the wire should be sent to this listener.
 *
 * @author K. Bryson
 */
public class NetworkCard extends Thread {
    
	// Wire pair that the network card is atatched to.
    private final TwistedWirePair wire;

    // Unique device name given to the network card.
    private final String deviceName;
    
    // A 'data frame listener' to call if a data frame is received.
    private final FrameListener listener;

    
    // Default values for high, low and mid- voltages on the wire.
    private final double HIGH_VOLTAGE = 2.5;
    private final double LOW_VOLTAGE = -2.5;
    
    // Default value for a signal pulse width that should be used in milliseconds.
    private final int PULSE_WIDTH = 200;
    
    // Default value for maximum payload size in bytes.
    private final int MAX_PAYLOAD_SIZE = 1500;

    
    /**
     * NetworkCard constructor.
     * @param deviceName This provides the name of this device, i.e. "Network Card A".
     * @param wire       This is the shared wire that this network card is connected to.
     * @param listener   A data frame listener that should be informed when data frames are received.
     *                   (May be set to 'null' if network card should not respond to data frames.)
     */
    public NetworkCard(String deviceName, TwistedWirePair wire, FrameListener listener) {
    	
    	this.deviceName = deviceName;
    	this.wire = wire;
    	this.listener = listener;
    	
    }

    /**
     * Tell the network card to send this data frame across the wire.
     * NOTE - THIS METHOD ONLY RETURNS ONCE IT HAS SENT THE DATA FRAME.
     * 
     * @param frame  Data frame to send across the network.
     */
    public void send(DataFrame frame) throws InterruptedException {
    	
    }


	public void run() {
		
		if (listener != null) {
			
			System.out.println("WAITING TO RECEIVE DATA FRAMES");
			
		}
		
	}
	
}
