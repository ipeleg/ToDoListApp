package il.ac.shenkar.classproject.location;

import java.io.IOException;
import java.util.List;
import android.content.Context;
import android.location.*;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class CurrentLocationListener implements LocationListener
{
	private Context context;
	
	public CurrentLocationListener(Context context)
	{
		this.context = context;
	}
	
	public void onLocationChanged(Location loc)
	{
		List<Address> address = null;
		double latitude = loc.getLatitude();
		double longitude = loc.getLongitude();

		Geocoder gc = new Geocoder(context);
		try
		{
			address = gc.getFromLocation(latitude, longitude, 10);
			Log.i("TaskListActivity", "Hello");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		String msg = address.get(0).getAddressLine(0) + "\n"
				+ address.get(0).getAddressLine(1) + "\n"
				+ address.get(0).getAddressLine(2);

		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public void onProviderDisabled(String provider)
	{
		Toast.makeText(context, "GPS Disabled", Toast.LENGTH_SHORT).show();
	}

	public void onProviderEnabled(String provider)
	{
		Toast.makeText(context, "GPS Enabled", Toast.LENGTH_SHORT).show();
	}

	public void onStatusChanged(String provider, int status, Bundle extras)
	{

	}
}
