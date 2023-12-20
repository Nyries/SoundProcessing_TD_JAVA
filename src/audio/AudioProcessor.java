package audio;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
/** The main audio processing class, implemented as a Runnable so
 * as to be run in a separated execution Thread. */
public class AudioProcessor implements Runnable {
    private AudioSignal inputSignal, outputSignal;
    private TargetDataLine audioInput;
    private SourceDataLine audioOutput;
    private boolean isThreadRunning; // makes it possible to "terminate" thread

    /**
     * Creates an AudioProcessor that takes input from the given TargetDataLine, and plays back
     * to the given SourceDataLine.
     *
     * @param frameSize the size of the audio buffer. The shorter, the lower the latency.
     */

    public AudioProcessor(TargetDataLine audioInput, SourceDataLine audioOutput, int frameSize) {
        this.inputSignal = new AudioSignal(frameSize);
        this.outputSignal = new AudioSignal(frameSize);
        this.audioInput = audioInput;
        this.audioOutput = audioOutput;

    }

    public void startAudioProcessing() {
        // Check if the thread is not already running to avoid starting multiple threads
        if (!isThreadRunning) {
            // Start the audio processing thread
            new Thread(this).start();
        }
    }

    public void stopAudioProcessing() {
        // Terminate the audio processing thread
        terminateAudioThread();

        // Wait for the thread to finish (optional)
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Audio processing thread code. Basically an infinite loop that continuously fills the sample
     * buffer with audio data fed by a TargetDataLine and then applies some audio effect, if any,
     * and finally copies data back to a SourceDataLine.
     */
    @Override
    public void run() {
        isThreadRunning = true;
        while (isThreadRunning) {
            inputSignal.recordFrom(audioInput);
            // your job: copy inputSignal to outputSignal with some audio effect
            for (int i = 0; i < inputSignal.getFrameSize(); i++) {
                double sample = inputSignal.getSample(i);
                double processedSample = sample;
                outputSignal.setSample(i, processedSample);
            }
            outputSignal.playTo(audioOutput);
        }
    }

    /** Tells the thread loop to break as soon as possible. This is an asynchronous process. */
    public void terminateAudioThread() {
        isThreadRunning=false;
    }
    public AudioSignal getInputSignal(){
        return inputSignal;
    }
    public AudioSignal getOutputSignal() {
        return outputSignal;
    }
        /* an example of a possible test code */
       /** public static void main(String[] args) {
            // Créez les lignes audio
            TargetDataLine inLine = AudioIO.obtainAudioInput("Default Audio Device", 16000);
            SourceDataLine outLine = AudioIO.obtainAudioOutput("Default Audio Device", 16000);

            // Créez une instance de AudioProcessor
            AudioProcessor audioProcessor = new AudioProcessor(inLine, outLine, FrameSize.FRAME_SIZE_1024);

            // Ouvrez et démarrez les lignes audio
            inLine.open();
            inLine.start();
            outLine.open();
            outLine.start();

            // Créez et démarrez le thread AudioProcessor
            Thread audioProcessorThread = new Thread(audioProcessor);
            audioProcessorThread.start();

            // Attendez un certain temps pour le test (par exemple, 10 secondes)
            Thread.sleep(10000);

            // Arrêtez le thread AudioProcessor de manière asynchrone
            audioProcessor.terminateAudioThread();

            // Attendez que le thread AudioProcessor se termine
            audioProcessorThread.join();

            // Fermez les lignes audio
            inLine.close();
            outLine.close();
            // Gérez les exceptions de manière appropriée dans une application réelle
        }*/
}