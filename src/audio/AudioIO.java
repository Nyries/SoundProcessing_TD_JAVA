/** A collection of static utilities related to the audio system. */
package audio;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.*;
import javax.sound.sampled.Mixer.Info;
import java.util.Arrays;

import java.util.List;
import java.util.stream.Collectors;

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
                .filter(e -> e.getName().equalsIgnoreCase(mixerName)).findFirst().orElse(null);
    }

    /**
     * Return a line that's appropriate for recording sound from a microphone.
     * Example of use:
     * TargetDataLine line = obtainInputLine("USB Audio Device", 8000);
     *
     * @param mixerName a string that matches one of the available mixers
     /* @see AudioSystem.getMixerInfo() which provides a list of all mixers on your system.
     */
    public static TargetDataLine obtainAudioInput(String mixerName, int sampleRate){
        Mixer.Info mixerInfo=getMixerInfo(mixerName);
        if (mixerInfo!=null){
             try {
                Mixer mixer = AudioSystem.getMixer(mixerInfo);
                AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, 16, 1, 2, sampleRate, false);
                DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
                Line.Info[] targetLineInfoArray = mixer.getTargetLineInfo();
                if (mixer.isLineSupported(dataLineInfo));
                return (TargetDataLine) mixer.getLine(dataLineInfo);
             } catch (LineUnavailableException e) {
                 e.printStackTrace();
             }
        }
        return null;
    }

    /**
     * Return a line that's appropriate for playing sound to a loudspeaker.
     */
    public static SourceDataLine obtainAudioOutput(String mixerName, int sampleRate) {
        Mixer.Info mixerInfo = getMixerInfo(mixerName);
        if (mixerInfo != null) {
            try{
                Mixer mixer = AudioSystem.getMixer(mixerInfo);
                AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, 16, 1, 2, sampleRate, false);
                DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
                if (mixer.isLineSupported(dataLineInfo)) {
                    return (SourceDataLine) mixer.getLine(dataLineInfo);
                }
            } catch (LineUnavailableException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /** Get a list of available input devices. */
    public static List<String> getAvailableInputDevices() {
        return Arrays.stream(AudioSystem.getMixerInfo())
                .map(AudioSystem::getMixer)
                .filter(mixer -> hasTargetDataLine(mixer))
                .map(Mixer::getMixerInfo)
                .map(Mixer.Info::getName)
                .collect(Collectors.toList());
    }

    /** Get a list of available output devices. */
    public static List<String> getAvailableOutputDevices() {
        return Arrays.stream(AudioSystem.getMixerInfo())
                .map(AudioSystem::getMixer)
                .filter(mixer -> hasSourceDataLine(mixer))
                .map(Mixer::getMixerInfo)
                .map(Mixer.Info::getName)
                .collect(Collectors.toList());
    }
    private static boolean hasTargetDataLine(Mixer mixer) {
        Line.Info[] targetDataLineInfo = mixer.getTargetLineInfo(new Line.Info(TargetDataLine.class));
        return targetDataLineInfo.length > 0;
    }

    private static boolean hasSourceDataLine(Mixer mixer) {
        Line.Info[] sourceDataLineInfo = mixer.getSourceLineInfo(new Line.Info(SourceDataLine.class));
        return sourceDataLineInfo.length > 0;
    }
        /** public static void main(String[] args) {
             printAudioMixers();

             try {
                 TargetDataLine audioInputLine = obtainAudioInput("Microphone Mixer",44100);
                 if (audioInputLine!=null){
                     System.out.println("Audio input line obtained successfully");
                 } else {
                     System.out.println("Failed to obtain audio input line");
                 }
             } catch (LineUnavailableException e){
                 e.printStackTrace();
             }
             try {
                 SourceDataLine audioOutputLine = obtainAudioOutput("USB Audio Device",8000);
                 if (audioOutputLine!= null){
                     System.out.println("Audio output obtained successfully");
                 } else{
                     System.out.println("Failed to obtain audio output line");
                 }
             } catch(LineUnavailableException e){
                 e.printStackTrace();
             }
         }*/

}