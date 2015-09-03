package com.game;

import android.os.Bundle;

import com.ecml.R;
import com.sideActivities.BaseActivity;


/* The principle is the same as a simple memory game. There are cards that you have to return.
Each card have a score with a note on it, or the name of the not (like A,B or La, Si).
Your goal is to find the name links to the note on the score.
The game is nice for children who want to learn how to read a score quickly.
We can also add another level of difficulty, for the stronger ones. In this level,
when you return a card, there is only the sound of the note. Each sound is associated to another card
with his note on the score on it, and you have to learn well the sound of each note to maximise the score.*/

public class Memory_game extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_memory);
    }
}