/*
 *  (c) K.Bryson, Dept. of Computer Science, UCL (2013)
 */

package physical_network;

import javax.xml.bind.DatatypeConverter;


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
    //private final int MAX_PAYLOAD_SIZE = 1500;

    
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
    	
    	for (byte b : frame.getPayload())
    	{	
    		// Convert byte type to 8 bit binary string.
    		String binary_str = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
    		
    		// Print out binary string (for checking).
    		System.out.println("[*] " + binary_str);
    		
    		// Iterate through binary string to set the voltage high/low accordingly.
    		for(char bit : binary_str.toCharArray())
    		{
    			if (bit == '1')
    			{
    				wire.setVoltage(deviceName, HIGH_VOLTAGE);
    				sleep(PULSE_WIDTH);
    			}
    			else
    			{
    				wire.setVoltage(deviceName, LOW_VOLTAGE);
    				sleep(PULSE_WIDTH);
    			}
    		}
    		
    	}
    	
    	wire.setVoltage(deviceName, 0.0);
    	
    }
    
    public static String toHexString(byte[] array) 
    {
        return DatatypeConverter.printHexBinary(array);
    }

    public static byte[] toByteArray(String s) 
    {
        return DatatypeConverter.parseHexBinary(s);
    }
    
	public void run() {
		
		if (listener != null) {
			
			System.out.println("WAITING TO RECEIVE DATA FRAMES");
					        		
			
			String data = new String();
			
			try {
	        	
	        	while (true) {
	        		
	                int count = 0;
	                String byte_data = new String();
	                
	                while (count < 8)
	                {
	                	double voltage = wire.getVoltage(deviceName);                
	                	//System.out.println(voltage);
	                	
		                if (voltage < -2.0)
		                {
		                	//System.out.println("0");
		                	byte_data += '0';
		                	count++;
		                }
		                if (voltage > 2.0)
		                {
		                	//System.out.println("1");
		                	byte_data += '1';
		                	count++;
		                }
		                
		                sleep(200);
		                
	                }
	                
	                data += byte_data;
	                
	                System.out.println(Integer.toHexString(Integer.parseInt(byte_data, 2)));
	                
	                DataFrame recieved_dataframe = new DataFrame(toByteArray(data));
	                
	                listener.receive(recieved_dataframe);
	            }
	        	

	        } catch (InterruptedException except) {
	            System.out.println("Netword Card Interrupted: " + getName());
	        }
			
			
		
		}
		
	}
	
}
