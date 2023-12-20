package ui;

import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class VuMeter extends ProgressBar {

    private Rectangle progressIndicator;
    final int height=50;
    public VuMeter() {
        super();
        setPrefHeight(30);
        setPrefWidth(5);

        progressIndicator = new Rectangle(5, getPrefHeight(), Color.GREEN);
        // Add the progress indicator to the progress bar
        getChildren().add(progressIndicator);
        // Bind the progress of the progress bar to its height
        this.progressProperty().addListener((obs, oldValue, newValue) -> {
            double height = getPrefHeight() * newValue.doubleValue();
            progressIndicator.setHeight(height);
        });
        setMinWidth(550);
        setRotate(-90);
    }

    public void setVolume(double volume) {
        setProgress(volume);
        updateProgressIndicatorColor(volume);
    }

    private void updateProgressIndicatorColor(double volume) {
        if (volume > 0.75) {
            this.setStyle("-fx-accent: red");
        } else if (volume >0.5) {
            this.setStyle("-fx-accent: yellow");
        } else {
            this.setStyle("-fx-accent: green");
        }
    }
}
