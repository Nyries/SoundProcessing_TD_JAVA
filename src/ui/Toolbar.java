package ui;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import audio.AudioIO;
public class Toolbar extends HBox {
    private AudioIO audioIO;

    public Toolbar(AudioIO audioIO) {
        this.audioIO = audioIO;

        // Create buttons and ComboBoxes
        Button startButton = new Button("Start");
        Button stopButton = new Button("Stop");
        ComboBox<String> inputComboBox = new ComboBox<>();
        ComboBox<String> outputComboBox = new ComboBox<>();

        // Fill ComboBoxes with audio input and output options
        inputComboBox.getItems().addAll(AudioIO.getAvailableInputDevices());
        outputComboBox.getItems().addAll(AudioIO.getAvailableOutputDevices());

        // Set event handlers
        startButton.setOnAction(event -> startAudioProcessing());
        stopButton.setOnAction(event -> stopAudioProcessing());

        // Add components to the toolbar
        getChildren().addAll(startButton, stopButton, inputComboBox, outputComboBox);
    }

    private void startAudioProcessing() {
        // Call resource-intensive code in a separate thread
        Thread audioProcessingThread = new Thread(() ->
                audioIO.startAudioProcessing(
                        inputComboBox.getValue(),
                        outputComboBox.getValue(),
                        /* other parameters */));
        audioProcessingThread.start();
    }

    private void stopAudioProcessing() {
        // Call resource-intensive code in a separate thread
        Thread stopThread = new Thread(audioIO::stopAudioProcessing);
        stopThread.start();
    }
}
