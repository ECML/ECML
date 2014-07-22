package com.ecml;

import android.app.*;
import android.content.DialogInterface;
import android.os.*;
import android.preference.*;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;

import com.ecml.R;


/** @class CalendarPreferencesActivity
 *  The user can change settings for the calendar such as:
 *  - The preferred hour to start practice
 *  - Availability
 *  - Whether the guests can invite other guests
 *  - Whether the guests can see the list of attendees
 *  - If they want a reminder
 *  - Which alarm method they choose
 *  - Number of minutes prior to the event that the reminder should fire
 *  - Default location
 *  - Change how notes are combined into chords (the time interval)
 *  - Change the colors for shading the left/right hands.
 *  - Whether to display measure numbers
 *  - Play selected measures in a loop
 *  - How long to wait before starting to play the music
 * 
 * When created, pass an Intent parameter containing MidiOptions.
 * When destroyed, this activity passes the result MidiOptions to the Intent.
 */
public class CalendarPreferencesActivity extends PreferenceActivity 
    implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    public static final String settingsID = "settings";
    public static final String defaultSettingsID = "defaultSettings";

    private MidiOptions defaultOptions;  /** The initial option values */
    private MidiOptions options;         /** The option values */

    private Preference restoreDefaults;           	/** Restore default settings */
    private CheckBoxPreference[] selectTracks;    	/** Which tracks to display */
    private CheckBoxPreference reminders;      		/** Boolean reminder or not */
    private ListPreference alarmMethod;   		/** Alarm method */
    private ListPreference  minutesPriorAlarm;    /** Minutes prior to the event that the reminder should fire */
    private CheckBoxPreference guestOtherGuest;  	/** Guests can invite other guests*/
    private CheckBoxPreference GuestSeeAttendee;  	/** Guests can see the list attendees */
    private CheckBoxPreference availability;      	/** Mark as busy time */
    private EditTextPreference location;						/** Default location */
    private DialogPreference hour;						/** Preferred hour to start practising */
    

    /** Create the Settings activity. Retrieve the initial option values
     *  (MidiOptions) from the Intent.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Holo_Light);
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
  
        setTitle("ECML: Calendar preferences");
        options = (MidiOptions) this.getIntent().getSerializableExtra(settingsID);
        defaultOptions = (MidiOptions) this.getIntent().getSerializableExtra(defaultSettingsID);
        createView();
    }

    /** Create all the preference widgets in the view */
    private void createView() {
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
        createRestoreDefaultPrefs(root);
       
       
        /*******************GENERAL SETTINGS******************/
        PreferenceCategory generalTitle = new PreferenceCategory(this);
        generalTitle.setTitle("General settings");
        root.addPreference(generalTitle);        
       
        createAvailability(root);
        createLocation(root);
        createHour(root);
        
        /*******************REMINDERS*************************/
        PreferenceCategory remindersTitle = new PreferenceCategory(this);
        remindersTitle.setTitle("Reminders");
        root.addPreference(remindersTitle);
        
        createreminders(root);
        createAlarmMethod(root);
        createMinutesPriorAlarm(root);
        
        /*******************CONCERTS AND MEETINGS*************/
       

        PreferenceCategory meetingsTitle = new PreferenceCategory(this);
        meetingsTitle.setTitle("Concerts and meetings");
        root.addPreference(meetingsTitle);
        
        createGuestOtherGuest(root);
        createGuestSeeAttendee(root);       
        
        setPreferenceScreen(root);
    }

    /** For each list dialog, we display the value selected in the "summary" text.
     *  When a new value is selected from the list dialog, update the summary
     *  to the selected entry.
     */
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ListPreference list = (ListPreference) preference;
        int index = list.findIndexOfValue((String)newValue);
        CharSequence entry = list.getEntries()[index];
        preference.setSummary(entry);
        return true;
    }

    /** When the 'restore defaults' preference is clicked, restore the default settings */
    public boolean onPreferenceClick(Preference preference) {
    	updateOptions();
//        if (preference == restoreDefaults) {
//            options = defaultOptions.copy();
//            createView(); 
//        }
//        else if (preference == setAllToPiano) {
//            for (int i = 0; i < options.instruments.length; i++) {
//                options.instruments[i] = 0;
//            }
//            createView();
//        }
//        else if (preference == setAllTracks ) {
//        	for (int i = 0 ; i < options.tracks.length ; i++) {
//        		options.tracks[i] = true;
//        		options.mute[i] = false;
//        	}
//        	createView();
//        }
        return true;
    }
    
   /**********************************GENERAL SETTINGS FUNCTIONS*********************************/
    /** Create the "Availability" preference */
    private void createAvailability(PreferenceScreen root) {
        availability = new CheckBoxPreference(this);
        availability.setTitle("Mark as a busy time");
//        availability.setChecked(options.showLyrics);
        root.addPreference(availability);
    }
    
    /** Create the "default location" preference */
    private void createLocation(PreferenceScreen root) { 
    	location = new EditTextPreference(this);
    	location.setTitle("Choose a default location");     	
    	root.addPreference(location);    	
    }
    
    /** Create the "preferred hour to start practicing" preference */
    private void createHour(PreferenceScreen root){
    	hour = new TimePreference(this, null);
    	hour.setTitle("Choose the preferred hour to start practicing");
    	root.addPreference(hour);  		

    }
    
    
   /*********************************END OF GENERAL SETTINGS FUNCTIONS**************************/
    
    /**********************************REMINDERS FUNCTIONS*********************************/
    
    /** Create the "Reminders" part. */
    private void createreminders(PreferenceScreen root) {
        reminders = new CheckBoxPreference(this);
        reminders.setTitle("Have a reminder");
//        muteTracks.setChecked(options.reminder);
        root.addPreference(reminders);
        }
    

    /** Create the "Select alarm method " lists.
     */
	private void createAlarmMethod(PreferenceScreen root) {
		alarmMethod = new ListPreference(this);
		alarmMethod.setOnPreferenceChangeListener(this);
		alarmMethod.setEntries(AlarmMethod);
		alarmMethod.setEntryValues(AlarmMethod);
		alarmMethod.setTitle("AlarmMethod");
		// // alarmMethod.setValueIndex(options.instruments[i]);
		alarmMethod.setSummary(alarmMethod.getEntry());
		root.addPreference(alarmMethod);

	}
    
    /** Create the "Select minutes prior event for alarm " lists.
     */
    private void createMinutesPriorAlarm(PreferenceScreen root) {
		minutesPriorAlarm = new ListPreference(this);
		minutesPriorAlarm.setOnPreferenceChangeListener(this);
		minutesPriorAlarm.setEntries(MinutesPriorAlarm);
		minutesPriorAlarm.setEntryValues(MinutesPriorAlarm);
		minutesPriorAlarm.setTitle("Minutes prior to the event that the reminder should fire");
		// minutesPriorAlarm.setValueIndex(options.instruments[i]);
		minutesPriorAlarm.setSummary(minutesPriorAlarm.getEntry());
		root.addPreference(minutesPriorAlarm);
        
    }
    
    
    /*********************************END OF REMINDERS FUNCTIONS**************************/   

    
    /**********************************CONCERTS & MEETINGS FUNCTIONS*********************************/
    
    /** Create the "Guests can invite other guests" preference */
 private void createGuestOtherGuest(PreferenceScreen root) {
     guestOtherGuest = new CheckBoxPreference(this);
     guestOtherGuest.setTitle("Guests can invite other guests");
//     guestOtherGuest.setChecked(options.scrollVert);
     root.addPreference(guestOtherGuest);
 }

 /** Create the "Guests can see the list of attendees" preference */
 private void createGuestSeeAttendee(PreferenceScreen root) {
     GuestSeeAttendee = new CheckBoxPreference(this);
     GuestSeeAttendee.setTitle("Guests can see the list of attendees");
//     GuestSeeAttendee.setChecked(options.showPiano);
     root.addPreference(GuestSeeAttendee);
 }
    
    /*********************************END OF CONCERTS & MEETINGS FUNCTIONS**************************/
    

    /* Create the "Restore Default Settings" preference */
    private void createRestoreDefaultPrefs(PreferenceScreen root) {
//        restoreDefaults = new Preference(this);
//        restoreDefaults.setTitle(R.string.restore_defaults);
//        restoreDefaults.setOnPreferenceClickListener(this);
//        root.addPreference(restoreDefaults);
    } 
    
   
    /** Update the MidiOptions based on the preferences selected. */
    private void updateOptions() {
//        for (int i = 0; i < options.tracks.length; i++) {
//            options.tracks[i] = selectTracks[i].isChecked();
//        }
//        for (int i = 0; i < options.mute.length; i++) {
//            options.mute[i] = muteTracks[i].isChecked();
//        }
//        for (int i = 0; i < options.tracks.length; i++) {
//            ListPreference entry = selectInstruments[i];
//            options.instruments[i] = entry.findIndexOfValue(entry.getValue());
//        }
////        options.scrollVert = scrollVertically.isChecked();
//        //options.showPiano = showPiano.isChecked();
//        //options.showLyrics = showLyrics.isChecked();
//        if (twoStaffs != null)
//            options.twoStaffs = twoStaffs.isChecked();
//        else
//            options.twoStaffs = false;
//
//        options.showNoteLetters = Integer.parseInt(showNoteLetters.getValue());
//        options.transpose = Integer.parseInt(transpose.getValue());
//        options.key = Integer.parseInt(key.getValue());
//        if (time.getValue().equals("Default")) {
//            options.time = null;
//        }
//        else if (time.getValue().equals("3/4")) {
//            options.time = new TimeSignature(3, 4, options.defaultTime.getQuarter(),
//                                             options.defaultTime.getTempo());
//        }
//        else if (time.getValue().equals("4/4")) {
//            options.time = new TimeSignature(4, 4, options.defaultTime.getQuarter(),
//                                             options.defaultTime.getTempo());
//        }
//        options.combineInterval = Integer.parseInt(combineInterval.getValue());
//        options.shade1Color = shade1Color.getColor();
//        options.shade2Color = shade2Color.getColor();
//        options.showMeasures = showMeasures.isChecked();
//        options.playMeasuresInLoop = playMeasuresInLoop.isChecked();
//        options.playMeasuresInLoopStart = Integer.parseInt(loopStart.getValue()) - 1;
//        options.playMeasuresInLoopEnd = Integer.parseInt(loopEnd.getValue()) - 1;
//        options.delay = Integer.parseInt(delay.getValue());
    }

    /** When the back button is pressed, update the MidiOptions.
     *  Return the updated options as the 'result' of this Activity.
     */
//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent();
//        updateOptions();
//        intent.putExtra(SettingsActivity.settingsID, options);
//        setResult(Activity.RESULT_OK, intent);
//        super.onBackPressed();
//    }
    
    public static String[] AlarmMethod = {
        "Default alarm", "Email",
        "alert"};
    
    public static String[] MinutesPriorAlarm = {
        "10 min", "20 min",
        "30 min", "40 min", "50 min", "60 min", "70 min", "80 min", "90 min", "100 min", "110 min", "120 min"};
            
}

