// Radioactive decay calculations
import java.util.*;

public class Radioactivity implements Runnable {

    Map<String,Integer> rssiMap;
    String[] sources;
    
    public Radioactivity(Map<String,Integer> rssiMap) {
	this.rssiMap = rssiMap;
    }
    
    public void run() {
	while (true) {
	    System.out.println("RSSI dump");
	    for (Integer signal : rssiMap.values()) {
		System.out.println("RSSI " + signal);
	    }

	    try {
		Thread.sleep(1000);
	    } catch (InterruptedException e) {
		System.out.println("Spurious interrupt");
	    }
	}
    }
}
