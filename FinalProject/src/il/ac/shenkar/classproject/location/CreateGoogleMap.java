package il.ac.shenkar.classproject.location;

import java.io.IOException;
import java.util.List;
import il.ac.shenkar.classproject.R;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
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
	private MapFragment mapFragment;
	private Marker marker;
	private LatLng position;
	private List<Address> address;
	
	private ImageView addressTitle;
	private EditText addressField;
	
	private Button setButton;
	private Button dropPinButton;
	
	private boolean fromAddress; // If true get LatLng from the address field

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_fragment_view);
		
		addressTitle = (ImageView) findViewById(R.id.enter_address_title);
		addressField = (EditText) findViewById(R.id.address_field);
		setButton = (Button) findViewById(R.id.set);
		dropPinButton = (Button) findViewById(R.id.drop_pin);
		
		fromAddress = false;
		mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapview);
		map = mapFragment.getMap();
		marker = map.addMarker(new MarkerOptions()
						.position(TEL_AVIV)
						.visible(false)
						.title("Alert Me Here")
						.draggable(true)
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));

		// Move the camera instantly to Tel-Aviv with a zoom of 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(TEL_AVIV, 15));

		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		
		dropPinButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				marker.setPosition(map.getCameraPosition().target);
				marker.setVisible(true);
			}
		});
		
		setButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent resultIntent = new Intent(); // Creating new result intent to hold results for the calling activity
				Geocoder gc = new Geocoder(getBaseContext());
				
				if (fromAddress) // If the user want to get the point from an address
				{
					try
					{
						address = gc.getFromLocationName(addressField.getText().toString(), 1); // Getting the LatLng from the address field
						
						resultIntent.putExtra("Lat", address.get(0).getLatitude()); // Adding the latitude to the IntentResult
						resultIntent.putExtra("Lng", address.get(0).getLongitude()); // Adding the longitude to the IntentResult
						resultIntent.putExtra("location", addressField.getText().toString()); // Adding the address string to the IntentResult
						
						setResult(Activity.RESULT_OK, resultIntent); // Setting the results for the calling activity
						Toast.makeText(getApplicationContext(), "Location was successfully retrieved" , Toast.LENGTH_SHORT).show();
					}
					catch (IOException e)
					{
						Toast.makeText(getApplicationContext(), "Location could not be found, Sorry.", Toast.LENGTH_SHORT).show();
					}
				}
				else // Else get the point from the map
				{
					position = marker.getPosition(); // Getting the LatLng from the marker on map
					try
					{
						address = gc.getFromLocation(position.latitude, position.longitude, 1);
						
						String addressString = address.get(0).getAddressLine(0) + " " + 
								address.get(0).getAddressLine(1) + " " +
								address.get(0).getAddressLine(2);
			
						resultIntent.putExtra("Lat", position.latitude); // Adding the latitude to the IntentResult
						resultIntent.putExtra("Lng", position.longitude); // Adding the longitude to the IntentResult
						resultIntent.putExtra("location", addressString); // Adding the address string to the IntentResult
			
						setResult(Activity.RESULT_OK, resultIntent); // Setting the results for the calling activity
						
						Toast.makeText(getApplicationContext(), "Location was successfully retrieved" , Toast.LENGTH_SHORT).show();
					}
					catch (IOException e)
					{
						Toast.makeText(getApplicationContext(), "Location could not be found, Sorry.", Toast.LENGTH_SHORT).show();
					}
				}
				finish();
			}
		});
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	getMenuInflater().inflate(R.menu.map_action_bar, menu);
    	
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch(item.getItemId())
    	{
    		case R.id.point_from_address:
    			// Switching to address mode
    			mapFragment.getView().setVisibility(View.INVISIBLE);
    			dropPinButton.setVisibility(View.INVISIBLE);
    			addressTitle.setVisibility(View.VISIBLE);
    			addressField.setVisibility(View.VISIBLE);
    			fromAddress = true;
    			
    			mapFragment.getActivity().invalidateOptionsMenu(); // Refreshing the activity view
    			break;
    		
    		case R.id.point_from_map:
    			// Switching to map mode
    			mapFragment.getView().setVisibility(View.VISIBLE);
    			dropPinButton.setVisibility(View.VISIBLE);
    			addressTitle.setVisibility(View.INVISIBLE);
    			addressField.setVisibility(View.INVISIBLE);
    			fromAddress = false;
    			
    			mapFragment.getActivity().invalidateOptionsMenu(); // Refreshing the activity view
    			break;
    	}
    	return true;
    }
	
    // Google Analytics
	@Override
	public void onStart()
	{
		super.onStart();
		EasyTracker.getInstance().setContext(getBaseContext());
		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	public void onStop()
	{
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}
}
