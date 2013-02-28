package il.ac.shenkar.classproject;

import com.google.analytics.tracking.android.EasyTracker;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.app.*;
import android.content.*;

public class TaskListActivity extends Activity
{
	private ListView lv1;
	private TaskListAdapter adapter;
	private TaskListArray tasksList;
	private Spinner sortSpinner;
	private TaskListDataBase taskDataBase;
	private Context context;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	requestWindowFeature(Window.FEATURE_ACTION_BAR);
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list); // Setting the UI view with activity_task_list
        context = getBaseContext(); // Getting the application context
        
        lv1 = (ListView) findViewById(R.id.listView1); // Creating the ListView
        tasksList = TaskListArray.getInstance(context); // Creating the tasks list
        sortSpinner = (Spinner) findViewById (R.id.sort_spinner); // Creating the spinner

        taskDataBase = TaskListDataBase.getInstance(context);
        tasksList.setTasks(taskDataBase.getAllTasks()); // Setting the tasks from database
        
        adapter = new TaskListAdapter(this); // Creating new adapter for the ListView
        lv1.setAdapter(adapter); // Setting the ListView Adapter
        
        Button addTaskButton = (Button) findViewById(R.id.add_button); // Creating the addTask button
        addTaskButton.setOnClickListener(new View.OnClickListener() // Setting click listener for addTask button 
		{			
			public void onClick(View v)
			{   
				Intent intent = new Intent(context, CreateTaskActivity.class); // Creating new Intent that will activate on click
				startActivity(intent); // Starting the new Activity
			}
		});
        
        sortSpinner.setOnItemSelectedListener(
        		new OnItemSelectedListener()
        		{
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        switch(position)
                        {
                        	case 0:
                        		break;
                        		
                        	case 1: // Sort the list according to Task creation date
                        		tasksList.sortFirstCreatedTaskFirst();
                        		break;
                        		
                        	case 2: // Sort the list according to Task title
                        		tasksList.sortByTitle();
                        		break;
                        }
                        
                		adapter.notifyDataSetChanged(); // Refresh the ListView when resuming this activity
                    }

					public void onNothingSelected(AdapterView<?> arg0)
					{
						
					}
        		});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	getMenuInflater().inflate(R.menu.settings, menu);
    	
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch(item.getItemId())
    	{
    		case R.id.exit:
    			finish();
    			break;
    	}
    	return true;
    }
    
	public void onResume()
	{
		super.onResume();
		adapter.notifyDataSetChanged(); // Refresh the ListView when resuming this activity
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		EasyTracker.getInstance().setContext(getBaseContext());
		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	public void onStop()
	{
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
		EasyTracker.getInstance().dispatch();
	}
}
