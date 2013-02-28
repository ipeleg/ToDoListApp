package il.ac.shenkar.classproject.alarm;

import il.ac.shenkar.classproject.*;
import java.util.Calendar;
import android.app.*;
import android.content.*;
import android.location.LocationManager;
import android.util.Log;

public class SetReminder
{
	public SetReminder()
	{

	}

	// Method for setting a proximity alert
	public void setProximityAlert(Context context, Task task, double latitude, double longitude)
	{
		Intent intent = new Intent(context, StatusBar.class); // Creating new intent to active with the reminder
		intent.putExtra("taskId", task.getId()); // Adding to the indent the task id to remind
		PendingIntent pi = PendingIntent.getBroadcast(context, ((int)task.getId())+500, intent, 0); // Wrapping the intent in pending intent and adding it to the alarm manager with the task id+500(not to overwrite the reminder), so multiply reminders is available 
		
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE); // Getting the location manager		
		lm.addProximityAlert(latitude, longitude, 300, -1, pi);
		
		Log.i("SetReminder", "Proximity alert for task " + task.getId() + " was added.");
	}
	
	// Method for canceling proximity alert
	public void cancelProximityAlert(Context context, long id)
	{
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE); // Getting the location manager
		
		PendingIntent pi = PendingIntent.getBroadcast(context, ((int)id)+500, new Intent(context, StatusBar.class), 0);
		lm.removeProximityAlert(pi);
		
		Log.i("SetReminder", "Proximity alert for task " + id + " was canceled.");
	}
	
	// Method for setting a one time reminder
	public void setOneTimeReminder(Context context, Calendar cal, Task task)
	{
		Intent intent = new Intent(context, StatusBar.class); // Creating new intent to active with the reminder
		intent.putExtra("taskId", task.getId()); // Adding to the indent the task id to remind
		PendingIntent pi = PendingIntent.getBroadcast(context, (int)task.getId(), intent, 0); // Wrapping the intent in pending intent and adding it to the alarm manager with the task id, so multiply reminders is available 

		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // Getting the alarm manager
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi); // Adding the alarm manager the pending intent
		
		Log.i("SetReminder", "Reminder for task " + task.getId() + " was added.");
	}
	
	// Method for canceling a reminder according to the PenidingIntent ID
	public void cancelReminder(Context context, long id)
	{
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // Getting the alarm manager
		
		PendingIntent pi = PendingIntent.getBroadcast(context, (int)id, new Intent(context, StatusBar.class), 0);
		am.cancel(pi);
		
		Log.i("SetReminder", "Reminder for task " + id + " was canceled.");
	}
	
	// Method for setting an automatic action with an interval
	public void setAutomaticTaskResolver(Context context, PendingIntent pi, long intervalMillis, boolean cancel)
	{
		Calendar calendar = Calendar.getInstance();
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // Getting the alarm manager
		
		if (cancel) // Checking if the method was called for canceling the repeating alarm
		{
			am.cancel(pi); // Canceling the alarm
			return;
		}
		
		am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intervalMillis, pi);
	}
}
