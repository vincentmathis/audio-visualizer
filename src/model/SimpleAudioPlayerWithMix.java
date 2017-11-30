package model;

import javax.sound.sampled.AudioFormat;

import ddf.minim.AudioBuffer;
import ddf.minim.AudioListener;
import ddf.minim.AudioMetaData;
import ddf.minim.AudioPlayer;

public class SimpleAudioPlayerWithMix {
    private AudioPlayer player;
    private boolean threading;

    public SimpleAudioPlayerWithMix (AudioPlayer player, boolean threading){
        this.player = player;
        this.threading = threading;
    }

    AudioBuffer getMix() {
        return player.mix;
    }

    /**
     * Add an AudioListener to this sound generating object, which will have its samples method called every time this object generates a new buffer of samples.
     *
     * @param listener AudioListener that will listen to this.
     */
    public void addListener(AudioListener listener){
        player.addListener(listener);
    }

    /**
     * The internal buffer size of this sound object.
     * The left, right, and mix AudioBuffers of this object will be this large, and sample buffers passed to AudioListeners added to this object will be this large.
     *
     * @return internal buffer size of this sound object, in sample frames.
     */
    public int bufferSize(){
        return player.bufferSize();
    }

    /**
     * Sets the position to millis milliseconds from the beginning.
     * This will not change the play state. If an error occurs while trying to cue, the position will not change.
     * If you try to cue to a negative position or to a position that is greater than length(), the amount will be clamped to zero or length().
     *
     * @param millis millisecond position to place the "playhead".
     */
    public void cue(int millis){
        player.cue(millis);
    }

    /**
     * Returns the current balance. This will be in the range [-1, 1].
     * Usually balance will only be available for stereo audio sources, because it describes how much attenuation should be applied to the left and right channels.
     * If a balance control is not available, this will do nothing.
     *
     * @return current balance or zero if a balance control is unavailable
     */
    public float getBalance() {
        return player.getBalance();
    }

    /**
     * Returns an AudioFormat object that describes the audio properties of this sound generating object.
     * This is often useful information when doing sound analysis or some synthesis, but typically you will not need to know about the specific format.
     *
     * @return AudioFormat describing this sound object.
     */
    public AudioFormat getFormat(){
        return player.getFormat();
    }

    /**
     * Returns the current gain. If a gain control is not available, this returns 0.
     *
     * Note that the gain is not the same thing as the level() of an AudioBuffer!
     * Gain describes the current volume of the sound in decibels, which is a logarithmic, rather than linear, scale. A gain of 0dB means the sound is not being amplified or attenuated.
     * Negative gain values will reduce the volume of the sound, and positive values will increase it.
     *
     * @return Current gain or zero if a gain control is unavailable. The gain is expressed in decibels.
     */
    public float getGain(){
        return player.getGain();
    }

    /**
     * Returns the meta data for the recording being played by this player.
     *
     * @return Meta data for this player's recording.
     */
    public AudioMetaData getMetaData(){
        return player.getMetaData();
    }

    /**
     * Returns the current pan. Usually pan will be only be available on mono audio sources because it describes a mono signal's position in a stereo field.
     * This will be in the range [-1, 1], where -1 will place the sound only in the left speaker and 1 will place the sound only in the right speaker.
     *
     * @return Current pan or zero if a pan control is unavailable.
     */
    public float getPan(){
        return player.getPan();
    }

    /**
     * Returns the current volume. If a volume control is not available, this returns 0. Note that the volume is not the same thing as the level() of an AudioBuffer!
     *
     * @return Current volume or zero if a volume control is unavailable.
     */
    public float getVolume(){
        return player.getVolume();
    }

    /**
     * Returns true if the AudioPlayer is currently playing and has more than one loop left to play.
     *
     * @return <code>true</code> if this is looping, <code>false</code> if not
     */
    public boolean isLooping(){
        return player.isLooping();
    }

    /**
     * Returns true if the sound is muted.
     *
     * @return Current mute state.
     */
    public boolean isMuted() {
        return player.isMuted();
    }

    /**
     * Indicates if the AudioPlayer is currently playing.
     *
     * @return <code>true</code> if this is currently playing, <code>false</code> if not
     */
    public boolean isPlaying(){
        return player.isPlaying();
    }

    /**
     * Returns the length of the sound in milliseconds. If for any reason the length could not be determined, this will return -1.
     * However, an unknown length should not impact playback.
     *
     * @return Length of the sound in milliseconds.
     */
    public int length(){
        return player.length();
    }

    /**
     * Set the AudioPlayer to loop some number of times.
     * If it is already playing, the position will not be reset to the beginning. If it is not playing, it will start playing.
     *
     * If you previously called this method and then paused the AudioPlayer, you can resume looping by using the result of getLoopCount() as the argument for this method.
     * To loop indefinitely, use loop().
     *
     * @param num Number of times to loop
     */
    public void loop(int num){
        player.loop(num);
    }

    /**
     * Set the AudioPlayer to loop indefinitely.
     */
    public void loop(){
        player.loop();
    }

    /**
     * Returns the number of loops left to do.
     *
     * @return Number of loops left.
     */
    public int loopCount() {
        return player.loopCount();
    }

    /**
     * Mutes the sound.
     */
    public void mute(){
        player.mute();
    }

    /**
     * Pauses playback.
     */
    public void pause(){
        player.pause();
    }

    /**
     * Starts playback from the current position. If this was previously set to loop, looping will be disabled.
     */
    public void play() {
        play(0);
    }

    /**
     * Starts playback from the current position. If this was previously set to loop, looping will be disabled.
     *
     * @param millis Milliseconds from the beginning of the file to begin playback from
     */
    public void play(int millis){

        player.play(millis);
        while (!threading && player.isPlaying()){
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Returns the current position of the "playhead" in milliseconds (ie how much of the sound has already been played).
     *
     * @return Current position of the "playhead" in milliseconds
     */
    public int position(){
        return player.position();
    }

    /**
     * Removes an AudioListener that was previously added to this sound object.
     *
     * @param listener AudioListener that should stop listening to this
     */
    public void removeListener(AudioListener listener){
        player.removeListener(listener);
    }

    /**
     * Rewinds to the beginning. This does not stop playback.
     */
    public void rewind(){
        player.rewind();
    }

    /**
     * Returns the sample rate of this sound object.
     *
     * @return Sample rate of this sound object.
     */
    public float sampleRate(){
        return player.sampleRate();
    }

    /**
     * Sets the balance. The value should be in the range [-1, 1].
     * If a balance control is not available, this will do nothing.
     *
     * @param value New value for the balance
     */
    public void setBalance(float value){
        player.setBalance(value);
    }

    /**
     * Sets the gain. If a gain control is not available, this does nothing.
     *
     * @param value New value for the gain, expressed in decibels.
     */
    public void setGain(float value){
        player.setGain(value);
    }

    /**
     * Sets the loop points used when looping.
     *
     * @param start start of the loop in milliseconds
     * @param stop end of the loop in milliseconds
     */
    public void setLoopPoints(int start, int stop){
        player.setLoopPoints(start, stop);
    }

    /**
     * Sets the pan. The provided value should be in the range [-1, 1].
     * If a pan control is not present, this does nothing.
     *
     * @param value new value for the pan
     */
    public void setPan(float value){
        player.setPan(value);
    }

    /**
     * Sets the volume.
     * If a volume control is not available, this does nothing.
     *
     * @param value new value for the volume, usually in the range [0,1].
     */
    public void setVolume(float value){
        player.setVolume(value);
    }

    /**
     * Transitions the balance from one value to another.
     *
     * @param from starting balance
     * @param to ending balance
     * @param millis length of the transition in milliseconds
     */
    public void shiftBalance(float from, float to, int millis){
        player.shiftBalance(from, to, millis);
    }

    /**
     * Transitions the gain from one value to another.
     *
     * @param from starting gain
     * @param to ending gain
     * @param millis length of the transition in milliseconds
     */
    public void shiftGain(float from, float to, int millis){
        player.shiftGain(from, to, millis);
    }

    /**
     * Transitions the pan from one value to another.
     *
     * @param from starting pan
     * @param to ending pan
     * @param millis length of the transition in milliseconds
     */
    public void shiftPan(float from, float to, int millis){
        player.shiftPan(from, to, millis);
    }

    /**
     * Transitions the volume from one value to another.
     *
     * @param from starting volume
     * @param to ending volume
     * @param millis length of the transition in milliseconds
     */
    public void shiftVolume(float from, float to, int millis){
        player.shiftVolume(from, to, millis);
    }

    /**
     * Skips millis milliseconds from the current position. millis can be negative, which will make this skip backwards.
     * If the skip amount would result in a negative position or a position that is greater than length(), the new position will be clamped to zero or length().
     *
     * @param millis how many milliseconds to skip, sign indicates direction
     */
    public void skip(int millis){
        player.skip(millis);
    }

    /**
     * The type is an int describing the number of channels this sound object has.
     *
     * @return <code>Minim.MONO</code> if this is mono, <code>Minim.STEREO</code> if this is stereo
     */
    public int type(){
        return player.type();
    }

    /**
     * Unmutes the sound.
     */
    public void unmute(){
        player.unmute();
    }

}
