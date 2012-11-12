package il.ac.shenkar.classproject;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TaskListAdapter extends BaseAdapter
{
	private static ArrayList<Task> taskList;
	private LayoutInflater l_Inflater;

	public TaskListAdapter(Context context, ArrayList<Task> tasks)
	{
		taskList = tasks;
		l_Inflater = LayoutInflater.from(context);
	}

	public int getCount()
	{
		return taskList.size();
	}

	public Object getItem(int position)
	{
		return taskList.get(position);
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
		
		holder.taskName.setText(taskList.get(position).getName());
		
		return convertView;
	}
	
	static class ViewHolder
	{
		TextView taskName;
	}

}
