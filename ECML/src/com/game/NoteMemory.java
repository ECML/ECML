package com.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.ecml.R;


/* The aim of the game is to improve your memory of notes. Several notes will appear on a score
during precise period of time and disapear after. The user will have to memorise as much notes
as he can. For that, he will have to select notes in a notes selection : select good ones and
put them in the right order.   */

public class NoteMemory extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_note_memory);
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
