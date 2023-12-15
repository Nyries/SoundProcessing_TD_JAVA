package ui;

import audio.AudioSignal;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.animation.AnimationTimer;

public class SignalView extends LineChart<Number, Number> {

    private XYChart.Series<Number, Number> series;
    private AudioSignal audiosignal;

    public SignalView(AudioSignal audiosignal) {
        super(new NumberAxis(), new NumberAxis());
        this.audiosignal = audiosignal;
        this.series = new XYChart.Series<>();

        this.setTitle("Audio Signal");
        this.getXAxis().setLabel("Sample");
        this.getXAxis().setLabel("Amplitude");
        this.getData().add(series);

        AnimationTimer animationTimer= new AnimationTimer(){
            @Override
            public void handle(long now){
                updateData();
            }
        };
            animationTimer.start();
    }

    public void setAudiosignal(AudioSignal audiosignal) {
        this.audiosignal = audiosignal;
    }

    public void updateData() {
        // Clear previous data
        series.getData().clear();

        // Populate the series with data points
        for (int i = 0; i < audiosignal.getFrameSize(); i++) {
            series.getData().add(new XYChart.Data<>(i, audiosignal.getSample(i)));
        }
    }
}
