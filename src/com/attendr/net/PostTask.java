package com.attendr.net;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An AsyncTask implementation for performing POSTs on the Hypothetical REST APIs.
 */
public class PostTask extends AsyncTask<String, String, String>{
    private RestTaskCallback mCallback;
    private String mRequestBody, type, mRestUrl;

    /**
     * Creates a new instance of PostTask with the specified URL, callback, and
     * request body.
     * 
     * @param restUrl The URL for the REST API.
     * @param callback The callback to be invoked when the HTTP request
     *            completes.
     * @param requestBody The body of the POST request.
     * 
     */
    public PostTask(String restUrl, String requestBody, String type, RestTaskCallback callback){
        this.mRestUrl = restUrl;
        this.mRequestBody = requestBody;
        this.mCallback = callback;
        this.type = type;
    }

    @Override
    protected String doInBackground(String... arg0) {
    	String response = null;
    	HttpURLConnection urlConnection = null;
    	try {
			URL url = new URL(mRestUrl);
	    	urlConnection = (HttpURLConnection) url.openConnection();
	    	urlConnection.addRequestProperty("Content-Type", "application/json");
	    	urlConnection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
	    	urlConnection.setRequestProperty("Accept","*/*");
	    	
	    	urlConnection.setDoOutput(true);
	    	
	    	OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
	    	
	    	out.write(mRequestBody);
	    	out.close();
	    	
	    	InputStream in = new BufferedInputStream(urlConnection.getInputStream());
	    	response = Utils.convertStreamToString(in);
	    	in.close();
	    	//Log.v("Server Response:", response);
	    	urlConnection.disconnect();
	    
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(type,Utils.convertStreamToString(urlConnection.getErrorStream()));
		}
		return response;
    }
    
    @Override
    protected void onPostExecute(String result) {
        mCallback.onTaskComplete(result);
        super.onPostExecute(result);
    }
}