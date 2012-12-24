package il.ac.shenkar.classproject;

import java.util.Calendar;
import android.app.*;
import android.app.TimePickerDialog.OnTimeSetListener;
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
		return new TimePickerDialog(getActivity(), (OnTimeSetListener) getActivity(), hour, minute, DateFormat.is24HourFormat(getActivity()));
	}
}
