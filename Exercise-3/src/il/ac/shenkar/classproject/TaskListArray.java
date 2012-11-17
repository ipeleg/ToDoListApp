package il.ac.shenkar.classproject;

import java.util.ArrayList;

public class TaskListArray
{
	private static TaskListArray array;
	private  ArrayList<Task> tasksArray;
	
	private TaskListArray()
	{
		tasksArray = new ArrayList<Task>();
	}
	
	public static TaskListArray getInstance()
	{
		if (array == null)
			array = new TaskListArray();
		return array;
	}
	
	public void addTask(Task newTask)
	{
		tasksArray.add(newTask);
	}
	
	public void removeTask(Task task2Del)
	{
		tasksArray.remove(task2Del);
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
