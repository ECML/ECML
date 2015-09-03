package com.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.ecml.R;



/*This mini game enable you to develop your score reading speed skills.
You will have to memorise a sequence of notes and play it correctly in a limited time afterwards.
2 versions : you will need your instruments to perform the first version,
 while you won't in the second one.

Version1 :
use your instruments to play the sequence of notes.
The higher the level the more notes you have to play.

Version 2
Use the button on the screen to execute the sequence of notes
The higher the level the more notes you have to play.
  */

public class Memorize_sequence extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_memorize_sequence);
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
