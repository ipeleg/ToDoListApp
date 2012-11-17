package il.ac.shenkar.classproject;

import android.content.Context;
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

	public TaskListAdapter(Context context)
	{
		taskList = TaskListArray.getInstance();
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
			holder.taskName = (TextView) convertView.findViewById(R.id.task_name_view);
			
			holder.done = (Button) convertView.findViewById(R.id.doneButton);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.taskName.setText(taskList.getTask(position).getName());
		
		holder.done.setOnClickListener(new OnClickListener() // OnClickListener to delete item from list 
		{
		    public void onClick(View v) 
		    {
		    	taskList.removeTask(index);
		        notifyDataSetChanged();
		    }
		});
		
		return convertView;
	}
	
	static class ViewHolder
	{
		TextView taskName;
		Button done = null;
	}
}
