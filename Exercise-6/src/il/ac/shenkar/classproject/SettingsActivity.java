package il.ac.shenkar.classproject;

import il.ac.shenkar.classproject.alarm.SetReminder;
import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.preference.*;
import android.preference.Preference.OnPreferenceChangeListener;

public class SettingsActivity extends PreferenceActivity
{
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_xml);
		
		CheckBoxPreference checkBox = (CheckBoxPreference) findPreference("fetch_task_checkbox");
		checkBox.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{
			public boolean onPreferenceChange(Preference preference, Object newValue)
			{
				SetReminder setReminder = new SetReminder();
		        Intent intentService = new Intent (getApplicationContext(), OnlineTaskService.class);	
		        PendingIntent pi = PendingIntent.getService(getApplicationContext(), 0, intentService, 0);
		        
				if (newValue.toString().equals("true"))
				{
					setReminder.setAutomaticTaskResolver(getApplicationContext(), pi, 24*60*60*1000, false);
				}
				else
				{
					setReminder.setAutomaticTaskResolver(getApplicationContext(), pi, 24*60*60*1000, true);
				}
				return true;
			}
			
		});
	}
}
