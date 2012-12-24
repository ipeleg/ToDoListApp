package il.ac.shenkar.classproject;

import java.util.Calendar;
import android.app.*;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.*;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment
{	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), (OnDateSetListener) getActivity(), year, month, day);
    }
}
