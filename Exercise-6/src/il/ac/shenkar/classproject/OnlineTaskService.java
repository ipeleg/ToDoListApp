package il.ac.shenkar.classproject;

import il.ac.shenkar.classproject.onlinetasks.GetOnlineTasks;
import org.json.JSONObject;
import android.app.IntentService;
import android.content.Intent;

public class OnlineTaskService extends IntentService 
{	
	public OnlineTaskService()
	{
		super("OnlineTaskService");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		TaskListDataBase taskDataBase;
		
		try
		{
			JSONObject jsonResponse = GetOnlineTasks.getTasks(); // Getting the JSONObject
			taskDataBase = TaskListDataBase.getInstance(getApplicationContext()); // Getting database instance
			
			Task newTask= new Task (jsonResponse.getString("topic")); // Creating new task with that title
			newTask.setDescription(jsonResponse.getString("description")); // Setting the task description
			
			taskDataBase.addTask(newTask); // Adding to DataBase
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
