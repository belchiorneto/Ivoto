package com.lojapro.alertas;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.Xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.lojapro.can.SplashScreen;
import com.lojapro.can.Eleitor;
import com.lojapro.can.R;

/**
 * This {@code IntentService} does the app's actual work.
 * {@code SampleAlarmReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class SampleSchedulingService extends IntentService {
    public SampleSchedulingService() {
        super("SchedulingService");
    }
    SharedPreferences sharedPref;
    
    public static final String TAG = "Scheduling Demo";
    // An ID used to post the notification.
    public static int NOTIFICATION_ID = 1;
    public static String idnotificacao = "";
    public static String msg = "";
    public static String URL = "http://ivoto.com.br/data_eleicoes/gerencianotificacoes.php";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    @Override
    protected void onHandleIntent(Intent intent) {
    	sharedPref = getSharedPreferences(
       	      "com.lojapro.can", Context.MODE_PRIVATE);
    	int idcidade =  sharedPref.getInt("cidade", 0);
    	String androidid = sharedPref.getString("androidid", "0");
    	URL += "?idandroid="+androidid+"&cidade="+idcidade;
        String urlString = URL;
        String result ="";
        try {
            result = loadFromNetwork(urlString);
            Log.i(TAG, "Resultado da web: " + result);
        } catch (IOException e) {
            Log.i(TAG, getString(R.string.connection_error));
        }
    
        String[] result1 = result.split("\\|", -1);
        
        try {
        	if(result1.length > 2){
	        	System.out.println("Tentando montar notificacao");
	        	//NOTIFICATION_ID = Integer.parseInt(result1[0].toString().trim());
	        	msg = result1[1];
	        	String cidade = result1[2];
	        	idnotificacao = result1[0];
	        	System.out.println("enviando notificacao: " + idnotificacao + "/" + cidade +"/"+ msg);
	        	sendNotification(msg, cidade);
	        }else{
	        	System.out.println("Não notificar");
	        }
        } catch(NumberFormatException nfe) {
        	Log.i(TAG, "Erro" + nfe);
        } 
        
        SampleAlarmReceiver.completeWakefulIntent(intent);
       
    }
    
    // Post a notification indicating whether a doodle was found.
    private void sendNotification(String msg, String cidade) {
        mNotificationManager = (NotificationManager)
               this.getSystemService(Context.NOTIFICATION_SERVICE);
    
        final Intent intent = new Intent(this, Eleitor.class);
        
        intent.putExtra("ref", "notif");
        intent.putExtra("idnotif", idnotificacao);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
        		intent, 0);

     // Constructs the Builder object.
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(cidade)
                .setContentText(msg)
                .setAutoCancel(true)
                /*
                 * Sets the big view "big text" style and supplies the
                 * text (the user's reminder message) that will be displayed
                 * in the detail area of the expanded notification.
                 * These calls are ignored by the support library for
                 * pre-4.1 devices.
                 */
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg));
        
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
 
//
// The methods below this line fetch content from the specified URL and return the
// content as a string.
//
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
