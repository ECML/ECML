package com.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.ecml.R;



/*Eternal challenge for music learners game... game about feelings in music
        The number of robots on earth is exactly the same as the number of humans emotions.
        Each robot is a symbol of an emotion.
        Music is all about emotions. The aim of the game is to provide new possibilities to improve
        the user abilities. It consists on transforming each robot into a human being, by selecting him
        as a player, and play the music. The player selects a robot and a song.
        If the music is correctly played (tempo, notes,,,) the robots is instantely transformed into a human being.
        Then you help to preserve the human feelings by transfering them to robots through music.*/


public class Feelings_game extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_feelings);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
