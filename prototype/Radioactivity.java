// Radioactive decay calculations
import java.util.*;

public class Radioactivity implements Runnable {

    Map<String,Integer> rssiMap;
    String[] sources;
    
    public Radioactivity(Map<String,Integer> rssiMap) {
	this.rssiMap = rssiMap;
    }
    
    public void run() {
	// Dose is energy (joules)
	double dose = 0;
	
	while (true) {
	    // totalPower is power of all beacons combined (watts)
	    double totalPower = 0;
	    
	    System.out.println("RSSI dump");
	    for (Integer signal : rssiMap.values()) {
		System.out.println("RSSI " + signal);
		if (signal == null) continue; // Beacon not yet seen

		double power = Math.pow(10, (double)signal/10);
		totalPower += power;
		System.out.println("Beacon power: "+power);
	    }
	    dose += totalPower;
	    System.out.println("Power: "+totalPower+ " dose: " + dose);
	    
	    try {
		Thread.sleep(1000);
	    } catch (InterruptedException e) {
		System.out.println("Spurious interrupt");
	    }
	}
    }
}
