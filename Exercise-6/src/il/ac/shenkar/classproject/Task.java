package il.ac.shenkar.classproject;

import android.text.format.Time;

public class Task
{
	private String title; // Task title
	private String description; // Task description
	private Time creationDate; // Date when the task was created
	private Time reminder;
	private long Id;
	private int hasReminder;
	private enum Dates {CREATION_DATE, REMINDER_DATE};

	public Task()
	{
		this.title = "";
		this.description = "";
		creationDate = new Time();
		this.reminder = new Time();
		this.reminder.set(0, 0, 0, 0, 0, 0);
		this.hasReminder = 0;
	}
	
	public Task(String title)
	{
		this.title = title;
		this.creationDate = new Time();
		this.creationDate.setToNow();
		this.reminder = new Time();
		this.reminder.set(0, 0, 0, 0, 0, 0);
		this.hasReminder = 0;
	}

	public void setDateFromString(String date, String dates)
	{
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4, 6)) - 1;
		int day = Integer.parseInt(date.substring(6, 8));
		
		int hour = Integer.parseInt(date.substring(9, 11));
		int minute = Integer.parseInt(date.substring(11, 13));
		int second = Integer.parseInt(date.substring(13, 15));
		
		Dates which = Dates.valueOf(dates);
		switch (which)
		{
			case CREATION_DATE:
				this.creationDate.set(second, minute, hour, day, month, year);
				break;
				
			case REMINDER_DATE:
				this.reminder.set(second, minute, hour, day, month, year);
				break;
		}
	}

	public long getId()
	{
		return Id;
	}

	public void setId(long id)
	{
		Id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Time getCrationDate()
	{
		return creationDate;
	}
	
	public Time getReminder()
	{
		return reminder;
	}

	public void setReminder(Time reminder)
	{
		this.reminder = reminder;
	}
	
	public boolean hasReminder()
	{
		if (hasReminder == 0) // If the first char in the toString is 0 there is no reminder for this task
			return false;
		return true;
	}
	
	public String getCreationDateString()
	{	
		return String.valueOf(creationDate.monthDay + "/" +  (creationDate.month+1) + "/" + creationDate.year);
	}
	
	public String getReminderDateString()
	{
		return String.valueOf(reminder.monthDay + "/" +  (reminder.month+1) + "/" + reminder.year);
	}
	
	public int getHasReminder()
	{
		return hasReminder;
	}

	public void setHasReminder(int hasReminder)
	{
		this.hasReminder = hasReminder;
	}
	
	public String getReminderTimeString()
	{
		if (reminder.minute < 10)
			return String.valueOf(reminder.hour + ":0" +  reminder.minute);
		else
			return String.valueOf(reminder.hour + ":" +  reminder.minute);
	}
	
	public String getFullDateString(Time time)
	{
		return time.toString().substring(0, 15); // Returns only the required fields from the toString()
														 // YYYYMMDDTHHMMSS
	}
}
