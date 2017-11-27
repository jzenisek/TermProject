/**
 * @file ChristmasPreferences.java 
 * @brief sets up the class PreferenceActivity
 * 
 * 
 */
 
package com.cs3560.largejollyhottamales.christmas;

import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class ChristmasPreferences extends PreferenceActivity {
	String beginningInterval = null;
	
	public static final String SINGLE_ENABLED_KEY = "notify_single_enable";
	public static final boolean SINGLE_ENABLED_DEFAULT = true;
	
	public static final String RECURRING_ENABLED_KEY = "notify_recurring_enable";
	public static final boolean RECURRING_ENABLED_DEFAULT = true;
	
	public static final String RECURRING_INTERVAL_KEY = "notify_recurring_interval";
	public static final String RECURRING_INTERVAL_DEFAULT = "daily";
	
	public static final String VIBRATE_KEY = "notify_vibrate";
	public static final boolean VIBRATE_DEFAULT = true;
	
	public static final String RINGTONE_KEY = "notify_ringtone";
	public static final String RINGTONE_DEFAULT = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.settings);
		PreferenceManager.setDefaultValues(this, R.xml.settings, false);
		
		setupControls();
	}
	/// @brief Sets up all the preferences for the app
	/// @param none
	/// @return none
	public void setupControls() {
		beginningInterval = PreferenceManager.getDefaultSharedPreferences(this).getString(RECURRING_INTERVAL_KEY, RECURRING_INTERVAL_DEFAULT);
		
		updateIntervalSummary(beginningInterval);
		updateRingtoneSummary(PreferenceManager.getDefaultSharedPreferences(this).getString(RINGTONE_KEY, null));
		
		// schedule/cancel single Christmas alarm based on whether preference is checked
		/// @brief lets the user set a preference for the Christmas Alarm
		/// @param Preference preference
		/// @param Object newValue
		/// @return true/false
		findPreference(SINGLE_ENABLED_KEY).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean value = ((Boolean) newValue).booleanValue();
				if (value)
					ChristmasAlarm.setChristmasAlarm(ChristmasPreferences.this);
				else
					ChristmasAlarm.cancelChristmasAlarm(ChristmasPreferences.this);
				return true;
			}
		});
		
		// schedule/cancel recurring Christmas alarm based on whether preference is checked
		/// @brief lets the user set a preference for the Recurring Alarm
		/// @param Preference preference
		/// @param Object newValue
		/// @return true/false
		findPreference(RECURRING_ENABLED_KEY).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean value = ((Boolean) newValue).booleanValue();
				if (value)
					ChristmasAlarm.setRecurringAlarm(ChristmasPreferences.this);
				else
					ChristmasAlarm.cancelRecurringAlarm(ChristmasPreferences.this);
				
				return true;
			}
		});
		
		// reschedule recurring Christmas alarm when interval changes
		/// @brief lets the user set a preference for the Reucurring interval Alarm
		/// @param Preference preference
		/// @param Object newValue
		/// @return true/false
		findPreference(RECURRING_INTERVAL_KEY).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				String value = (String) newValue;
				if (!value.equals(beginningInterval)) {
					ChristmasAlarm.cancelRecurringAlarm(ChristmasPreferences.this);
					ChristmasAlarm.setRecurringAlarm(ChristmasPreferences.this, value);
					
					updateIntervalSummary((String) newValue);
					
					beginningInterval = value;
				}
				return true;
			}
		});
		
		findPreference(RINGTONE_KEY).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				updateRingtoneSummary((String) newValue);
				return true;
			}
		});
	}
	/// @brief updates the interval value
	/// @param String value
	/// @return none
	private void updateIntervalSummary(String value) {
		findPreference(RECURRING_INTERVAL_KEY).setSummary(codeToName(value));
	}
	/// @brief updates the ringtones
	/// @param String uri
	/// @return none
	private void updateRingtoneSummary(String uri) {
		String summary;
		
		if (uri != null && !uri.equals(""))
			summary = RingtoneManager.getRingtone(this, Uri.parse(uri)).getTitle(this);
		else
			summary = "Silent";
		
		findPreference(RINGTONE_KEY).setSummary(summary);
	}
	private String codeToName(String code) {
		String[] codes = getResources().getStringArray(R.array.notify_recurring_interval_values);
		String[] names = getResources().getStringArray(R.array.notify_recurring_interval_names);

		for (int i=0; i<codes.length; i++) {
			if (codes[i].equals(code))
				return names[i];
		}
		return null;
	}
	
}