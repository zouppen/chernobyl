// Parses btmon output
import java.io.*;
import java.util.*;
import java.util.regex.*;

class Receiver {
    public static void main (String args[])
    {
	// Line data reader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 

	Pattern p = Pattern.compile("^(>).*|^[ \t]*([^:]*): *([^ ]*).*");

	String mac = null;
	Integer rssi = null;
	boolean found = false;
	Map<String, Integer> rssiMap = new HashMap<String, Integer>();

	// Set up which MAC addresses to follow
	for (String arg : args) {
	    rssiMap.put(arg, null);
	}
	
	// Start the game in a thread
	Radioactivity radioactivity;
	try {
	    radioactivity = new Radioactivity(0);
	} catch (Exception e) {
	    System.out.println("Error while creating game object: " + e);
	    return;
	}
	Thread radioactivityThread = new Thread(radioactivity);
	radioactivityThread.start();

	while (true) {
	    String line;
	    try {
		line = reader.readLine();
		if (line == null) break;
	    } catch (IOException e) {
		System.out.println("I/O error while reading stdin");
		break;
	    }
	    
	    // Match regex
	    Matcher m = p.matcher(line);
	    if (!m.matches()) continue;

	    // Got match
	    if (">".equals(m.group(1))) {
		// New packet indicator, reset the state
		mac = null;
		rssi = null;
		found = false;
	    } else if ("Address".equals(m.group(2))) {
		mac = m.group(3);
	    } else if ("RSSI".equals(m.group(2))) {
		rssi = Integer.valueOf(m.group(3));
	    }

	    // Collect the packet data, do it now
	    if (!found && mac != null && rssi != null) {
		found = true;
		if (rssiMap.containsKey(mac)) {
		    // We are listening to this MAC, so recording data
		    Integer oldRssi = rssiMap.put(mac, rssi);

		    // Report to debug screen
		    if (oldRssi == null) {
			System.out.println("Beacon found: "+mac);
		    }

		    // Report update to the game. It linearizes
		    // decibels for us.
		    radioactivity.updateSum(oldRssi, rssi);
		}
	    }
	}

	System.out.println("btmon closed");
    }
}
