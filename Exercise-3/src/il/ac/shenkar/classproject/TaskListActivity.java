package il.ac.shenkar.classproject;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.app.*;
import android.content.*;

public class TaskListActivity extends Activity
{
	private ListView lv1;
	private TaskListAdapter adapter;
	TaskListArray tasksList;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list); // Setting the UI view with activity_task_list    
        
        lv1 = (ListView) findViewById(R.id.listView1); // Creating the ListView
        tasksList = TaskListArray.getInstance(); // Creating the tasks list
        
        adapter = new TaskListAdapter(this); // Creating new adapter for the ListView
        lv1.setAdapter(adapter); // Setting the ListView Adapter
        
        Button addTaskButton = (Button) findViewById(R.id.add_button); // Creating the addTask button
        addTaskButton.setOnClickListener(new View.OnClickListener() // Setting click listener for addTask button 
		{			
			public void onClick(View v)
			{
				Intent intent = new Intent(v.getContext(), CreateTaskActivity.class); // Creating new Intent that will activate on click
				startActivity(intent); // Starting the new Activity
			}
		});
    }
    
	public void onResume()
	{
		super.onResume();
		adapter.notifyDataSetChanged(); // Refresh the ListView when resuming this activity
	}
}
