package ui;

import audio.AudioSignal;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class SignalView extends LineChart<Number, Number> {

    private XYChart.Series<Number, Number> series;
    private AudioSignal audiosignal;

    public SignalView(NumberAxis x, NumberAxis y) {
        super(x, y);
        series = new XYChart.Series<>();
        getData().add(series);
    }

    public void setAudiosignal(AudioSignal audiosignal) {
        this.audiosignal = audiosignal;
    }

    public void updateData() {
        if (audiosignal == null) {
            // Handle the case when audioSignal is not set
            return;
        }

        // Clear previous data
        series.getData().clear();

        // Get the audio signal data
        double[] signalData = audiosignal.getSignalData();

        // Populate the series with data points
        for (int i = 0; i < signalData.length; i++) {
            series.getData().add(new XYChart.Data<>(i, signalData[i]));
        }
    }
}
