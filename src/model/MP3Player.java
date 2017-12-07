package model;

import ddf.minim.analysis.FFT;
import java.util.Observable;

public class MP3Player extends Observable {

    private int steps = 1;
    private final SimpleMinimWithMix minim;
    private SimpleAudioPlayerWithMix audioPlayer;
    private Thread thread;
    private FFT fft;
    private boolean slowShrink = false;

    public MP3Player() {
        minim = new SimpleMinimWithMix(true);
    }

    private class FFTThread extends Thread {
        float bands[] = new float[fft.specSize()];

        @Override
        public void run() {

            while (!isInterrupted()) {

                fft.forward(audioPlayer.getMix());
                for (int i = 0; i < fft.specSize(); i += steps) {
                    if(slowShrink) {
                        if(fft.getBand(i) > bands[i]) {
                            bands[i] = fft.getBand(i);
                        } else {
                            bands[i] -= 2;
                        }
                    } else {
                        bands[i] = fft.getBand(i);
                    }
                }
                setChanged();
                notifyObservers(bands);
                setChanged();
                notifyObservers(audioPlayer.position() * 1.0 / audioPlayer.length());
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
        }
    }

    public void play(String fileName) {
        if (audioPlayer != null && thread != null) {
            thread.interrupt();
            minim.stop();
        }
        audioPlayer = minim.loadMP3File(fileName);
        audioPlayer.play();
        fft = new FFT(audioPlayer.bufferSize(), audioPlayer.sampleRate());
        thread = new FFTThread();
        thread.start();
    }

    public void setSlowShrink(boolean slowShrink) {
        this.slowShrink = slowShrink;
    }

    public void setSteps(int steps) {
        if(steps == 0) {
            this.steps = 1;
        } else {
            this.steps = steps;
        }
    }

    public void setPosition(double percent) {
        if(audioPlayer != null) {
            int pos = (int) (audioPlayer.length() * percent);
            audioPlayer.cue(pos);
        }
    }
}
