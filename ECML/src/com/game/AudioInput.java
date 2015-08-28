/**
 * Interface for Audio / Microphone readers
 */

package com.game;

public interface AudioInput {
    public void startRecorder();
    public void stopRecorder();
    public void pauseRecorder();
    public boolean isActive();
}
