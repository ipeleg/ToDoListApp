package il.ac.shenkar.classproject;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.app.*;
import android.content.*;

public class TaskListActivity extends Activity
{
	private ListView lv1;
	private TaskListAdapter adapter;
	private TaskListArray tasksList;
	private Spinner sortSpinner;
	private TextView sort;
	private TaskListDataBase taskDataBase;
	private Context context;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list); // Setting the UI view with activity_task_list
        context = getApplicationContext(); // Getting the application context
        
        lv1 = (ListView) findViewById(R.id.listView1); // Creating the ListView
        tasksList = TaskListArray.getInstance(getApplicationContext()); // Creating the tasks list
        sortSpinner = (Spinner) findViewById (R.id.sort_spinner); // Creating the spinner
        sort = (TextView) findViewById (R.id.sort); // Creating the sort

        taskDataBase = TaskListDataBase.getInstance(getApplicationContext());
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
                        
                    }

					public void onNothingSelected(AdapterView<?> arg0)
					{
						
					}
        		});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.settings, menu);
    	
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
    		
    		case R.id.settings:
    			startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
    			break;
    	}
    	return true;
    }
    
	public void onResume()
	{
		super.onResume();
		adapter.notifyDataSetChanged(); // Refresh the ListView when resuming this activity
	}
}
