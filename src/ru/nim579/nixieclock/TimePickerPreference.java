//Class for making TimePicker preference view
//Version 1.0. Last update: never.
//This code was taken from an external source (website) and not changed

package ru.nim579.nixieclock;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

public class TimePickerPreference extends DialogPreference {

	private static final String DEFAULT_TIME = "00:00";

	private TimePicker mTimePicker;

	public TimePickerPreference(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		setPositiveButtonText(R.string.general_ok);
		setNegativeButtonText(R.string.general_cancel);
	}

	public TimePickerPreference(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}

	public TimePickerPreference(Context context){
		this(context, null);
	}

	@Override
	protected View onCreateDialogView(){
		mTimePicker = new TimePicker(getContext());
		mTimePicker.setEnabled(true);
		return mTimePicker;
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		TimePicker timePicker = mTimePicker;
		int[] hourAndMinutes = getHourAndTime(getPersistedString(DEFAULT_TIME));
		timePicker.setCurrentHour(hourAndMinutes[0]);
		timePicker.setCurrentMinute(hourAndMinutes[1]);
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getString(index);
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		persistString(restoreValue ? getPersistedString(getCurrentTime())
				: (String) defaultValue);
	}

	protected void onDialogClosed(boolean positiveResult){
		if (positiveResult){
			if (mTimePicker != null){
				persistString(getCurrentTime());
			}
		}
	}

	/**
	 * 
	 * 
	 * 
	 * @return string in format HH:MM
	 */

	private String getCurrentTime(){
		if (mTimePicker != null) {
			return mTimePicker.getCurrentHour() + ":"+ mTimePicker.getCurrentMinute();
		}
		return DEFAULT_TIME;
	}

	/**
	 * 
	 * 
	 * 
	 * @param timeString
	 *            string in format HH:MM
	 * 
	 * @return array of size 2; first element - hours, second - minutes
	 */

	public static int[] getHourAndTime(String timeString){
		if (!timeString.contains(":")) {
			throw new RuntimeException("Bad time string");
		}
		String[] values = timeString.split(":");
		return new int[] { Integer.parseInt(values[0]), Integer.parseInt(values[1]) };

	}

}
