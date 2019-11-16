// Radioactive decay calculations
import java.util.*;
import java.io.*;

public class Radioactivity implements Runnable {

    // Geiger counter sensitivity. Larger values; more beeps
    private static double sensitivity = 1e11;
    private static int screenFps = 2;

    private Map<String,Integer> rssiMap;
    private String[] sources;
    private Geiger geiger = new Geiger();

    // Power of all signal combined (watts)
    private double powerAll;

    // Write raw samples to a FIFO. FIXME temporary.
    BufferedWriter audio;
    
    // Update current radiation level. Old value must be given so we
    // can substract it before adding new one. Units are decibels!
    public synchronized void updateSum(Integer minusdB, Integer plusdB) { 
	if (minusdB != null) {
	    powerAll -= linearize(minusdB);
	}
	powerAll += linearize(plusdB);
	System.out.println("Updated RSSI to " + plusdB);
    }
    
    public Radioactivity(double backgroundRadiation) throws IOException {
	this.powerAll = backgroundRadiation;
	this.audio = new BufferedWriter(new FileWriter("/tmp/geiger.raw"));
    }

    public static double linearize(double dB) {
	return Math.pow(10, (double)dB/10);
    }
    
    public void run() {
	// Dose is energy (joules)
	double dose = 0;

	// Screen is display counter
	int screenSleep = 0;

	// Run for every sample. TODO must be rate-limited
	while (true) {
	    // Energy of this sample is power of all beacons divided
	    // by samples per second
	    double energy = powerAll / Geiger.sampleRate;

	    // Calculating cumulative dose.
	    dose += energy;

	    // Generating sound
	    double rate = energy * sensitivity;
	    double sample = geiger.getSample(rate);
	    short sampleShort = (short)(sample * Short.MAX_VALUE);	    

	    // Update screen
	    if (screenSleep == 0) {
		System.out.println("Power: " + powerAll + " dose: " + dose);
		screenSleep = Geiger.sampleRate / screenFps;
	    }
	    screenSleep--;

	    try {
		audio.write(sampleShort & 0xff);
		audio.write(sampleShort >> 8);
	    } catch (IOException e) {
		System.out.println("WRITE ERROR");
		return;
	    }
	}
    }
}
