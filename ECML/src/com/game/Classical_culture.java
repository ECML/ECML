package com.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.ecml.R;


/* The aim of this game is to test the knowledge of the user. The principle is really simple.
The user has to listen to a part of a famous classical music and has to select the name
of this piece and its author  */

public class Classical_culture extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_classcal_culture);
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
