package model;

import ddf.minim.analysis.BeatDetect;
import ddf.minim.analysis.FFT;

import java.util.Observable;

public class MP3Player extends Observable {

    private final SimpleMinimWithMix minim;
    private SimpleAudioPlayerWithMix audioPlayer;
    private Thread thread;
    private FFT fft;
    private BeatDetect beatDetect;

    private float bands[];
    private boolean beat = false;
    private double progess;

    public MP3Player() {
        minim = new SimpleMinimWithMix(true);
    }

    public void play(String fileName) {
        stop();
        audioPlayer = minim.loadMP3File(fileName);
        audioPlayer.play();

        fft = new FFT(audioPlayer.bufferSize(), audioPlayer.sampleRate());
        bands = new float[fft.specSize() - fft.specSize() / 2];
        beatDetect = new BeatDetect();
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

        @Override
        public void run() {

            while (!isInterrupted()) {

                fft.forward(audioPlayer.getMix());
                for (int i = 0; i < bands.length; i++) bands[i] = fft.getBand(i);

                beatDetect.detect(audioPlayer.getMix());
                beat = beatDetect.isOnset();

                progess = audioPlayer.position() * 1.0 / audioPlayer.length();

                setChanged();
                notifyObservers();

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
        }
    }

    public float[] getBands() {
        return bands;
    }

    public boolean isBeat() {
        return beat;
    }

    public double getProgess() {
        return progess;
    }
}
