package com.floreantpos.isdnmonitor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class VibrantDialog extends Object {

    public static final int SHAKE_DISTANCE = 10;
    public static final double SHAKE_CYCLE = 50;
    public static final int SHAKE_DURATION = 1000;
    public static final int SHAKE_UPDATE = 5;

    private JDialog dialog;
    private Point naturalLocation;
    private long startTime;
    private Timer shakeTimer;
    private final double TWO_PI = Math.PI * 2.0;

    public VibrantDialog (JDialog d) {
        dialog = d;
    }

    public void startShake() {
        naturalLocation = dialog.getLocation();
        startTime = System.currentTimeMillis();
        shakeTimer = new Timer(SHAKE_UPDATE,
                new ActionListener( ) {
    @Override
            public void actionPerformed (ActionEvent e) { 
                // calculate elapsed time               
                long elapsed = System.currentTimeMillis()-
                  startTime; 
                // use sin to calculate an x-offset 
                double waveOffset = (elapsed % SHAKE_CYCLE) /
                    SHAKE_CYCLE; 
                double angle = waveOffset * TWO_PI;

                // offset the x-location by an amount 
                // proportional to the sine, up to 
                // shake_distance 
                int shakenX = (int) ((Math.sin (angle) *
                                      SHAKE_DISTANCE) +
                                     naturalLocation.x); 
               
                dialog.setLocation (shakenX, naturalLocation.y);               
                dialog.repaint( );

                // should we stop timer? 
                if (elapsed >= SHAKE_DURATION)
                 stopShake();
            }
        }
       );
       shakeTimer.start( );

}

public void stopShake( ) {
    shakeTimer.stop( );
    dialog.setLocation (naturalLocation);
    dialog.repaint( );
}

}
