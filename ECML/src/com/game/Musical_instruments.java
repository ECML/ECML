package com.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.ecml.R;



/* The goal of the game is to improve the students hearing ability.
The idea is to launch a short piece of music, the student has to listen carefully and in
the end list out all the instruments used in the song. We will propose a whole list of
different instruments and he will have to select only those which are truly in use.
The game may have several difficulty levels to better help the student to improve.
The higher the level, the harder the piece is to analyse.
 */

public class Musical_instruments extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_musical_instruments);
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
