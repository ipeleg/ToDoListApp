package il.ac.shenkar.classproject;

import java.io.*;
import java.net.*;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;

public class TasksFromWeb extends AsyncTask<URL, Integer, JSONObject>
{
	private JSONObject jsonResponse;
	
	@Override
	protected JSONObject doInBackground(URL... arg0)
	{
		try
		{
			HttpURLConnection urlConnection = (HttpURLConnection) arg0[0].openConnection(); // New URL connection to the passed URL
			
			InputStream	in = new BufferedInputStream (urlConnection.getInputStream());	
			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader bufferedReader = new BufferedReader(inReader);
			StringBuilder responseBuilder = new StringBuilder();
			
			for	(String	line=bufferedReader.readLine();	line!=null;	line=bufferedReader.readLine())	// Reading the Lines in the URL
				responseBuilder.append(line);

			String response = responseBuilder.toString(); // Creating String form the response builder
			jsonResponse = new JSONObject(response); // Creating JSON object
			
			return jsonResponse;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
