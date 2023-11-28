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
}
