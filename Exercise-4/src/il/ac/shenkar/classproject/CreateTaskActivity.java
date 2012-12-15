package il.ac.shenkar.classproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateTaskActivity extends Activity
{
	private TaskListDataBase taskDataBase;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_task_activity); // Setting the UI view with create_task_list
		
		taskDataBase = TaskListDataBase.getInstance(getApplicationContext());
		Button createActivityButton = (Button) findViewById(R.id.create_button); //Creating the 'create' button
		createActivityButton.setOnClickListener(new View.OnClickListener() // Setting click listener for addTask button 
		{
			public void onClick(View v)
			{
				EditText titleField = (EditText) findViewById(R.id.enter_task_title); // Getting the task title
				Task newTask= new Task (titleField.getText().toString()); // Creating new task with that title
				
				EditText descriptionField = (EditText) findViewById(R.id.enter_task_description); // Getting the task description
				newTask.setDescription(descriptionField.getText().toString()); // Setting the task description
				
				taskDataBase.addTask(newTask); // Adding to DataBase
				
				finish();
			}
		});
	}

}
