package il.ac.shenkar.classproject.location;

import il.ac.shenkar.classproject.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CreateGoogleMap extends Activity
{
	static final LatLng TEL_AVIV = new LatLng(32.0833, 34.8000);
	private GoogleMap map;
	private Marker marker;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_fragment_view);
		
		Button setButton = (Button) findViewById(R.id.set);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapview)).getMap();
		marker = map.addMarker(new MarkerOptions()
						.position(TEL_AVIV)
						.title("Tel Aviv")
						.draggable(true)
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));

		// Move the camera instantly to Tel-Aviv with a zoom of 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(TEL_AVIV, 15));

		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		
		setButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LatLng position = marker.getPosition();
				Intent resultIntent = new Intent();
				
				resultIntent.putExtra("Lat", position.latitude);
				resultIntent.putExtra("Lng", position.longitude);
				setResult(Activity.RESULT_OK, resultIntent);
				
				finish();
			}
		});
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		EasyTracker.getInstance().setContext(getBaseContext());
		EasyTracker.getInstance().activityStart(this); // Add this method.
	}

	@Override
	public void onStop()
	{
		super.onStop();
		EasyTracker.getInstance().activityStop(this); // Add this method.
	}
}
