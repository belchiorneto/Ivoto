package com.lojapro.can;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.lojapro.can.R;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class ListaCandidatos extends Activity {
	private ShareActionProvider mShareActionProvider; 
	public static final String WIFI = "Wi-Fi";
	    public static final String ANY = "Any";
	   
	    // Whether there is a Wi-Fi connection.
	    private static boolean wifiConnected = false;
	    // Whether there is a mobile connection.
	    private static boolean mobileConnected = false;
	    // Whether the display should be refreshed.
	    public static boolean refreshDisplay = true;

	    // The user's current network preference setting.
	    public static String sPref = null;

	    // The BroadcastReceiver that tracks network connectivity changes.
	    private NetworkReceiver receiver = new NetworkReceiver();


	// All static variables
	static final String URL = "http://ivoto.com.br/data_eleicoes/ws.php";
	static final String URL_SEND = "http://ivoto.com.br/data_eleicoes/send.php";
	
	// XML node keys
	static final String KEY_SONG = "song"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_IDUSUARIO = "idusuario";
	static final String KEY_TITLE = "title";
	static final String KEY_ARTIST = "artist";
	static final String KEY_DURATION = "duration";
	static final String KEY_INDICACAO = "ind";
	static final String KEY_STATUSINDICADO = "statusind";
	static final String KEY_THUMB_URL = "thumb_url";
	
	ListView list;
    static LazyAdapter adapter;
	XMLParser parser;
	NodeList nl;
	//XMLParser asyncTask = new XMLParser();
	public static ArrayList<HashMap<String, String>> songsList;
	ListaRegistros lista;
	public static int idcidade = 1;
	static Intent itent;
	private ProgressDialog progressDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_candidatos);
		// Register BroadcastReceiver to track connection changes.
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
        
		itent = getIntent();
		if(itent.getExtras().getInt("position") != 0){
			idcidade = itent.getExtras().getInt("position");
		 }
		
	}

	// Refreshes the display if the network connection and the
    // pref settings allow it.
    @Override
    public void onStart() {
        super.onStart();

        // Gets the user's network preference settings
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Retrieves a string value for the preferences. The second parameter
        // is the default value to use if a preference value is not found.
        sPref = sharedPrefs.getString("listPref", "Wi-Fi");

        updateConnectedFlags();

        // Only loads the page if refreshDisplay is true. Otherwise, keeps previous
        // display. For example, if the user has set "Wi-Fi only" in prefs and the
        // device loses its Wi-Fi connection midway through the user using the app,
        // you don't want to refresh the display--this would force the display of
        // an error page instead of stackoverflow.com content.
        if (refreshDisplay) {
            loadPage();
        }
    }
    
	@Override
	protected void onResume() {
	      super.onResume();
	      if(progressDialog != null){  
	      if(progressDialog.isShowing()){
				progressDialog.dismiss();
			}
	      }
	 }
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.principal, menu);
	        /*
	        SearchManager searchManager =
	                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	        SearchView searchView =
	                (SearchView) menu.findItem(R.id.buscapessoal).getActionView();
	        searchView.setSearchableInfo(
	                searchManager.getSearchableInfo(getComponentName()));
	        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
	            @Override 
	            public boolean onQueryTextChange(String newQueryText){
	              if (TextUtils.isEmpty(newQueryText.toString())) {
	            	  adapter.getFilter().filter(newQueryText.toString());
	              } else {
	            	  adapter.getFilter().filter(newQueryText.toString());
	              }
	              return true;
	            }
	            @Override public boolean onQueryTextSubmit(String query){
	              return false;
	            }
	          }
	        );
	        */
	        return true;
	    }
	// Call to update the share intent
	 private void setShareIntent(Intent shareIntent) {
	     if (mShareActionProvider != null) {
	         mShareActionProvider.setShareIntent(shareIntent);
	     }
	 }
	
	void processFinish(String output){
	     //this you will received result fired from async class of onPostExecute(result) method.
		
	}

	/**
    *
    * This BroadcastReceiver intercepts the android.net.ConnectivityManager.CONNECTIVITY_ACTION,
    * which indicates a connection change. It checks whether the type is TYPE_WIFI.
    * If it is, it checks whether Wi-Fi is connected and sets the wifiConnected flag in the
    * main activity accordingly.
    *
    */
   public class NetworkReceiver extends BroadcastReceiver {

       @Override
       public void onReceive(Context context, Intent intent) {
           ConnectivityManager connMgr =
                   (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
           NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
          
           // Checks the user prefs and the network connection. Based on the result, decides
           // whether
           // to refresh the display or keep the current display.
           // If the userpref is Wi-Fi only, checks to see if the device has a Wi-Fi connection.
           if (networkInfo != null) {
        	   // If device has its Wi-Fi connection, sets refreshDisplay
               // to true. This causes the display to be refreshed when the user
               // returns to the app.
               refreshDisplay = true;
               //Toast.makeText(context, R.string.wifi_connected, Toast.LENGTH_SHORT).show();

               // If the setting is ANY network and there is a network connection
               // (which by process of elimination would be mobile), sets refreshDisplay to true.
           } else {
        	   refreshDisplay = false;
               Toast.makeText(context, R.string.lost_connection, Toast.LENGTH_SHORT).show();
               startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
              
           }
       }
   }
	
   // Checks the network connection and sets the wifiConnected and mobileConnected
   // variables accordingly.
   private void updateConnectedFlags() {
       ConnectivityManager connMgr =
               (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

       NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
       if (activeInfo != null && activeInfo.isConnected()) {
           wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
           mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
       } else {
           wifiConnected = false;
           mobileConnected = false;
       }
   }
   private void loadPage() {
       
	   if (wifiConnected || mobileConnected) {
           // AsyncTask subclass
		   progressDialog = new ProgressDialog(ListaCandidatos.this);
		   if(progressDialog.isShowing()){
			   progressDialog.dismiss();
		   }
		  lista = new ListaRegistros(this);
    	  songsList = lista.RetornaLista(idcidade);
   		
   		list=(ListView)findViewById(R.id.lista_can);
   	    adapter=new LazyAdapter(this, songsList);        
           list.setAdapter(adapter);
           
           list.setOnItemClickListener(new OnItemClickListener() {

   			@SuppressWarnings("unchecked")
   			@Override
   			public void onItemClick(AdapterView<?> parent, View view,
   					int position, long id) {
   				progressDialog.setCancelable(true);
   		        progressDialog.setMessage("Aguarde...");
   		        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
   		        progressDialog.setProgress(0);
   		        progressDialog.show();
   				Intent intent = new Intent(ListaCandidatos.this, CollectionDemoActivity.class);
   				intent.putExtra("id", LazyAdapter.filtrado.get(position).get("id"));
   				intent.putExtra("nome", LazyAdapter.filtrado.get(position).get("title"));
   				intent.putExtra("partido", LazyAdapter.filtrado.get(position).get("artist"));
   				intent.putExtra("url", LazyAdapter.filtrado.get(position).get("thumb_url"));
   				intent.putExtra("position", position);
   					
                   startActivity(intent);
                   progressDialog.dismiss();
                                
   			}
   		});
   		
       } else {
           showErrorPage();
       }
   }
// Displays an error if the app is unable to load content.
   private void showErrorPage() {
	   new AlertDialog.Builder(ListaCandidatos.this)
       .setTitle("Erro de conexão")
       .setMessage("Para usar este recurso você deve estar conectado à internet")
       .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) { 
               // continue with delete
           }
        })
       .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) { 
               // do nothing
           }
        })
       .setIcon(android.R.drawable.ic_dialog_alert)
        .show();
   }
   public static void updateadapter(){
		 adapter.notifyDataSetChanged(); 
   }
  
}
