//Class for Activity.About
//Version 1.0. Last update: never.
//First developer: Ivanushkin Nikolay. Developer: also
//This class sets content view on on about.xml

package ru.nim579.nixieclock;

import android.app.Activity;
import android.os.Bundle;

public class About extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	}
}
