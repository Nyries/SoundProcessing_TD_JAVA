package ui;

import audio.AudioSignal;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.TilePane;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;
import javafx.scene.control.*;
import sun.misc.Signal;

public class Main extends Application {

    private AudioSignal audioSignal;
    private Button recordButton;
    private Button playButton;
    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            audioSignal = new AudioSignal();
            root.setTop(createToolbar());
            root.setLeft(createVuMeter());
            root.setCenter(createSignalView());
            Scene scene = new Scene(root,1500,600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("The JavaFX audio processor");
            primaryStage.setOnCloseRequest(event -> {System.exit(0);});
            primaryStage.show();

            } catch(Exception e) {e.printStackTrace();}
        }
        private Node createToolbar(){
            recordButton = new Button("Enregistrer");
            recordButton.setOnAction(event->toggleRecording());

            playButton = new Button("Lire");
            playButton.setOnAction(event ->playRecordedAudio());
            ToolBar tb = new ToolBar(recordButton,playButton);
            return tb;
        }
        private Node createVuMeter(){
            Group groupVuMeter = new Group();
            audioSignal.startCapture();
            groupVuMeter.getChildren().add(audioSignal.getVuMeter());
            return groupVuMeter;
        }
        private Node createSignalView(){
            Group groupSignalView = new Group();
            audioSignal.startCapture();
            groupSignalView.getChildren().add(audioSignal.getSignalView());
            return groupSignalView;
        }
    private void toggleRecording(){
        audioSignal.toggleRecording();

        if (audioSignal.isRecording()){
            recordButton.setText("Arrêter l'enregistrement");
            playButton.setDisable(true);
        } else {
            recordButton.setText("Enregistrer");
            playButton.setDisable(false);
        }
    }
    private void playRecordedAudio() {
        byte[] recordedAudio = audioSignal.getRecordedAudio();
        audioSignal.playAudio(recordedAudio);
    }
}
