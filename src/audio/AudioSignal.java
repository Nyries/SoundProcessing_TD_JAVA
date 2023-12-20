package audio;

import javax.sound.sampled.*;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/** A container for an audio signal backed by a double buffer so as to allow floating point calculation
 2 * for signal processing and avoid saturation effects. Samples are 16 bit wide in this implementation. */
public class AudioSignal {

    private double[] sampleBuffer; // floating point representation of audio samples
    private double dBlevel; // current signal level

    /**
     * Construct an AudioSignal that may contain up to "frameSize" samples.
     *
     * @param frameSize the number of samples in one audio frame
     */
    public AudioSignal(int frameSize) {
        this.sampleBuffer = new double[frameSize];
    }

    /**
     * Sets the content of this signal from another signal.
     *
     * @param other other.length must not be lower than the length of this signal.
     */
    public void setFrom(AudioSignal other) {
        if(other.sampleBuffer.length>=this.sampleBuffer.length){
            System.arraycopy(other.sampleBuffer,0,this.sampleBuffer,0,this.sampleBuffer.length);
        } else {
            throw new IllegalArgumentException("The other signal length is lower than the length of this signal.");
        }
    }

    public double[] getSignalData() {
        return this.sampleBuffer;
    }

    /**
     * Fills the buffer content from the given input. Byte's are converted on the fly to double's.
     * @return false if at end of stream
     */
    public boolean recordFrom(TargetDataLine audioInput) {
        byte[] byteBuffer = new byte[sampleBuffer.length * 2]; // 16 bit samples
        if (audioInput.read(byteBuffer, 0, byteBuffer.length) == -1) return false;
        for (int i = 0; i < sampleBuffer.length; i++)
            sampleBuffer[i] = ((byteBuffer[2 * i] << 8) + byteBuffer[2 * i + 1]) / 32768.0; // big endian
        // ... TODO : dBlevel = update signal level in dB here ...
        updateSignalLevel();
        return true;
    }

    /**
     * Plays the buffer content to the given output.
     *
     * @return false if at end of stream
     */
    public boolean playTo(SourceDataLine audioOutput){
        byte[] byteBuffer = new byte[sampleBuffer.length * 2]; // 16 bit samples
        for (int i = 0; i < byteBuffer.length; i+=2) {
            byteBuffer[i] = (byte)(sampleBuffer[i]*256);
            byteBuffer[i+1] = (byte)(sampleBuffer[i]*32768%256);
        }
        if (audioOutput.write(byteBuffer, 0, byteBuffer.length) == -1) return false;
        // ... TODO : dBlevel = update signal level in dB here ...
        return true;
    }
    private void updateSignalLevel(){
        double sum=0.0;
        for (double sample: sampleBuffer){
            sum+=sample*sample;
        }
        double rms = Math.sqrt((sum/ sampleBuffer.length));
        dBlevel=20.0*Math.log10(rms);
    }
    public void setSample(int i, double value){
        if (i>=0 && i< sampleBuffer.length){
            sampleBuffer[i]=value;
        }
    }
    public double getSample(int i){
        return (i>=0 && i< sampleBuffer.length) ? sampleBuffer[i]:0.0;
    }
    public double getdBlevel(){
        return dBlevel;
    }

    public int getFrameSize(){
        return sampleBuffer.length;
    }

    /**public Complex[] computeFFT() {
        int n = sampleBuffer.length;

        // Use Apache Commons Math library for FFT
        FastFourierTransformer transformer = new FastFourierTransformer();
        Complex[] transformed = transformer.transform(sampleBuffer, TransformType.FORWARD);

        return transformed;
    }*/

    // your job: add getters and setters ...
    // double getSample(int i)
    // void setSample(int i, double value)
    // double getdBLevel()
    // int getFrameSize()
    // Can be implemented
}
