package ui;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import audio.AudioSignal;
public class Spectrogram extends LineChart<Number, Number> {
    private AudioSignal audioSignal;

    public Spectrogram(AudioSignal audioSignal) {
        super(new NumberAxis(), new NumberAxis());
        this.audioSignal = audioSignal;

        // Customize the chart appearance as needed
        setTitle("Spectrum Visualization");
        getXAxis().setLabel("Frequency");
        getYAxis().setLabel("Amplitude");

        // Add series to the chart
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        getData().add(series);

        // Use AnimationTimer to update the chart periodically
        javafx.animation.AnimationTimer animationTimer = new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                updateData();
            }
        };
        animationTimer.start();
    }

    private void updateData() {
        // Implement the logic to compute FFT and update the spectrum chart
        // ...

        // Example: Clear the existing data and add new data points
        XYChart.Series<Number, Number> series = getData().get(0);
        series.getData().clear();
        for (int i = 0; i < audioSignal.getFrameSize(); i++) {
            series.getData().add(new XYChart.Data<>(i, /* computed spectrum value */));
        }
    }
}
