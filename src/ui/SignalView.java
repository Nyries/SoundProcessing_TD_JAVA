package ui;

import audio.AudioSignal;
import javafx.animation.AnimationTimer;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class SignalView extends LineChart<Number, Number> {
    private XYChart.Series<Number, Number> series;

    public SignalView() {
        super(new NumberAxis(),new NumberAxis());

        setTitle("Graphique du Signal Audio");
        getXAxis().setLabel("Temps");
        getYAxis().setLabel("Amplitude");

        series = new XYChart.Series<>();
        getData().add(series);
    }
    public void addDataPoint(double x, double y){
        series.getData().add(new XYChart.Data<>(x,y));
    }
}
