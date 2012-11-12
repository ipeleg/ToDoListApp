package il.ac.shenkar.classproject;

import java.util.ArrayList;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.app.*;
import android.content.*;

public class TaskListActivity extends Activity
{
	private ArrayList<Task> taskList; 
	private ListView lv1;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list); // Setting the UI view with activity_task_list    
        
        taskList = new ArrayList<Task>(); // Creating new task list which will hold the tasks added
        lv1 = (ListView) findViewById(R.id.listView1); // Creating the ListView
        Button addTaskButton = (Button) findViewById(R.id.add_button); // Creating the addTask button
        addTaskButton.setOnClickListener(new View.OnClickListener() // Setting click listener for addTask button 
		{			
			public void onClick(View v)
			{
				Intent intent = new Intent(v.getContext(), CreateTaskActivity.class); // Creating new Intent that will activate on click
				startActivityForResult(intent, 0); // Starting the new intent for getting result
			}
		});
    }
    
	protected void onActivityResult(int requestCode,int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK)
		{
			Bundle bundle = data.getExtras(); // Getting the bundle from the intent
			Task newTask = new Task(bundle.getString("name")); // Creating the new task
			taskList.add(newTask); // Adding the new task to the array
			lv1.setAdapter(new TaskListAdapter(this,taskList)); // Setting the ListView Adapter
		} 
	}
    
}
