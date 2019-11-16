import java.util.*;

public class Geiger {

    public final static int sampleRate = 48000;
    
    // FIR bandbass filter coefficients. 1500...4000 Hz, transition
    // 1000 attenuation -40dB, filter gain 5dB. For 48000Hz sample
    // rate. Generated with GNU Radio filter design tool
    // `gr_filter_design` from file `geiger_filter.txt`.
    private final static double[] taps = {-0.0039298152551054955, -0.002834566170349717, -0.001579446136020124, -0.0004835568252019584, 7.777287100907415e-05, -0.00026653401437215507, -0.0017551189521327615, -0.004326836671680212, -0.007481124252080917, -0.010249346494674683, -0.01134132593870163, -0.009476615116000175, -0.003834627103060484, 0.005511336959898472, 0.017344482243061066, 0.02938353270292282, 0.03874972090125084, 0.042754318565130234, 0.03980882093310356, 0.03017161227762699, 0.016239147633314133, 0.0021827230229973793, -0.007088879588991404, -0.007441231980919838, 0.0028932192362844944, 0.022137144580483437, 0.044661302119493484, 0.06181688234210014, 0.06387463212013245, 0.04270077869296074, -0.0054770177230238914, -0.07798644155263901, -0.16500216722488403, -0.25061720609664917, -0.31566405296325684, -0.34178197383880615, -0.31584128737449646, -0.23365293443202972, -0.10197810083627701, 0.061798807233572006, 0.23243165016174316, 0.3815132975578308, 0.4831225574016571, 0.5191560983657837, 0.4831225574016571, 0.3815132975578308, 0.23243165016174316, 0.061798807233572006, -0.10197810083627701, -0.23365293443202972, -0.31584128737449646, -0.34178197383880615, -0.31566405296325684, -0.25061720609664917, -0.16500216722488403, -0.07798644155263901, -0.0054770177230238914, 0.04270077869296074, 0.06387463212013245, 0.06181688234210014, 0.044661302119493484, 0.022137144580483437, 0.0028932192362844944, -0.007441231980919838, -0.007088879588991404, 0.0021827230229973793, 0.016239147633314133, 0.03017161227762699, 0.03980882093310356, 0.042754318565130234, 0.03874972090125084, 0.02938353270292282, 0.017344482243061066, 0.005511336959898472, -0.003834627103060484, -0.009476615116000175, -0.01134132593870163, -0.010249346494674683, -0.007481124252080917, -0.004326836671680212, -0.0017551189521327615, -0.00026653401437215507, 7.777287100907415e-05, -0.0004835568252019584, -0.001579446136020124, -0.002834566170349717, -0.0039298152551054955};
    
    private final Random random = new Random();
    private FIR fir = new FIR(taps);

    public double getSample(double rate) {
	// Simulating geiger counter by calculating the beep
	// interval using exponential distribution quantile
	// function:
	// https://en.wikipedia.org/wiki/Exponential_distribution
	double interval = -Math.log(1-random.nextDouble())/rate;

	// If the beep interval is less than the length of this
	// sample, it means the Geiger-Müller tube conducts during
	// this sample – we hear a beep.
	boolean beep = interval * sampleRate < 1;

	// Simulating geiger counter piezo speaker by generating
	// sample "1" in case of conductivity and 0 otherwise. The
	// audio is then fed into FIR filter which does bandbass
	// filter to simulate the perceived sound.
	double filteredSample = fir.getOutputSample(beep ? 1 : 0);

	return filteredSample;
    }
}
