package ui;

import audio.AudioSignal;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class VuMeter extends Canvas {
    private AudioSignal audioSignal;

    public VuMeter(AudioSignal audioSignal){
        this.audioSignal = audioSignal;
        setWidth(30);
        setHeight(100);
        update();
    }

    public void update(){
        GraphicsContext gc = getGraphicsContext2D();
        double signalLevel = audioSignal.getdBlevel();

        Color color;
        if (signalLevel>-20) {
            color=Color.GREEN;
        } else if(signalLevel >-40){
            color=Color.ORANGE;
        } else {
            color=Color.RED;
        }

        gc.clearRect(0,0,getWidth(),getHeight());
        gc.setFill(color);
        gc.fillRect(0,getHeight()*(1-signalLevel/60.0),getWidth(),getHeight());
    }
}
