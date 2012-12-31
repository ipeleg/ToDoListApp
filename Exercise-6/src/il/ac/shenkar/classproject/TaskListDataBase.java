package il.ac.shenkar.classproject;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskListDataBase extends SQLiteOpenHelper
{
	private static TaskListDataBase instance;
	
	// Table column names
	public static final String KEY_ID = "id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_CREATION_DATE = "creationDate";
	
	// DataBase name
	public static final String DATABASE_NAME = "tasksDataBase";
	
	// Table name
	public static final String TABLE_TASKS = "tasksTable";
	
	//DataBase Version
	public static final int DATABASE_VERSION = 12;

	public TaskListDataBase(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// Singleton implement
	public static TaskListDataBase getInstance(Context context)
	{
		if (instance == null)
			instance = new TaskListDataBase(context);
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASKS +
				"(" + KEY_ID + " INTEGER PRIMARY KEY," +
				KEY_TITLE + " TEXT," +
				KEY_DESCRIPTION + " TEXT," +
				KEY_CREATION_DATE + " TEXT" + ")";
		
		db.execSQL(CREATE_TASK_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
 
        // Create tables again
        onCreate(db);
	}

	// Add new Task and returning the ROWID given from the SQL
	public long addTask(Task newTask)
	{
		long rowID;
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
	    values.put(KEY_TITLE, newTask.getTitle()); // Setting the title
	    values.put(KEY_DESCRIPTION, newTask.getDescription()); // Setting the description
	    values.put(KEY_CREATION_DATE, newTask.getCreationFullDateString()); // Setting the description
	    
	    // Adding the new row to DataBase
	    rowID = db.insert(TABLE_TASKS, null, values);
	    db.close(); // Closing database connection
	    
	    return rowID;
	}

	// Get specific task
	public Task getTask(long id)
	{
	    SQLiteDatabase db = this.getReadableDatabase();
	    
	    Cursor cursor = db.query(TABLE_TASKS, new String[] { KEY_ID,
	    		KEY_TITLE, KEY_DESCRIPTION, KEY_CREATION_DATE }, KEY_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    Task task = new Task();
        task.setId(Long.parseLong(cursor.getString(0)));
        task.setTitle(cursor.getString(1));
        task.setDescription(cursor.getString(2));
        task.setDateFromString(cursor.getString(3));

	    db.close();
	    return task;
	}

	// Get all Tasks
	public ArrayList<Task> getAllTasks()
	{
		ArrayList<Task> taskList = new ArrayList<Task>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_TASKS;
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToLast()) {
	        do {
	            Task task = new Task();
	            task.setId(Integer.parseInt(cursor.getString(0)));
	            task.setTitle(cursor.getString(1));
	            task.setDescription(cursor.getString(2));
	            task.setDateFromString(cursor.getString(3));
	            
	            // Adding task to list
	            taskList.add(task);
	        } while (cursor.moveToPrevious());
	    }
	 
	    db.close();
	    return taskList;
	}

	// Get number of Tasks in table
	public int getTasksCount()
	{
		String countQuery = "SELECT  * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        db.close();
        return cursor.getCount();
	}

	// Update the given Task
	public int updateTask (Task taskToUpdate)
	{
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    ContentValues values = new ContentValues();
	    values.put(KEY_TITLE, taskToUpdate.getTitle());
	    values.put(KEY_DESCRIPTION, taskToUpdate.getDescription());
	    values.put(KEY_CREATION_DATE, taskToUpdate.getCreationFullDateString());
	 
	    // updating row
	    return db.update(TABLE_TASKS, values, KEY_ID + " = ?", new String[] { String.valueOf(taskToUpdate.getId()) });
	}

	// Delete the given Task
	public void deleteTask (Task task2Del)
	{
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_TASKS, KEY_ID + " = ?", new String[] { String.valueOf(task2Del.getId()) });
	    db.close();
	}
}
