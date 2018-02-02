package model;

import ddf.minim.analysis.BeatDetect;
import ddf.minim.analysis.FFT;

import java.util.Observable;

public class MP3Player extends Observable {

    private final SimpleMinimWithMix minim;
    private SimpleAudioPlayerWithMix audioPlayer;
    private Thread thread;
    private FFT fft;
    private BeatDetect beat;

    public MP3Player() {
        minim = new SimpleMinimWithMix(true);
    }

    public void play(String fileName) {
        stop();
        audioPlayer = minim.loadMP3File(fileName);
        audioPlayer.play();
        fft = new FFT(audioPlayer.bufferSize(), audioPlayer.sampleRate());
        beat = new BeatDetect();
        thread = new FFTThread();
        thread.start();
    }

    public void stop() {
        if (audioPlayer != null && thread != null) {
            thread.interrupt();
            minim.stop();
        }
    }

    public void setPosition(double percent) {
        if (audioPlayer != null) {
            int pos = (int) (audioPlayer.length() * percent);
            audioPlayer.cue(pos);
        }
    }

    private class FFTThread extends Thread {
        int buffSize = fft.specSize() - fft.specSize() / 2;
        float bands[] = new float[buffSize];

        @Override
        public void run() {

            while (!isInterrupted()) {

                fft.forward(audioPlayer.getMix());
                beat.detect(audioPlayer.getMix());
                for (int i = 0; i < buffSize; i++) {
                    bands[i] = fft.getBand(i);
                }

                if (beat.isOnset()) {
                    setChanged();
                    notifyObservers(true);
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
}
