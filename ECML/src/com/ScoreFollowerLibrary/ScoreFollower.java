package com.ScoreFollowerLibrary;

import com.ScoreFollowerLibrary.nl.metaphoric.scorefollower.lib.AnalyzeListener;
import com.ScoreFollowerLibrary.nl.metaphoric.scorefollower.lib.AudioAnalyzer;
import com.ScoreFollowerLibrary.nl.metaphoric.scorefollower.lib.Parameters;
import com.ScoreFollowerLibrary.nl.metaphoric.scorefollower.lib.feature.FrameVector;
import com.ScoreFollowerLibrary.nl.metaphoric.scorefollower.lib.matcher.PositionMatcher;
import com.ecml.MicrophoneReader;
import com.ecml.MidiTrack;

/**
 * Created by Jonathan Roux on 27/08/2015.
 *
 * Interface between the ScoreFollower library and ECML
 */
public class ScoreFollower {

    /**
     * Analyzer of the audio which creates FrameVector
     */
    private AudioAnalyzer analyzer;

    /**
     * The matcher for the position in the score
     */
    private PositionMatcher matcher;

    /**
     * The microphoneReader which reads data that you play
     */
    private MicrophoneReader micro;

    /**
     * ScoreFollower constructor
     * @param listener The listener which will use the new data
     * @param track The midi track file
     */
    public ScoreFollower(AnalyzeListener listener, MidiTrack track) {
        this.analyzer = new AudioAnalyzer(listener, Parameters.sampleRate, Parameters.windowSize, Parameters.hopSize);
        this.matcher = new PositionMatcher(track.getChromaVectors(), Parameters.windowSize, Parameters.hopSize);
    }

    /**
     * Restart the position matcher at the position 0
     * Call it right after you click on play
     */
    public void restartMatcher() {
        this.matcher.restart();
    }

    /**
     * Restart the position matcher at the position
     * @param position Position where return
     */
    public void restartMatcher(int position) {
        this.matcher.restart(position);
    }

    /**
     * Start the recording of the audio
     * Call it right after you restarted the matcher
     */
    public void startRecord() {
        this.micro = new MicrophoneReader(this.analyzer);
        this.micro.startRecorder();
    }

    /**
     * Get the right position on the score of the audio that you record
     * Call it when you want to know where you are on the score
     * @param v The Framevector to test
     * @return The position of this note
     */
    public int getPosition(FrameVector v) {
        return this.matcher.getPosition(v);
    }

    /**
     * To know if the matcher plays
     * @return True or false
     */
    public boolean isPlaying() {
        return this.matcher.isPlaying();
    }
}
