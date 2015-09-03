package com.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.ecml.R;



/* In this game, one player have to play the note diplayed on the screen before the end of a countdown.
There are two modes : The first one where the player selects its level, the second one where
the difficulty level go up by itself.(progress mode)
 */

public class PlayTheNote extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_play_the_note);
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
