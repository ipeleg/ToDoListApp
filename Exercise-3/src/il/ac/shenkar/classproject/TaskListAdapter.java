package il.ac.shenkar.classproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
		
		if (convertView == null)
		{
			convertView = l_Inflater.inflate(R.layout.task_item_view, null);
			holder = new ViewHolder();
			holder.taskName = (TextView) convertView.findViewById(R.id.task_name_view);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.taskName.setText(taskList.getTask(position).getName());
		
		return convertView;
	}
	
	static class ViewHolder
	{
		TextView taskName;
	}
	
	public Task getViewTag(View v)
	{
		return (Task) v.getParent();
	}

}
