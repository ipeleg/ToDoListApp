package il.ac.shenkar.classproject;

import il.ac.shenkar.classproject.alarm.SetReminder;
import il.ac.shenkar.classproject.fragments.*;
import il.ac.shenkar.classproject.location.CreateGoogleMap;
import il.ac.shenkar.classproject.onlinetasks.GetOnlineTasks;
import java.net.URL;
import java.util.Calendar;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;

import android.annotation.SuppressLint;
import android.app.*;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.net.*;
import android.os.*;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.view.*;
import android.widget.*;

@SuppressLint("HandlerLeak")
public class CreateTaskActivity extends FragmentActivity implements OnDateSetListener, OnTimeSetListener
{
	// Variables for the DatePicker
	private DatePickerFragment newDateFragment = null;
	private int year=-1, month=-1, day=-1;
	
	// Variables for the TimePicker
	private TimePickerFragment newTimeFragment = null;
	private int hour=-1, minute=-1;
	
	private TextView dateSeted;
	private TextView timeSeted;
	private boolean checkedReminder;
	private boolean checkedProximity;
	private TaskListDataBase taskDataBase;
	private SetReminder setReminder;
	private Button setDate;
	private Button setTime;
	private Button getTaskFromWeb;
	private ProgressBar progress;
	static final int DATE_DIALOG_ID = 1;
	
	private EditText descriptionField;
	private EditText titleField;
	
	private Context context;
	private double latitude;
	private double longitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_task_activity); // Setting the UI view with create_task_list
		context = getBaseContext();
		setReminder = new SetReminder();
		
		taskDataBase = TaskListDataBase.getInstance(getBaseContext()); // Getting database instance
		dateSeted = (TextView) findViewById(R.id.date_set_view);
		timeSeted = (TextView) findViewById(R.id.time_set_view);
		getTaskFromWeb = (Button) findViewById(R.id.get_from_web_button);
		progress = (ProgressBar) findViewById(R.id.progress_bar);
		
		descriptionField = (EditText) findViewById(R.id.enter_task_description); // Getting the task description
		titleField = (EditText) findViewById(R.id.enter_task_title); // Getting the task title
		
		Button createActivityButton = (Button) findViewById(R.id.create_button); //Creating the 'create' button
		createActivityButton.setOnClickListener(new View.OnClickListener() // Setting click listener for addTask button 
		{
			public void onClick(View v)
			{
				long rowID;
				TaskListArray array = TaskListArray.getInstance(context);
				
				Task newTask= new Task (titleField.getText().toString()); // Creating new task with that title
				newTask.setDescription(descriptionField.getText().toString()); // Setting the task description
				
				if (checkedReminder) // If the user want to set a reminder
				{
					if (!isDateAndTimeSet()) // Making sure the user picked date and time for the reminder
						Toast.makeText(context, "You must set date and time.", Toast.LENGTH_SHORT).show();
					else
					{
						Calendar cal = Calendar.getInstance();
						cal.set(year, month, day, hour, minute, 0); // Setting a new Calendar instance with the user reminder date
						
						Time reminder = new Time(); // Reminder in Time format to store in the object
						reminder.set(0, minute, hour, day, month, year); // Setting the reminder to be saved in the Task object
						newTask.setReminder(reminder);
						newTask.setHasReminder(1); // Setting the boolean flag to indicate that that the task has reminder
					
						rowID = taskDataBase.addTask(newTask); // Adding to DataBase
						newTask.setId(rowID);
						array.addTask(newTask);
						
						setReminder.setOneTimeReminder(context, cal, newTask); // Creating a new reminder to be added to the alarm manager
					}
				}
				else // Else, just create a new task
				{
					rowID = taskDataBase.addTask(newTask); // Adding to DataBase
					newTask.setId(rowID);
					array.addTask(newTask); // Adding to Array
				}
				
				if (checkedProximity) // If the user want to add proximity alert
					setReminder.setProximityAlert(context, newTask, latitude, longitude);
				
				finish();
			}
		});
		
		// Set Date Button
		setDate = (Button) findViewById(R.id.set_date); //Creating the 'Set Date' button
		setDate.setOnClickListener(new View.OnClickListener() // Setting click listener for setDate button 
		{
			public void onClick(View v)
			{
				newDateFragment = new DatePickerFragment();
				newDateFragment.show(getSupportFragmentManager(), "datePicker"); // Show the user dialog to pick a date
			}
		});
		
		// Set Time button
		setTime = (Button) findViewById(R.id.set_time); //Creating the 'Set Time' button
		setTime.setOnClickListener(new View.OnClickListener() // Setting click listener for setTime button 
		{
			public void onClick(View v)
			{	
				newTimeFragment = new TimePickerFragment();
				newTimeFragment.show(getSupportFragmentManager(), "timePicker"); // Show the user dialog to pick a time
			}
		});
		
		
		// Checkbox for the proximity alerts
		final CheckBox checkBoxProximity = (CheckBox) findViewById(R.id.set_proximity_alert); //Creating the CheckBox for proximity alert
		checkBoxProximity.setOnClickListener(new View.OnClickListener() // Setting click listener for the check-box for controlling the set's buttons clickability
		{
			public void onClick(View v)
			{
				checkedProximity = ((CheckBox) v).isChecked();
				switch(v.getId())
				{
		        case R.id.set_proximity_alert:
		            if (checkedProximity) // If the CheckBox is checked allow the user to set a reminder, else don't
		            {
						Intent intent = new Intent(context, CreateGoogleMap.class); // Creating new Intent that will start and activity with map
						startActivityForResult(intent,1); // Starting the new activity
		            }
		            else
		            {

		            }
		            break;
				}
			}
		});
		
		// Checkbox for the reminders
		final CheckBox checkBoxReminder = (CheckBox) findViewById(R.id.set_reminder); //Creating the CheckBox for reminder alert
		checkBoxReminder.setOnClickListener(new View.OnClickListener() // Setting click listener for the check-box for controlling the set's buttons clickability
		{
			public void onClick(View v)
			{
				checkedReminder = ((CheckBox) v).isChecked();
				switch(v.getId())
				{
		        case R.id.set_reminder:
		            if (checkedReminder) // If the CheckBox is checked allow the user to set a reminder, else don't
		            {
						setDate.setVisibility(View.VISIBLE);
						setTime.setVisibility(View.VISIBLE);
						dateSeted.setVisibility(View.VISIBLE);
						timeSeted.setVisibility(View.VISIBLE);
		            }
		            else
		            {
		            	setDate.setVisibility(View.INVISIBLE);
						setTime.setVisibility(View.INVISIBLE);
						dateSeted.setVisibility(View.INVISIBLE);
						timeSeted.setVisibility(View.INVISIBLE);
		            }
		            break;
				}
			}
		});
		
		getTaskFromWeb.setOnClickListener(new View.OnClickListener() // Listener for the Get Task Online button
		{		
			public void onClick(View v)
			{
				if (!isOnline())
					return;
				try
				{
					progress.setVisibility(View.VISIBLE);
					TasksFromWeb tasksFromWeb = new TasksFromWeb(); // Creating new AsyncTask
					tasksFromWeb.execute(); // Executing the asynctask
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	// Checking if the user set both date and time for the reminder
	public boolean isDateAndTimeSet()
	{
		if (newDateFragment == null || newTimeFragment == null)
			return false;
		return true;
	}

	// Setting the Data input values that was received from the user
    public void onDateSet(DatePicker view, int year, int month, int day)
    {
    	this.year = year;
    	this.month = month;
    	this.day = day;
    	
        dateSeted.setText("Remider Date Is Set To:\n" + String.valueOf(this.day) + "/" + String.valueOf(this.month + 1) + "/" + String.valueOf(this.year));
    }

	// Setting the Time input values that was received from the user
	public void onTimeSet(TimePicker view, int hourOfDay, int minute)
	{
		this.hour = hourOfDay;
		this.minute = minute;
        
		if (minute < 10)
			timeSeted.setText("Remider Time Is Set To:\n" + String.valueOf(hourOfDay) + ":0" + String.valueOf(minute));
		else
			timeSeted.setText("Remider Time Is Set To:\n" + String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
	}
	
	//Checking if the device has Internet connectivity
	public boolean isOnline()
	{
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected())
	    {
	        return true;
	    }
	    else
	    {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Internet Issue");
            alertDialog.setMessage("It seems that your device is not connected.\nPlease make sure you have internet connectivity.");
            alertDialog.show();
	        return false;
	    }
	}
	
	// Will be called after the user picked a point for alert on map 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Activity.RESULT_OK)
		{
			latitude = data.getDoubleExtra("Lat", -1);
			longitude = data.getDoubleExtra("Lng", -1);
		}
	}

	// Private class for getting an online task
	private class TasksFromWeb extends AsyncTask<URL, Integer, JSONObject>
	{	
		@Override
		protected JSONObject doInBackground(URL... arg0)
		{
			return GetOnlineTasks.getTasks();
		}
		
		@Override
		protected void onPostExecute(JSONObject jsonResponse)
		{
			super.onPostExecute(jsonResponse);
			
			try
			{
				titleField.setText(jsonResponse.getString("topic")); // Setting the title field in the UI
				descriptionField.setText(jsonResponse.getString("description")); // Setting the description field in the UI
				progress.setVisibility(View.INVISIBLE);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		EasyTracker.getInstance().setContext(getBaseContext());
		EasyTracker.getInstance().activityStart(this); // Add this method.
	}

	@Override
	public void onStop()
	{
		super.onStop();
		EasyTracker.getInstance().activityStop(this); // Add this method.
	}
}
