package il.ac.shenkar.classproject;

import java.util.Calendar;
import android.app.*;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

public class TimePickerFragment extends DialogFragment
{

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		final TimePickerDialog timePicker = new TimePickerDialog(getActivity(), (OnTimeSetListener) getActivity(), hour, minute, DateFormat.is24HourFormat(getActivity()));
		
        timePicker.setCancelable(true);
		timePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int which)
			 {
				 if (which == DialogInterface.BUTTON_NEGATIVE)
				        timePicker.cancel();
			 }
		});
        
        return timePicker;
	}
}
