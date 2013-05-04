//Class for main preferences list
//Version 1.0. Last update: never.
//First developer: Ivanushkin Nikolay. Developer: also
//This class sets preferences on settings.xml (/res/xml/settings.xml)

package ru.nim579.nixieclock;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Prefs extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	addPreferencesFromResource(R.xml.settings);
	}	
}
