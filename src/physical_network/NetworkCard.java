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
    // MAY CHANGE THIS TO 200 ...
    private final int PULSE_WIDTH = 300;
    
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
    	
    	// String object for storing binary string converted from the byte array.
    	String binary_str = new String();
    	
    	// ASCII End of Transmission character (EOT), 0x04.
    	String eot = new String("00000100");
    	
    	for (byte b : frame.getPayload())
    	{	
    		// Convert byte type to 8 bit binary string.
    		binary_str += String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
    	}
    	
    	// Print out Manchester encoded binary string (checking).
    	//System.out.println(ManchesterEncoding(binary_str.concat(eot)));
    	
    	// Append EOT character and send the bits across the wire.
    	for (char bit : ManchesterEncoding(binary_str.concat(eot)).toCharArray())
    	{
    		sendBit(bit);
    	}
    	
    	// Set voltage on the wire back to 0.0V before receiving next message.
    	wire.setVoltage(deviceName, 0.0);
    	sleep(PULSE_WIDTH);
    }
    
    /**
    * Takes a character (bit) from the binary string and sets the voltage on the wire to high/low accordingly.
    * 
    * @param bit  A character (bit) from a binary string.
    */
    private void sendBit(char bit) throws InterruptedException
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
    
    /**
     * Encode binary string using Manchester encoding.
     * Instead of applying XOR to the signal and the clock, simply replace "01" and "10" 
     * where you have original bits 0" and "1" respectively.
     * 
     * @param binary_str  Original binary string converted from the byte array.
     */
    private String ManchesterEncoding(String binary_str)
    {
    	String manchester = new String();
    	
    	for (char c : binary_str.toCharArray())
    	{
    		if (c == '0')
    			manchester += "01";
    		else
    			manchester += "10";
    	}
    	
    	return manchester;
    }
      
    /**
     * Converts a hex string to a byte array.
     * 
     * @param hex  Hex string
     */
    private static byte[] toByteArray(String hex) 
    {
        return DatatypeConverter.parseHexBinary(hex);
    }
    
    /**
     * Reads the voltage on the wire and return a character according to the voltage.
     * 
     * @param frame  Data frame to send across the network.
     */
    private char getBit()
    {
    	double voltage = wire.getVoltage(deviceName);
    	
    	// Signal High
    	if (voltage < -1.0)
    		return '0';
    	
    	// Signal Low
    	if (voltage > 1.0)
    		return '1';
    	
    	// Zero voltage
    	return '2';
    }
    
    /* Manchester Encoding
     * 
     * By using Manchester encoding I will be using twice the number of bits compared to my original binary string.
     * e.g. If I have a binary string "01" I will be sending "0110" instead. 
     * 
     * On the receiving end, I will be watching out for the change in voltage from high to low or vice versa because
     * I know from the Manchester encoding that voltage change from:
     * low to high -> bit 0 and
     * high to low -> bit 1.
     * 
     * One thing to be careful of with this is that if you receive a binary pattern, for example "0101" (original binary string "00"),
     * you do not want to include the voltage transition from high to low between the first "01" and the next "01".
     * 
     * Considering all possible combinations, this problem can only be seen in pattern "0101" (original: "00") and "1010" (original: "11"),
     * i.e. in the original binary string, you are sending the same bits consecutively.
     * 
     * Therefore, I have written the program to notice the problem above and ignore the transition in between so my readings are correct.
     */
    
    /**
     * Returns a hex string for each byte of data received across the wire.
     * 
     * @throws InterruptedException
     */
    private String getByte() throws InterruptedException
    {
    	String binary = new String();
    	String hex = new String();
    	
    	boolean ignore_next_edge = false;
    	
    	System.out.println("WAITING ...");
    	
    	char prev_bit = getBit();
    	sleep(10);
    	
    	while (binary.length() != 8)
    	{
    		char next_bit = getBit();

	    	if (prev_bit == '0' && next_bit == '1')
	    	{
	    		if (!ignore_next_edge)
	    		{
	    			binary += '0';
	    			
	    			// Received a bit, so synchronize the clock.
	    			sleep(PULSE_WIDTH * 3 / 2);
	    			
	    			if (getBit() == prev_bit)
	    				ignore_next_edge = true;
	    		}
	    		else
	    			ignore_next_edge = false;
	    		
	    	}
	    	else if (prev_bit == '1' && next_bit == '0')
	    	{
	    		if (!ignore_next_edge)
	    		{
	    			binary += '1';
	    			
	    			// Received a bit, so synchronize the clock.
	    			sleep(PULSE_WIDTH * 3 / 2);
	    		
	    			if (getBit() == prev_bit)
	    				ignore_next_edge = true;
	    		}
	    		else
	    			ignore_next_edge = false;	
	    	}	
	    	
	    	prev_bit = next_bit;
	    	sleep(10);    		
    	}
    	
    	hex = String.format("%2s", Long.toHexString(Long.parseLong(binary,2))).replace(' ', '0');
    	System.out.println("RECEIVED BYTE = " + hex);
    	
    	return hex;
    }
    
    /**
     * Receives the signal on the wire until the end of transmission and then constructs a
     * DataFrame object with the hex string obtained from the signal.
     * 
     * @throws InterruptedException
     */
    private DataFrame receiveSignal() throws InterruptedException
    {
    	String hex = new String();
    	String hex_bytes = new String();
    	
    	while (true)
    	{
    		hex = getByte();
    		
    		if (hex.equals("04"))
    		{
    			//System.out.println(hex_bytes);
    			return new DataFrame(toByteArray(hex_bytes));
    		}
    		else
    			hex_bytes += hex; 
    	}
    }
    
	public void run() 
	{
		if (listener != null) 
		{
			System.out.println("WAITING TO RECEIVE DATA FRAMES");
					        		
			try {
		        	while (true) 
		        	{
		                if (Math.abs(wire.getVoltage(deviceName)) > 1.0)
		                {
		                	sleep(PULSE_WIDTH / 2);
		                }
		                
		                DataFrame frame = receiveSignal();
		                listener.receive(frame);
		            }
	            }

	        catch (InterruptedException except) 
	        {
	            System.out.println("Netword Card Interrupted: " + getName());
	        }
		}
	}
	
}
