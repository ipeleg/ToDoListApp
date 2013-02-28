package il.ac.shenkar.classproject;

import java.util.Calendar;
import com.google.analytics.tracking.android.EasyTracker;
import il.ac.shenkar.classproject.alarm.SetReminder;
import il.ac.shenkar.classproject.fragments.DatePickerFragment;
import il.ac.shenkar.classproject.fragments.TimePickerFragment;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.view.*;
import android.widget.*;

public class ShowTaskDetails extends FragmentActivity implements OnDateSetListener, OnTimeSetListener
{
	private Context context;
	
	// Variables for the DatePicker
	private DatePickerFragment newDateFragment = null;
	private int year=-1, month=-1, day=-1;
	
	// Variables for the TimePicker
	private TimePickerFragment newTimeFragment = null;
	private int hour=-1, minute=-1;
	
	private TextView newDateSeted;
	private TextView newTimeSeted;
	
	private SetReminder setReminder;
	private TaskListArray array;
	private TaskListDataBase taskDataBase;
	private Task task;
	
	private TextView reminder;
	private TextView setNewReminder;
	private TextView proximityTitle;
	
	private EditText title;
	private EditText description;
	private EditText dateCreated;
	private EditText showReminder;
	private EditText proximity;
	
	private Button edit;
	private Button setNewTime;
	private Button setNewDate;
	
	private RelativeLayout activityLayout;
	private boolean isEditing;
	private boolean checked;
	
	private ShareActionProvider myShareActionProvider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_task_details); // Setting the UI view with show_task_details
		context = getBaseContext();
		
		final CheckBox checkBox = (CheckBox) findViewById(R.id.set_new_reminder); //Creating the CheckBox
		isEditing = false;
		activityLayout = (RelativeLayout) findViewById(R.id.show_task_details_view);
		title = (EditText) findViewById(R.id.show_title);
		description = (EditText) findViewById(R.id.show_description);
		dateCreated = (EditText) findViewById(R.id.show_date_created);
		showReminder = (EditText) findViewById(R.id.show_reminder);
		proximity = (EditText) findViewById(R.id.show_proximity_alert);
		
		proximityTitle = (TextView) findViewById(R.id.proximity_alert_title);
		newDateSeted = (TextView) findViewById(R.id.new_date_set_view);
		newTimeSeted = (TextView) findViewById(R.id.new_time_set_view);
		setNewReminder = (TextView) findViewById(R.id.set_new_reminder);
		reminder = (TextView) findViewById(R.id.reminder);
		
		edit = (Button) findViewById(R.id.edit_button);
		
		setNewDate = (Button) findViewById(R.id.set_new_date);
		setNewDate.setOnClickListener(new View.OnClickListener() // Setting click listener for setDate button 
		{
			public void onClick(View v)
			{
				newDateFragment = new DatePickerFragment();
				newDateFragment.show(getSupportFragmentManager(), "datePicker"); // Show the user dialog to pick a date
			}
		});
		
		setNewTime = (Button) findViewById(R.id.set_new_time);
		setNewTime.setOnClickListener(new View.OnClickListener() // Setting click listener for setTime button 
		{
			public void onClick(View v)
			{	
				newTimeFragment = new TimePickerFragment();
				newTimeFragment.show(getSupportFragmentManager(), "timePicker"); // Show the user dialog to pick a time
			}
		});
		
		array = TaskListArray.getInstance(getBaseContext());
		taskDataBase = TaskListDataBase.getInstance(getBaseContext()); // Creating the tasks list
		task = new Task();
		
		long id = getIntent().getLongExtra("taskId", -1);
		task = taskDataBase.getTask(id); // Getting the task from the database according to the ID
		
		title.setText(task.getTitle());
		description.setText(task.getDescription());
		dateCreated.setText(task.getCreationDateString());
		
		if (task.hasReminder())
		{
			showReminder.setText(task.getReminderDateString() + " at "+ task.getReminderTimeString());
			showReminder.setVisibility(View.VISIBLE);
			reminder.setVisibility(View.VISIBLE);
		}
		
		if (!task.getLocation().equals(""))
		{
			proximity.setText(task.getLocation());
			proximity.setVisibility(View.VISIBLE);
			proximityTitle.setVisibility(View.VISIBLE);
		}
		
		// ClickListener for the "Edit Task" button
		edit.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (!isEditing) // If the user wants to edit change the background and let the user edit fields
				{
					isEditing = true;
					edit.setBackgroundResource(R.drawable.done_editing_button);
					
					setNewReminder.setVisibility(View.VISIBLE);
					
					title.setEnabled(true);
					title.setTextColor(Color.RED);
					description.setEnabled(true);
					description.setTextColor(Color.RED);
					
					activityLayout.setBackgroundResource(R.drawable.edit_background);
				}
				else // Else revert the background to normal, close the edit mode and update task in database
				{
					if (checked)
					{
						if (!isDateAndTimeSet())
						{
							Toast.makeText(context, "You must set date and time.", Toast.LENGTH_SHORT).show();
							return;
						}
						else
							setTaskReminder();
					}
					
					isEditing = false;
					edit.setBackgroundResource(R.drawable.edit_button);
					title.setEnabled(false);
					description.setEnabled(false);
					
					checkBox.setChecked(false); // Setting the checkbox to unchecked
					setNewReminder.setVisibility(View.INVISIBLE);
					setNewDate.setVisibility(View.INVISIBLE);
					setNewTime.setVisibility(View.INVISIBLE);					
					newDateSeted.setVisibility(View.INVISIBLE);
					newTimeSeted.setVisibility(View.INVISIBLE); // Making all the components invisible
					
					task.setTitle(title.getText().toString()); // Updating task title
					title.setTextColor(Color.BLACK);
					task.setDescription(description.getText().toString()); // Updating task description
					description.setTextColor(Color.BLACK);
					
					array.updateTask(task, task.getId()); // Updating the task in the array + database
					activityLayout.setBackgroundResource(R.drawable.carbon);
				}
				
			}
		});
		
		// ClickListener for the set new reminder CheckBox
		checkBox.setOnClickListener(new View.OnClickListener() // Setting click listener for the check-box for controlling the set's buttons clickability
		{
			public void onClick(View v)
			{
				checked = ((CheckBox) v).isChecked();
				switch(v.getId())
				{
		        case R.id.set_new_reminder:
		            if (checked) // If the CheckBox is checked allow the user to set a reminder, else don't
		            {
		            	setNewDate.setVisibility(View.VISIBLE);
		            	setNewTime.setVisibility(View.VISIBLE);
		            	newDateSeted.setVisibility(View.VISIBLE);
		            	newTimeSeted.setVisibility(View.VISIBLE); // Making all the components visible
		            }
		            else
		            {
		            	setNewDate.setVisibility(View.INVISIBLE);
		            	setNewTime.setVisibility(View.INVISIBLE);
		            	newDateSeted.setVisibility(View.INVISIBLE);
		            	newTimeSeted.setVisibility(View.INVISIBLE); // Making all the components invisible
		            }
		            break;
				}
			}
		});
	}
	
	// Setting a reminder from the user input to the task
	private void setTaskReminder()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, hour, minute, 0); // Setting a new Calendar instance with the user reminder date

		Time reminderTime = new Time(); // Reminder in Time format to store in the object
		reminderTime.set(0, minute, hour, day, month, year); // Setting the reminder to be saved in the Task object
		task.setReminder(reminderTime);
		task.setHasReminder(1); // Setting the boolean flag to indicate that that the task has reminder
		
		setReminder = new SetReminder();
		setReminder.setOneTimeReminder(context, cal, task); // Creating a new reminder to be added to the alarm manager
		
		showReminder.setText(task.getReminderDateString() + " at "+ task.getReminderTimeString());
		showReminder.setVisibility(View.VISIBLE);
		reminder.setVisibility(View.VISIBLE);
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
    	
    	newDateSeted.setText("Remider Date Is Set To:\n" + String.valueOf(this.day) + "/" + String.valueOf(this.month + 1) + "/" + String.valueOf(this.year));
    }

	// Setting the Time input values that was received from the user
	public void onTimeSet(TimePicker view, int hourOfDay, int minute)
	{
		this.hour = hourOfDay;
		this.minute = minute;
        
		if (minute < 10)
			newTimeSeted.setText("Remider Time Is Set To:\n" + String.valueOf(hourOfDay) + ":0" + String.valueOf(minute));
		else
			newTimeSeted.setText("Remider Time Is Set To:\n" + String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	getMenuInflater().inflate(R.menu.sharing_action_bar, menu);
    	
    	MenuItem item = menu.findItem(R.id.menu_item_share);
    	myShareActionProvider = (ShareActionProvider)item.getActionProvider();
    	myShareActionProvider.setShareIntent(createShareIntent());
    	
    	return true;
    }
    
    private Intent createShareIntent()
    {
    	  Intent shareIntent = new Intent(Intent.ACTION_SEND);
          shareIntent.setType("text/plain"); // Setting the type of content
          String message = "Just a small reminder :)\n";
          
          if (task.hasReminder()) // If the reminder has an alert write it with the message
        	  message += "When: " + showReminder.getText().toString() + "\n";
          
          if (!task.getLocation().equals("")) // If the reminder has location write it to the message
        	  message += "Where: " + proximity.getText().toString() + "\n";
        
          message += title.getText().toString() + "\n" + description.getText().toString();
          
          shareIntent.putExtra(Intent.EXTRA_TEXT, message);
          
          return shareIntent;
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
