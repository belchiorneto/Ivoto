package com.lojapro.can;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract.Document;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.lojapro.candidato.Candidato;
import com.lojapro.candidato.PerfilCandidato;
import com.lojapro.candidato.RegistrarComoCandidato;
import com.lojapro.can.R;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class Principal extends Activity {
	SharedPreferences sharedpreferences; 
	static ProgressDialog pd;
	GPSTracker gps;
	
	 static double latitude = 0;
	 static double longitude = 0;
	 
	 // variaveis do endere�o
	 static String rua;
	 static String numero;
	 static String bairro;
	 static String cidade;
	 static String estado;
	 static String cep;
	 static String pais;
	// All static variables
	static final String URL = "http://ivoto.com.br/data_eleicoes/ws.php";
	static final String URL_SEND = "http://ivoto.com.br/data_eleicoes/send.php";
	private static Context context;
	
	// XML node keys
	static final String KEY_SONG = "song"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_IDUSUARIO = "idusuario";
	static final String KEY_TITLE = "title";
	static final String KEY_ARTIST = "artist";
	static final String KEY_DURATION = "duration";
	static final String KEY_INDICACAO = "ind";
	static final String KEY_STATUSINDICADO = "status_indicado";
	static final String KEY_THUMB_URL = "thumb_url";
	private static final String ns = null;
	GridView grid;
    LazyAdapter adapter;
	XMLParser parser;
	NodeList nl;
	ImageView bot;
	ImageView botadmin;
	static DataBaseCidades dbcidades;
	//XMLParser asyncTask = new XMLParser();
	ArrayList<HashMap<String, String>> songsList;
	List<Entry> listareg;
	String nomesestados[];
	int[] idsestados;
	public static final String MY_PREFS_NAME = "configs";
	private ProgressDialog progressDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		 
		
		
		 LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View v = inflater.inflate(R.layout.main, null);
	    progressDialog = new ProgressDialog(Principal.this);
	     
	    LinearLayout layout = (LinearLayout) v.findViewById(R.id.ll1);
	    Button share = (Button)v.findViewById(R.id.sharebut);
	    share.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
            	//create the send intent
            	Intent shareIntent = 
            	 new Intent(android.content.Intent.ACTION_SEND);

            	//set the type
            	shareIntent.setType("text/plain");

            	//add a subject
            	shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, 
            	 "Eleições 2016");

            	//build the body of the message to be shared
            	String shareMessage = "Veja quem são os pré-candidatos para as eleições 2016 em sua cidade. http://ivoto.com.br/appteste";

            	//add the message
            	shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
            	 shareMessage);

            	//start the chooser for sharing
            	startActivity(Intent.createChooser(shareIntent, 
            	 "Recomendar para um amigo"));
            }
	    });
	  
	    TextView id[];
		
		String xml = null;
		org.w3c.dom.Document doc;
		 try{
		 xml=getXML(getAssets().open("estadosxml.xml"));
		 }catch(Exception e){
		     Log.d("Error",e.toString());
		  }
		 
		 doc = XMLfromString(xml);
		
		 nomesestados = parseXML(doc);
		
              
    
		CustomGrid adapter = new CustomGrid(Principal.this, nomesestados, idsestados);
		 
	
		setContentView(layout);
		/*
		gps = new GPSTracker(Principal.this);
		// Check if GPS enabled
        if(gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }
		
        LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latitude, longitude,
                    getApplicationContext(), new GeocoderHandler());
        */
        Principal.context = getApplicationContext();
		
		/*
		ImageView im = (ImageView)findViewById(R.id.imageView1);  
	    im.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Principal.this, ListaCandidatos.class);
                startActivity(intent);
				
			}
	    	
	    });
      */
        
       
	}
	
	@Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE); 
		   String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
		   int idName = prefs.getInt("idName", 0); //0 is the default value.
		   String admins = prefs.getString("admin", "no"); //0 is the default value.

		 if(admins.equals("sim")){

			 bot.setVisibility(View.GONE);
			 botadmin.setVisibility(View.VISIBLE);
		 }else{

			 botadmin.setVisibility(View.GONE);
			 bot.setVisibility(View.VISIBLE);
		 }
    }
	@Override
	   protected void onResume() {
	      super.onResume();
	      if(progressDialog.isShowing()){
				progressDialog.dismiss();
		  }
	}

	private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
        
            if(locationAddress!= null){
            	String lines[] = new String[9];
            	lines = locationAddress.split("\\r?\\n");
            	if(lines.length > 5){
            	String ruabairro[] = lines[3].split(" - ");
            	String cidadeestado[] = lines[4].split(" - ");
                String mnumero[] =  ruabairro[0].split(",");
            	
            	
           	if(mnumero.length > 1){ 
           		numero = mnumero[mnumero.length-1];
           		rua = mnumero[0];
           	}else{
           		numero = "0";
           		rua = ruabairro[0];
           	}
           	 bairro = ruabairro[1];
           	 cidade = cidadeestado[0];
           	 estado = cidadeestado[1];
           	 cep = lines[5];
           	 pais = lines[8];
            	}
            }
        }
    }
	void processFinish(String output){
	     //this you will received result fired from async class of onPostExecute(result) method.
		
	}
	public static Context getAppContext() {
        return Principal.context;
    }
	public static double getlat() {
        return latitude;
    }
	public static double getlong() {
        return longitude;
    }
	public static String getrua() {
        return rua;
    }
	public static String getnumero() {
        return numero;
    }
	public static String getcidade() {
        return cidade;
    }
	public static String getbairro() {
        return bairro;
    }
	public static String getestado() {
        return estado;
    }
	public static String getpais() {
        return pais;
    }
	public static String getcep() {
        return cep;
    }
	
	class Estado
	{

		public String id;
		public String nome;
		
	}
	private String[] parseXML(org.w3c.dom.Document doc)
	{
		nl = doc.getElementsByTagName("estado");
		String[] retorno = new String[nl.getLength()];
		idsestados = new int[nl.getLength()];
		for(int i = 0; i<nl.getLength(); i++){

			Node node = nl.item(i);
			Element fstElmnt = (Element) node;
			Element id = (Element) node;
			NodeList nameList = fstElmnt.getElementsByTagName("nome");
			NodeList idlist = fstElmnt.getElementsByTagName("id");
			Element nameElement = (Element) nameList.item(0);
			Element idelement = (Element) idlist.item(0);
			nameList = nameElement.getChildNodes();
			idlist = idelement.getChildNodes();
			String idvalue = ((Node) idlist.item(0)).getNodeValue();
			String namevalue = ((Node) nameList.item(0)).getNodeValue();
			//NodeList nome = ((Element) nl.item(i)).getElementsByTagName("nome");
			retorno[i] = namevalue;
			idsestados[i] = Integer.parseInt(idvalue);
		}
		return retorno;
	}

	//getXML   method
	 public static String getXML(InputStream is)throws IOException {

	    BufferedInputStream bis = new BufferedInputStream(is);
	    ByteArrayOutputStream buf = new ByteArrayOutputStream();
	    int result = bis.read();
	    while(result != -1) {
	      byte b = (byte)result;
	      buf.write(b);
	      result = bis.read();
	    }        
	    return buf.toString();
	}
	//XMLfromString   method
	  public final static org.w3c.dom.Document XMLfromString(String xml){
		  org.w3c.dom.Document doc = null;

	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            try {

	          DocumentBuilder db = dbf.newDocumentBuilder();

	          InputSource is = new InputSource();
	              is.setCharacterStream(new StringReader(xml));
	              doc = db.parse(is); 

	        } catch (ParserConfigurationException e) {

	          return null;
	        } catch (SAXException e) {

	                return null;
	        } catch (IOException e) {

	          return null;
	        }

	            return doc;

	  }
}
