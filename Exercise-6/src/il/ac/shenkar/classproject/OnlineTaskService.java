package il.ac.shenkar.classproject;

import java.net.URL;

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
			TasksFromWeb tasksFromWeb = new TasksFromWeb(); // Creating new AsyncTask
			URL url = new URL("http://mobile1-tasks-dispatcher.herokuapp.com/task/random"); // Creating new URL to get tasks from 
			tasksFromWeb.execute(url); // Executing on the given URL

			JSONObject jsonResponse = tasksFromWeb.get(); // Getting the JSONObject from the asynctask
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
