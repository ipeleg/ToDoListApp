package il.ac.shenkar.classproject.onlinetasks;

import java.io.*;
import java.net.*;
import org.json.*;

public class GetOnlineTasks
{	
	public static JSONObject getTasks()
	{
		JSONObject jsonResponse;
		
		try
		{
			URL url = new URL("http://mobile1-tasks-dispatcher.herokuapp.com/task/random"); // Creating new URL to get tasks from
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); // New URL connection to the passed URL
			
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
		catch (MalformedURLException e)
		{
			e.printStackTrace();
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
