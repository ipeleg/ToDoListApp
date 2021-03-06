package il.ac.shenkar.classproject;

import il.ac.shenkar.classproject.alarm.SetReminder;
import android.app.AlertDialog;
import android.content.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class TaskListAdapter extends BaseAdapter
{
	private TaskListArray taskList;
	private LayoutInflater l_Inflater;
	private Context taskListContext;
	private SetReminder setReminder;

	public TaskListAdapter(Context context)
	{
		taskListContext = context;
		setReminder = new SetReminder();
		taskList = TaskListArray.getInstance(context);
		l_Inflater = LayoutInflater.from(context);
	}

	public int getCount()
	{
		return taskList.getSize();
	}

	public Object getItem(int position)
	{
		return taskList.getTask(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		final int index = position;
		
		if (convertView == null)
		{
			convertView = l_Inflater.inflate(R.layout.task_item_view, null);
			holder = new ViewHolder();
			
			holder.taskTitle = (TextView) convertView.findViewById(R.id.task_title);
			holder.taskDescription = (TextView) convertView.findViewById(R.id.task_description);
			
			holder.isDone = (CheckBox) convertView.findViewById(R.id.is_done);
			holder.delete = (Button) convertView.findViewById(R.id.doneButton);
			
			if (taskList.getTask(index).getIsDone() == 1)
				holder.isDone.setChecked(true);
			else
				holder.isDone.setChecked(false);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
			
			if (taskList.getTask(index).getIsDone() == 1)
				holder.isDone.setChecked(true);
			else
				holder.isDone.setChecked(false);
		}
		
		holder.taskTitle.setText(taskList.getTask(position).getTitle());
		holder.taskDescription.setText(taskList.getTask(position).getDescription());
		
		holder.isDone.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (((CheckBox) v).isChecked())
					taskList.getTask(index).setIsDone(1);
				else
					taskList.getTask(index).setIsDone(0);
				
				taskList.updateTask(taskList.getTask(index), taskList.getTask(index).getId());
			}
		});
		
		// ClickListener for the delete task button 
		holder.delete.setOnClickListener(new OnClickListener() // OnClickListener to delete item from list 
		{
		    public void onClick(View v) 
		    {
		    	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
		    	{
		    	    public void onClick(DialogInterface dialog, int which)
		    	    {
		    	        switch (which)
		    	        {
		    	        case DialogInterface.BUTTON_POSITIVE: // Delete the task if yes is selected
		    	        		
		    	        		if (taskList.getTask(index).hasReminder()) // If the task has a reminder -> cancel it.
		    	        			setReminder.cancelReminder(taskListContext, taskList.getTask(index).getId());
		    	        		
		    	        		if (!taskList.getTask(index).getLocation().equals("")) // If the task has a proximity alert
		    	        				setReminder.cancelProximityAlert(taskListContext, taskList.getTask(index).getId()); // Canceling any proximity alerts
		    	        		
		    	        		taskList.removeTask(index); // Removing the task from the array
		    		    		notifyDataSetChanged(); // Refreshing the ListView
		    	            break;

		    	        case DialogInterface.BUTTON_NEGATIVE: // Do nothing if "no" is selected
		    	            break;
		    	        }
		    	    }
		    	};

		    	AlertDialog.Builder builder = new AlertDialog.Builder(taskListContext);
		    	builder.setMessage("Are you sure the task is done?").setPositiveButton("Yes", dialogClickListener)
		    	    .setNegativeButton("No", dialogClickListener).show();
		    }
		});
		
		View rowView = convertView;
		rowView.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View arg0)
			{
				Intent intent = new Intent(taskListContext, ShowTaskDetails.class);
				intent.putExtra("taskId",taskList.getTask(index).getId());
				taskListContext.startActivity(intent);
			}
		});
		
		return convertView;
	}
	
	static class ViewHolder
	{
		TextView taskTitle;
		TextView taskDescription;
		Button delete = null;
		CheckBox isDone = null;
	}
}
