package il.ac.shenkar.classproject;

import android.text.format.Time;

public class Task
{
	private String title; // Task title
	private String description; // Task description
	private Time creationDate; // Date when the task was created
	private long Id;

	public Task()
	{
		this.title = "";
		this.description = "";
		creationDate = new Time();
	}
	
	public Task(String title)
	{
		this.title = title;
		this.creationDate = new Time();
		this.creationDate.setToNow();
	}
	
	public Task(long id, String title, String description, String date)
	{
		super();
		this.title = title;
		this.description = description;
		setDateFromString(date);
		Id = id;
	}

	public void setDateFromString(String date)
	{
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4, 6));
		int day = Integer.parseInt(date.substring(6, 8));
		
		int hour = Integer.parseInt(date.substring(9, 11));
		int minute = Integer.parseInt(date.substring(11, 13));
		int second = Integer.parseInt(date.substring(13, 15));
		
		this.creationDate.set(second, minute, hour, day, month-1, year);
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
	
	public String getCreationDateString()
	{
		String date = getCreationFullDateString();
		
		return date.substring(6, 8) + "/" + date.substring(4, 6) + "/" + date.substring(0, 4);
	}
	
	public String getCreationFullDateString()
	{
		return creationDate.toString().substring(0, 15); // Returns only the required fields from the toString()
														 // YYYYMMDDTHHMMSS
	}
}
