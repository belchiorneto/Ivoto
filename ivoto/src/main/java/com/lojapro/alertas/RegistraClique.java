package com.lojapro.alertas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.lojapro.can.Eleitor;
import com.lojapro.can.R;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class RegistraClique extends AsyncTask {

	SharedPreferences sharedPref;
    public static String URL = "http://ivoto.com.br/data_eleicoes/registraclique.php";
    Context ctx;
    String idnotif;
    public RegistraClique(Context ctx, String idnotif) {
    	this.ctx = ctx;
    	this.idnotif = idnotif;
	}
	
    @Override
	protected Object doInBackground(Object... arg0) {
    	sharedPref = ctx.getSharedPreferences(
       	      "com.lojapro.can", Context.MODE_PRIVATE);
    	int idcidade =  sharedPref.getInt("cidade", 0);
    	String androidid = sharedPref.getString("androidid", "0");
    	URL += "?idandroid="+androidid+"&cidade="+idcidade+"&idnotif="+idnotif;
    	// BEGIN_INCLUDE(service_onhandle)
        // The URL from which to fetch content.
        String urlString = URL;
      
        String result ="";
        
        // Try to connect to the Google homepage and download content.
        try {
            result = loadFromNetwork(urlString);
      
        } catch (IOException e) {
      	  System.out.println("Executando registra clique, erro: " + e);
        }
    
        // If the app finds the string "doodle" in the Google home page content, it
        // indicates the presence of a doodle. Post a "Doodle Alert" notification.
        
        String[] result1 = result.split("\\|", -1);
		return null;
	}
    /** Given a URL string, initiate a fetch operation. */
    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str ="";
      
        try {
            stream = downloadUrl(urlString);
            str = readIt(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }      
        }
        return str;
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
    
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

    /** 
     * Reads an InputStream and converts it to a String.
     * @param stream InputStream containing HTML from www.google.com.
     * @return String version of InputStream.
     * @throws IOException
     */
    private String readIt(InputStream stream) throws IOException {
      
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        for(String line = reader.readLine(); line != null; line = reader.readLine()) 
            builder.append(line);
        reader.close();
        return builder.toString();
    }
	
    
    
}
