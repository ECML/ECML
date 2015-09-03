/*
 * Copyright (c) 2007-2011 Madhav Vaidyanathan
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 */



package com.ecml;

import android.util.Log;

import com.ScoreFollowerLibrary.nl.metaphoric.scorefollower.lib.feature.FrameVector;
import com.ScoreFollowerLibrary.nl.metaphoric.scorefollower.lib.feature.FrameVectorFactory;

import java.util.Comparator;
import java.util.HashMap;


/** @class MidiNote
 * <br>
 * A MidiNote contains
 *
 * starttime - The time (measured in pulses) when the note is pressed.
 * channel   - The channel the note is from.  This is used when matching
 *             NoteOff events with the corresponding NoteOn event.
 *             The channels for the NoteOn and NoteOff events must be
 *             the same.
 * notenumber - The note number, from 0 to 127.  Middle C is 60.
 * duration  - The time duration (measured in pulses) after which the 
 *             note is released.
 *
 * A MidiNote is created when we encounter a NoteOff event.  The duration
 * is initially unknown (set to 0).  When the corresponding NoteOff event
 * is found, the duration is set by the method NoteOff().
 */
public class MidiNote implements Comparator<MidiNote> {
    private int starttime;   /* The start time, in pulses */
    private int channel;     /* The channel */
    private int notenumber;  /* The note, from 0 to 127. Middle C is 60 */
    private int duration ;   /* The duration, in pulses */
    private FrameVector chroma; /* The chroma vector of the note */
    private final static HashMap<String, FrameVector> noteChroma = new HashMap<String, FrameVector>();

    // FRAMEVECTOR for each note (constance)


    /** Create a new MidiNote.  This is called when a NoteOn event is
     * encountered in the MidiFile.
     */
	public MidiNote(int starttime, int channel, int notenumber, int duration) {
		this.starttime = starttime;
		this.channel = channel;
		this.notenumber = notenumber;
		this.duration = duration;

        if (noteChroma.isEmpty()) {
            Log.d("Midinote", "Verification");
            initialize();
        }
        // For each note, define the chroma vector associate
        switch ((this.notenumber + 3) % 12) {
            case 0:
                this.chroma = noteChroma.get("A");
                break;
            case 1:
                this.chroma = noteChroma.get("A#");
                break;
            case 2:
                this.chroma = noteChroma.get("B");
                break;
            case 3:
                this.chroma = noteChroma.get("C");
                break;
            case 4:
                this.chroma = noteChroma.get("C#");
                break;
            case 5:
                this.chroma = noteChroma.get("D");
                break;
            case 6:
                this.chroma = noteChroma.get("D#");
                break;
            case 7:
                this.chroma = noteChroma.get("E");
                break;
            case 8:
                this.chroma = noteChroma.get("F");
                break;
            case 9:
                this.chroma = noteChroma.get("F#");
                break;
            case 10:
                this.chroma = noteChroma.get("G");
                break;
            case 11:
                this.chroma = noteChroma.get("G#");
                break;
            default:
                break;
        }
	}

    private void initialize() {
        // First try (not perfect chroma)
        /*noteChroma.put("A", FrameVectorFactory.getVector(new double[] {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
        noteChroma.put("A#", FrameVectorFactory.getVector(new double[] {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
        noteChroma.put("B", FrameVectorFactory.getVector(new double[] {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
        noteChroma.put("C", FrameVectorFactory.getVector(new double[] {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}));
        noteChroma.put("C#", FrameVectorFactory.getVector(new double[] {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0}));
        noteChroma.put("D", FrameVectorFactory.getVector(new double[] {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0}));
        noteChroma.put("D#", FrameVectorFactory.getVector(new double[] {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0}));
        noteChroma.put("E", FrameVectorFactory.getVector(new double[] {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0}));
        noteChroma.put("F", FrameVectorFactory.getVector(new double[] {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0}));
        noteChroma.put("F#", FrameVectorFactory.getVector(new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0}));
        noteChroma.put("G", FrameVectorFactory.getVector(new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0}));
        noteChroma.put("G#", FrameVectorFactory.getVector(new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}));*/

        // For trumpet
        noteChroma.put("A", FrameVectorFactory.getVector(new double[] {0.9, 0, 0.1, 0.3, 0.1, 0.3, 0.5, 0, 0.2, 0.6, 0, 0.1}));
        noteChroma.put("A#", FrameVectorFactory.getVector(new double[] {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
        noteChroma.put("B", FrameVectorFactory.getVector(new double[] {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
        noteChroma.put("C", FrameVectorFactory.getVector(new double[] {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}));
        noteChroma.put("C#", FrameVectorFactory.getVector(new double[] {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0}));
        noteChroma.put("D", FrameVectorFactory.getVector(new double[] {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0}));
        noteChroma.put("D#", FrameVectorFactory.getVector(new double[] {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0}));
        noteChroma.put("E", FrameVectorFactory.getVector(new double[] {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0}));
        noteChroma.put("F", FrameVectorFactory.getVector(new double[] {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0}));
        noteChroma.put("F#", FrameVectorFactory.getVector(new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0}));
        noteChroma.put("G", FrameVectorFactory.getVector(new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0}));
        noteChroma.put("G#", FrameVectorFactory.getVector(new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}));
    }

    public int getStartTime() {
		return starttime;
	}

	public void setStartTime(int value) {
		starttime = value;
	}

	public int getEndTime() {
		return starttime + duration;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int value) {
		channel = value;
	}

	public int getNumber() {
		return notenumber;
	}

	public void setNumber(int value) {
		notenumber = value;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int value) {
		duration = value;
	}

    /**
     * Get the chroma vector from the note
     * @return The chroma vector
     */
    public FrameVector getChromaVector() { return this.chroma; }

	/** A NoteOff event occurs for this note at the given time.
	 * Calculate the note duration based on the noteoff event.
	 */
	public void NoteOff(int endtime) {
		duration = endtime - starttime;
	}

	/** Compare two MidiNotes based on their start times.
	 * If the start times are equal, compare by their numbers.
	 */
	public int compare(MidiNote x, MidiNote y) {
		if (x.getStartTime() == y.getStartTime())
			return x.getNumber() - y.getNumber();
		else
			return x.getStartTime() - y.getStartTime();
	}


	/** Clone the MidiNote */
	public MidiNote Clone() {
		return new MidiNote(starttime, channel, notenumber, duration);
	}

	@Override
	public String toString() {
		String[] scale = new String[] { "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#" };
		return String.format("MidiNote channel=%1$s number=%2$s %3$s start=%4$s duration=%5$s", channel, notenumber, scale[(notenumber + 3) % 12],
                starttime, duration);

	}

	/** Calculate the pitch of the note */
	public String pitch() {
		String[] scale = new String[] { "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#" };
		return scale[(notenumber + 3) % 12];
	}

	/** Calculate the octave of the note */
	public int octave() {
		return (notenumber / 12) - 1;
	}

}


