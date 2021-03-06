package il.ac.shenkar.classproject;

import java.util.ArrayList;

import android.content.Context;

public class TaskListArray
{
	private Context context;
	private static TaskListArray array;
	private  ArrayList<Task> tasksArray;
	private TaskListDataBase dataBase;
	
	private TaskListArray(Context context)
	{
		this.context = context;
		dataBase = TaskListDataBase.getInstance(this.context);
		tasksArray = new ArrayList<Task>();
	}
	
	public static TaskListArray getInstance(Context context)
	{
		if (array == null)
			array = new TaskListArray(context);
		return array;
	}
	
	public void addTask(Task newTask)
	{
		tasksArray.add(0,newTask); // Adding new element to the first place in the array
	}
	
	public void removeTask(int position)
	{
		Task task2Del = tasksArray.get(position);
		
		dataBase.deleteTask(task2Del);
		tasksArray.remove(position);
	}
	
	public void setTasks(ArrayList<Task> tasks)
	{
		this.tasksArray = tasks;
	}
	
	public int getSize()
	{
		return tasksArray.size();
	}
	
	public Task getTask(int position)
	{
		return tasksArray.get(position);
	}
}
