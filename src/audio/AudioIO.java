/** A collection of static utilities related to the audio system. */
package audio;

import javax.sound.sampled.*;
import java.util.Arrays;
public class AudioIO {
    /**
     * Displays every audio mixer available on the current system.
     */
    public static void printAudioMixers() {
        System.out.println("Mixers:");
        Arrays.stream(AudioSystem.getMixerInfo())
                .forEach(e -> System.out.println("- name=\"" + e.getName()
                        + "\" description=\"" + e.getDescription() + " by " + e.getVendor() + "\""));
    }

    /**
     * @return a Mixer.Info whose name matches the given string.
     * Example of use: getMixerInfo("Macbook default output")
     */
    public static Mixer.Info getMixerInfo(String mixerName) {
        // see how the use of streams is much more compact than for() loops!
        return Arrays.stream(AudioSystem.getMixerInfo())
                .filter(e -> e.getName().equalsIgnoreCase(mixerName)).findFirst().get();
    }

    /**
     * Return a line that's appropriate for recording sound from a microphone.
     * Example of use:
     * TargetDataLine line = obtainInputLine("USB Audio Device", 8000);
     *
     * @param mixerName a string that matches one of the available mixers
     /* @see AudioSystem.getMixerInfo() which provides a list of all mixers on your system.
     */
    public static TargetDataLine obtainAudioInput(String mixerName, int sampleRate) {
        Mixer.Info mixerInfo=getMixerInfo(mixerName);

        if (mixerInfo!=null) {
            try {
                Mixer mixer = AudioSystem.getMixer(mixerInfo);
                Line.Info[] targetLineInfoArray = mixer.getTargetLineInfo();
                for (Line.Info targetLineInfo : targetLineInfoArray) {
                    if (targetLineInfo instanceof DataLine.Info) {
                        DataLine.Info dataLineInfo = (DataLine.Info) targetLineInfo;

                        AudioFormat format = new AudioFormat(sampleRate, 8, 1, true, true);

                        if (dataLineInfo.isFormatSupported(format)) {
                            TargetDataLine line = (TargetDataLine) mixer.getLine(dataLineInfo);
                            line.open(format);
                            return line;
                        }
                    }
                }
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        } else{
            System.err.println("Mixer not found: " + mixerName);
        }

        return null;
    }

    /**
     * Return a line that's appropriate for playing sound to a loudspeaker.
     */
    public static SourceDataLine obtainAudioOutput(String mixerName, int sampleRate) {

        Mixer.Info info=getMixerInfo(mixerName);
        SourceDataLine line=AudioSystem.getSourceDataLine(sampleRate,info);
        return line;
    }

    public static void main(String[] args) {
        TargetDataLine line = obtainAudioInput("USB Audio Device",8000);
    }
}