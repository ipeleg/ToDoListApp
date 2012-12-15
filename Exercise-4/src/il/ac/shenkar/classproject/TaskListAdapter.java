package il.ac.shenkar.classproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class TaskListAdapter extends BaseAdapter
{
	private TaskListArray taskList;
	private LayoutInflater l_Inflater;
	private Context TaskListContext;

	public TaskListAdapter(Context context)
	{
		TaskListContext = context;
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
			holder.taskID = (TextView) convertView.findViewById(R.id.task_id);
			
			holder.done = (Button) convertView.findViewById(R.id.doneButton);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.taskTitle.setText(taskList.getTask(position).getTitle());
		holder.taskDescription.setText(taskList.getTask(position).getDescription());
		holder.taskID.setText(String.valueOf(taskList.getTask(position).getId()));
		
		holder.done.setOnClickListener(new OnClickListener() // OnClickListener to delete item from list 
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
		    	        		taskList.removeTask(index);
		    		    		notifyDataSetChanged();
		    	            break;

		    	        case DialogInterface.BUTTON_NEGATIVE: // Do nothing if no is selected
		    	            break;
		    	        }
		    	    }
		    	};

		    	AlertDialog.Builder builder = new AlertDialog.Builder(TaskListContext);
		    	builder.setMessage("Are you sure the task is done?").setPositiveButton("Yes", dialogClickListener)
		    	    .setNegativeButton("No", dialogClickListener).show();
		    }
		});
		
		return convertView;
	}
	
	static class ViewHolder
	{
		TextView taskTitle;
		TextView taskDescription;
		TextView taskID;
		Button done = null;
	}
}
