package il.ac.shenkar.classproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateTaskActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_task_activity); // Setting the UI view with create_task_list
		
		Button createActivityButton = (Button) findViewById(R.id.create_button); //Creating the 'create' button
		createActivityButton.setOnClickListener(new View.OnClickListener() // Setting click listener for addTask button 
		{			
			public void onClick(View v)
			{
				EditText textField = (EditText) findViewById(R.id.add_activity_text);
				Task newTask= new Task (textField.getText().toString());
				
				TaskListArray array = TaskListArray.getInstance();
				array.addTask(newTask);
				
				finish();
			}
		});
	}

}
