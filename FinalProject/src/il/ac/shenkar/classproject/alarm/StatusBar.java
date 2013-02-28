package il.ac.shenkar.classproject.alarm;

import il.ac.shenkar.classproject.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.Uri;
import android.support.v4.app.*;

public class StatusBar extends BroadcastReceiver
{
	private NotificationManager nm;
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		long id = 0;
		
		Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.notification_large_icon);
		TaskListDataBase taskDataBase = TaskListDataBase.getInstance(context); // Getting an instance of the application SQLite database
		TaskListArray tasksList = TaskListArray.getInstance(context); // Creating the tasks list
		
		id = intent.getLongExtra("taskId", -1); // Getting the task id that supposed to be reminded
		Task task = (Task) taskDataBase.getTask(id); // Getting this task from the database
		task.setHasReminder(0);
		tasksList.updateTask(task, id);
		
		Intent newIntent = new Intent(context, ShowTaskDetails.class); // Creating an Intent to start the ToDoApplication
		newIntent.putExtra("taskId", id); // Adding the bundle to the intent
		PendingIntent pi = PendingIntent.getActivity(context, (int)task.getId(), newIntent, 0); // Wrapping the intent with PendingIntent to set in the notification
		
		nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); // Getting the Notification Manager
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		
		builder.setContentTitle(task.getTitle())
				.setContentText(task.getDescription())
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.setContentIntent(pi)
				.setSmallIcon(R.drawable.notification_small_icon)
				.setLargeIcon(icon)
				.setSound(Uri.parse("android.resource://il.ac.shenkar.classproject/"+ R.raw.bell_ringing));
		
		Notification newNotification = builder.build(); // Building the new notification with the task details
		newNotification.flags |= Notification.FLAG_AUTO_CANCEL; // Dismiss the notification after been selected
		nm.notify((int)task.getId(), newNotification); // Adding the new notification to the Notification Manager with the task id
	}
}
