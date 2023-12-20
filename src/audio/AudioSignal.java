package audio;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ui.SignalView;
import ui.VuMeter;

public class AudioSignal {

    private TargetDataLine targetDataLine;
    private ByteArrayOutputStream byteArrayOutputStream;
    private boolean recording = false;
    private VuMeter vuMeter;
    private SignalView signalView;


    public AudioSignal() {
        initializeAudio();
    }

    private void initializeAudio() {
        try {
            vuMeter = new VuMeter();
            signalView = new SignalView();
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }

            targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(format);

            byteArrayOutputStream = new ByteArrayOutputStream();

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void startCapture() {
        targetDataLine.start();
        new Thread(this::captureAudio).start();
    }

    public void stopCapture() {
        targetDataLine.stop();
    }

    public void toggleRecording() {
        recording = !recording;
        if (recording) {
            byteArrayOutputStream.reset(); // Reset the buffer when starting recording
        }
    }

    public boolean isRecording() {
        return recording;
    }
    public byte[] getRecordedAudio() {
        return byteArrayOutputStream.toByteArray();
    }

    private void captureAudio() {
        byte[] buffer = new byte[1024];

        while (true) {
            int bytesRead = targetDataLine.read(buffer, 0, buffer.length);

            double volume = calculateVolume(buffer,bytesRead);

            // Update the volume property in the JavaFX application thread
            javafx.application.Platform.runLater(() -> vuMeter.setVolume(volume));

            if (recording) {
                try {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private double calculateVolume(byte[] buffer, int bytesRead) {
        long sum = 0;

        // Conversion des octets en échantillons audio 16 bits
        for (int i = 0; i < bytesRead; i += 2) {
            short sample = (short) ((buffer[i + 1] << 8) | (buffer[i] & 0xFF));
            sum += Math.abs(sample)*2;
        }

        // Calcul de la moyenne des valeurs absolues des échantillons audio
        double average = (double) sum / (bytesRead / 2);

        // Normalisation de la moyenne dans la plage [0, 1]
        return average / Short.MAX_VALUE;
    }

    public void playAudio(byte[] audioData) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
        AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, targetDataLine.getFormat(), audioData.length / 4);

        try {
            SourceDataLine line = AudioSystem.getSourceDataLine(audioInputStream.getFormat());
            line.open(audioInputStream.getFormat());
            line.start();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
                line.write(buffer, 0, bytesRead);
                double volume = calculateVolume(buffer,bytesRead);

                // Update the volume property in the JavaFX application thread
                javafx.application.Platform.runLater(() -> vuMeter.setVolume(volume));
            }

            line.drain();
            line.close();

        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }
    public VuMeter getVuMeter() {
        return vuMeter;
    }

    public SignalView getSignalView() {
        return signalView;
    }
}
