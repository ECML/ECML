package com.ecml;

import android.app.Application;

/** @class ECML
 * <br>
 * This class stores all variables that need to be available from any activity of the application.
 *
 */
public class ECML extends Application {

		public static FileUri song;	/* This variable stores the latest chosen song so that
									 * the user can go back to score whenever the user wants */
		public static int speedLvl;	/* This variable is used to set the player's speed bar at the right
									 * speed when opening SpeedGamelvln activity */

}
