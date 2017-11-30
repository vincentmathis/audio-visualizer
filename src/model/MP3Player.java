package model;

import ddf.minim.analysis.FFT;
import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;

import java.util.Observable;

public class MP3Player extends Observable {

    private SimpleMinim minim;
    private SimpleAudioPlayer audioPlayer;
    private FFT fft;
    private FFTThread thread;

    class FFTThread extends Thread {

        @Override
        public void run() {
            while (!isInterrupted()) {
                //fft.forward(audioPlayer.mix);
                float bands[] = new float[fft.specSize()];
                for (int i = 0; i < fft.specSize(); i++) {
                    bands[i] = fft.getBand(i);
                }
                setChanged();
                notifyObservers(bands);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
        }
    }

    public MP3Player() {
        minim = new SimpleMinim(true);
    }

    public void play(String fileName) {
        if (audioPlayer != null && thread != null) {
            thread.interrupt();
            minim.stop();
        }
        minim.stop();
        audioPlayer = minim.loadMP3File(fileName);
        audioPlayer.play();
        //fft = new FFT(audioPlayer.bufferSize(), audioPlayer.sampleRate());
        //thread = new FFTThread();
        //thread.run();
    }

}
