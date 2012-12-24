package il.ac.shenkar.classproject;

import il.ac.shenkar.alarm.SetReminder;

import java.io.Serializable;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.os.*;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.*;

@SuppressLint("HandlerLeak")
public class CreateTaskActivity extends FragmentActivity
{
	// Variables for the DatePicker
	private Bundle dateBundle;
	private DatePickerFragment newDateFragment = null;
	private int year=-1, month=-1, day=-1;
	
	// Variables for the TimePicker
	private Bundle timeBundle;
	private TimePickerFragment newTimeFragment = null;
	private int hour=-1, minute=-1;
	
	private static TextView dateSeted;
	private static TextView timeSeted;
	private boolean checked;
	private TaskListDataBase taskDataBase;
	private Button setDate;
	private Button setTime;
	static final int DATE_DIALOG_ID = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_task_activity); // Setting the UI view with create_task_list
		
		taskDataBase = TaskListDataBase.getInstance(getApplicationContext());
		dateSeted = (TextView) findViewById(R.id.date_set_view);
		timeSeted = (TextView) findViewById(R.id.time_set_view);
		
		Button createActivityButton = (Button) findViewById(R.id.create_button); //Creating the 'create' button
		createActivityButton.setOnClickListener(new View.OnClickListener() // Setting click listener for addTask button 
		{
			public void onClick(View v)
			{
				long rowID;
				TaskListArray array = TaskListArray.getInstance(getApplicationContext());
				
				EditText titleField = (EditText) findViewById(R.id.enter_task_title); // Getting the task title
				Task newTask= new Task (titleField.getText().toString()); // Creating new task with that title
				
				EditText descriptionField = (EditText) findViewById(R.id.enter_task_description); // Getting the task description
				newTask.setDescription(descriptionField.getText().toString()); // Setting the task description
				
				if (checked)
				{
					if (!dateAndTimeIsSet())
						Toast.makeText(getApplicationContext(), "Please Set Date And Time For The Reminder", Toast.LENGTH_SHORT).show();
					else
					{
						dateBundle = newDateFragment.getArguments();
						day = dateBundle.getInt("set_day");
						month = dateBundle.getInt("set_month");
						year = dateBundle.getInt("set_year"); // Getting the date set by the user

						timeBundle = newTimeFragment.getArguments();
						hour = timeBundle.getInt("set_hour");
						minute = timeBundle.getInt("set_minute"); // Getting the time set by the user
					
						Calendar cal = Calendar.getInstance();
						cal.set(year, month, day, hour, minute, 0); // Setting a new Calendar instance with the user reminder date
					
						rowID = taskDataBase.addTask(newTask); // Adding to DataBase
						newTask.setId(rowID);
						array.addTask(newTask);
						
						new SetReminder(getApplicationContext(), cal, newTask); // Creating a new reminder to be added to the alarm manager
					
						finish();
					}
				}
				else
				{
					rowID = taskDataBase.addTask(newTask); // Adding to DataBase
					newTask.setId(rowID);
					array.addTask(newTask);
				
					finish();
				}
			}
		});
		
		// Set Date Button
		setDate = (Button) findViewById(R.id.set_date); //Creating the 'Set Date' button
		setDate.setOnClickListener(new View.OnClickListener() // Setting click listener for setDate button 
		{
			public void onClick(View v)
			{
                dateBundle = new Bundle(); // Creating new bundle for the dialog
 
                dateBundle.putInt("set_day", day); // Mapping the day variable in the bundle 
                dateBundle.putInt("set_month", month); // Mapping the month variable in the bundle 
                dateBundle.putInt("set_year", year); // Mapping the year variable in the bundle 
                
				newDateFragment = new DatePickerFragment();
				newDateFragment.setArguments(dateBundle);
				newDateFragment.show(getSupportFragmentManager(), "datePicker"); // Show the user dialog to pick a date
			}
		});
		
		// Set Time button
		setTime = (Button) findViewById(R.id.set_time); //Creating the 'Set Time' button
		setTime.setOnClickListener(new View.OnClickListener() // Setting click listener for setTime button 
		{
			public void onClick(View v)
			{
				timeBundle = new Bundle(); // Creating new bundle for the dialog
				
				timeBundle.putInt("set_hour", hour); // Mapping the hour variable in the bundle 
				timeBundle.putInt("set_minute", minute); // Mapping the minute variable in the bundle 
				
				newTimeFragment = new TimePickerFragment();
				newTimeFragment.setArguments(timeBundle);
				newTimeFragment.show(getSupportFragmentManager(), "timePicker"); // Show the user dialog to pick a time
			}
		});
		
		final CheckBox checkBox = (CheckBox) findViewById(R.id.set_reminder); //Creating the CheckBox
		checkBox.setOnClickListener(new View.OnClickListener() // Setting click listener for the check-box for controlling the set's buttons clickability
		{
			public void onClick(View v)
			{
				checked = ((CheckBox) v).isChecked();
				switch(v.getId())
				{
		        case R.id.set_reminder:
		            if (checked) // If the CheckBox is checked allows the user to set a reminder, else don't
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
	}
	
	public boolean dateAndTimeIsSet() // Checking if the user set both date and time for the reminder
	{
		if (newDateFragment == null || newTimeFragment == null)
			return false;
		return true;
	}
	
	public static void setDate(String date)
	{
		dateSeted.setText(date);
	}
	
	public static void setTime(String time)
	{
		timeSeted.setText(time);
	}
}
