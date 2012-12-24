package il.ac.shenkar.alarm;

import il.ac.shenkar.classproject.*;
import android.app.*;
import android.content.*;
import android.graphics.*;

public class StatusBar extends BroadcastReceiver
{
	private NotificationManager nm;
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.notification_large_icon);
		TaskListDataBase taskDataBase = TaskListDataBase.getInstance(context); // Getting an instance of the application SQLite database
		long id = 0;
		
		id = intent.getLongExtra("taskID", -1); // Getting the task id that supposed to be reminded
		Task task = (Task) taskDataBase.getTask(id); // Getting this task from the database
		
		Intent newIntent = new Intent(context,TaskListActivity.class); // Creating an Intent to start the ToDoApplication
		PendingIntent pi = PendingIntent.getActivity(context, (int)task.getId(), newIntent, 0); // Wrapping the intent with PendingIntent to set in the notification
		
		nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); // Getting the Notification Manager
		Notification.Builder builder = new Notification.Builder(context);
		
		builder.setContentTitle(task.getTitle())
				.setContentText(task.getDescription())
				.setDefaults(Notification.DEFAULT_ALL)
				.setContentIntent(pi)
				.setSmallIcon(R.drawable.notification_small_icon)
				.setLargeIcon(icon);
		
		Notification newNotification = builder.build(); // Building the new notification with the task details
		newNotification.flags |= Notification.FLAG_AUTO_CANCEL; // Dismiss the notification after been selected
		nm.notify((int)task.getId(), newNotification); // Adding the new notification to the Notification Manager with the task id
	}
}
