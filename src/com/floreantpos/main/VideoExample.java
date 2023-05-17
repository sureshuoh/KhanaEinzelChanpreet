package com.floreantpos.main;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
public class VideoExample {

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public static void main(final String[] args) {
     NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Programme\\VideoLAN\\VLC");
     Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VideoExample(args);
            }
        });
    }

    private VideoExample(String[] args) {
        JFrame frame = new JFrame("vlcj Tutorial");

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        frame.setContentPane(mediaPlayerComponent);

        frame.setLocation(100, 100);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        mediaPlayerComponent.getMediaPlayer().playMedia("C:\\floreantpos\\Video\\kasse.mp4");
        
    }
}