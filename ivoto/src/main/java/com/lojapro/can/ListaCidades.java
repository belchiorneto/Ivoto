package com.lojapro.can;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import com.lojapro.can.R;
import android.widget.AdapterView.OnItemClickListener;

public class ListaCidades extends Activity {
	
	ListView list;
	ListCidadesAdapter adapter;
	NodeList nl;
	static Intent itent;
	public static int idestado = 1;
	ArrayList<HashMap<String, String>> listacidades;

	private ProgressDialog progressDialog;
	public void onCreate(Bundle savedInstanceState) {
		
		long startTime = System.currentTimeMillis();
		super.onCreate(savedInstanceState);
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("inicio:" + elapsedTime);
		setContentView(R.layout.list_cidades);
		 progressDialog = new ProgressDialog(ListaCidades.this);
	        progressDialog.setCancelable(true);
	        progressDialog.setMessage("Carregando...");
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progressDialog.setProgress(0);
	        progressDialog.show();
	}
	
	@Override
	   protected void onResume() {
	      super.onResume();
	        if(progressDialog.isShowing()){
				progressDialog.dismiss();
			}
	    /*
	        itent = getIntent(); 
			if(itent.getExtras().getInt("position") != 0){
				 idestado = itent.getExtras().getInt("position");
			 }
		*/
			list=(ListView)findViewById(R.id.lista_cid);
		    String xml = null;
			org.w3c.dom.Document doc;
			try{
			xml=getXML(getAssets().open("cidadesjson.xml"));
			}catch(Exception e){
			    Log.d("Error",e.toString());
			}
			ParseXMLTask parseXMLTask = new ParseXMLTask();
			try {
				listacidades = parseXMLTask.execute(xml).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		
			adapter = new ListCidadesAdapter(this, listacidades);
			
			list.setAdapter(adapter);
			list.setTextFilterEnabled(true);
			
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					progressDialog.setCancelable(true);
			        progressDialog.setMessage("Aguarde...");
			        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			        progressDialog.setProgress(0);
			        progressDialog.show();
					Intent i = new Intent(ListaCidades.this, ListaCandidatos.class);
	                String idcidade = (String) view.getTag();
					System.out.println("id cidade: " + idcidade);
	                i.putExtra("position", Integer.parseInt(idcidade));
	                
	                startActivity(i);
	                             
				}
			});
	      
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
	              if(adapter != null){
	            	if (TextUtils.isEmpty(newQueryText.toString())) {
	            	  
	            	  adapter.getFilter().filter(newQueryText.toString());
	              } else {
	            	  adapter.getFilter().filter(newQueryText.toString());
	              }
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
		 class ParseXMLTask extends AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {
				
				@Override
			    protected void onPreExecute() {
			        super.onPreExecute();
			       
			    }
				
				@Override
			    protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			        String yourXml = params[0]; 
			        ArrayList<HashMap<String, String>> retorno = new ArrayList<HashMap<String, String>>();
			        org.w3c.dom.Document doc;
			        doc = XMLfromString(yourXml);
			        retorno = parseXML(doc);
			        //Parse your xml here
			        return retorno;
			    }

			    protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			        super.onPostExecute(result);
			        progressDialog.dismiss();
			        //do something after parsing is done
			    }   
			}
		 
		 public final static org.w3c.dom.Document XMLfromString(String xml){
			  org.w3c.dom.Document doc = null;

		        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		            try {

		          DocumentBuilder db = dbf.newDocumentBuilder();

		          InputSource is = new InputSource();
		              is.setCharacterStream(new StringReader(xml));
		              doc = db.parse(is); 

		        } catch (ParserConfigurationException e) {
		          System.out.println("XML parse error: " + e.getMessage());
		          return null;
		        } catch (SAXException e) {
		          System.out.println("Wrong XML file structure: " + e.getMessage());
		                return null;
		        } catch (IOException e) {
		          System.out.println("I/O exeption: " + e.getMessage());
		          return null;
		        }

		            return doc;

		  }
		 
		 private ArrayList<HashMap<String, String>> parseXML(org.w3c.dom.Document doc)
			{
				nl = doc.getElementsByTagName("cidade");
				ArrayList<HashMap<String, String>> retorno = new ArrayList<HashMap<String, String>>();
				
				for(int i = 0; i<nl.getLength(); i++){
					HashMap<String, String> hm = new HashMap<String, String>();
					Node node = nl.item(i);
					Element fstElmnt = (Element) node;
					Element id = (Element) node;
					NodeList nameList = fstElmnt.getElementsByTagName("nome");
					NodeList idlist = fstElmnt.getElementsByTagName("id");
					NodeList idestadolist = fstElmnt.getElementsByTagName("idestado");
					Element nameElement = (Element) nameList.item(0);
					Element idelement = (Element) idlist.item(0);
					Element idestadoel = (Element) idestadolist.item(0);
					nameList = nameElement.getChildNodes();
					idlist = idelement.getChildNodes();
					idestadolist = idestadoel.getChildNodes();
					String idvalue = ((Node) idlist.item(0)).getNodeValue();
					String namevalue = ((Node) nameList.item(0)).getNodeValue();
					String idestadovalue = ((Node) idestadolist.item(0)).getNodeValue();
					//NodeList nome = ((Element) nl.item(i)).getElementsByTagName("nome");
					
					if(Integer.parseInt(idestadovalue) == idestado){
						
						hm.put("nomecidade", namevalue);
						hm.put("idcidade", idvalue);
						retorno.add(hm);
					}
				}
				return retorno;
			}
	}


/*
private class getListFromXML extends AsyncTask<String, Integer, Long> {

	public ArrayList<String> getlistcidades(){
	ArrayList<String> retorno = new ArrayList();
	String xml = null;
	org.w3c.dom.Document doc;
	 try{
	 xml=getXML(getAssets().open("cidadesjson.xml"));
	 }catch(Exception e){
	     Log.d("Error",e.toString());
	  }
	 
	 doc = XMLfromString(xml);
	 retorno = parseXML(doc);
	return retorno;
}

	 
	
	@Override
	protected Long doInBackground(URL... params) {
		// TODO Auto-generated method stub
		return null;
	}
	*/
