package model;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import de.hsrm.mi.eibo.simpleplayer.MinimHelper;
import model.SimpleAudioPlayerWithMix;

public class SimpleMinimWithMix extends Minim{
    private boolean threading;

    public SimpleMinimWithMix() {
        this(false);
    }

    public SimpleMinimWithMix(boolean threading) {
        super(new MinimHelper());
        this.threading = threading;
    }

    public SimpleAudioPlayerWithMix loadMP3File(String filename){
        return loadMP3File(filename, 1024);
    }

    public SimpleAudioPlayerWithMix loadMP3File(String filename, int bufferSize){
        AudioPlayer player = super.loadFile(filename, bufferSize);
        return new SimpleAudioPlayerWithMix(player, threading);
    }
}
