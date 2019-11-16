// Implementation of desktop audio. TODO Port to Android

import javax.sound.sampled.*;

public class Audio {

    private final AudioFormat af = new AudioFormat(Geiger.sampleRate, 16, 1, true, true);
    private SourceDataLine line;
    private byte[] buffer = new byte[Geiger.sampleRate/50*2]; // 20ms buffer
    private int buffer_i = 0;
    
    public Audio() throws LineUnavailableException {
	line = AudioSystem.getSourceDataLine(af);
	line.open(af, Geiger.sampleRate);
        line.start();
    }

    public void write(short x) {
	// Accumulate buffer for some time before writing it to sound
	// card. Sound card buffer limits rate by blocking.
	buffer[buffer_i++] = (byte)(x >> 8);
	buffer[buffer_i++] = (byte)(x & 0xff);

	if (buffer_i == buffer.length) {
	    line.write(buffer, 0, buffer.length);
	    buffer_i = 0;
	}
    }
}
