package com.lojapro.can;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.util.Log;


public class XMLParseCidadesBusca extends AsyncTask<String, Void, Document> {
	String URLc = "http://ivoto.com.br/data_eleicoes/cidades.php";
	Context ctx;
	String nome;
	public AsyncResponse delegate = null; //Call back interface
	ProgressDialog pd;
	
	public XMLParseCidadesBusca(Context context, String nome) {
		this.ctx = context;
		this.nome = nome;
		pd = new ProgressDialog(context);
	}
	@Override
    protected void onPreExecute() {
			pd.setMessage("Aguarde ...");
			pd.setTitle("Carregando lista da web");
			pd.show();
		
    }
	/**
	 * Getting XML from URL making HTTP request
	 * @param url string
	 * @return 
	 * */
	
	protected Document doInBackground(String... urls) {
		Document retorno = null;
	   URL url;
	   try {
		
			url = new URL(urls[0] + "?nome="+nome);
		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
            retorno = doc;
            
         
        } catch (Exception e) {
            retorno = null;
        }
	return retorno;
	}
	
	protected void onPostExecute(Document feed) {
        // TODO: check this.exception 
        // TODO: do something with the feed
		
		if (pd.isShowing()) {
            pd.dismiss();
        }else{
        }
        
	}

	
	/**
	 * Getting XML DOM element
	 * @param XML string
	 * */
	public Document getDomElement(String xml){
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
		        is.setCharacterStream(new StringReader(xml));
		        doc = db.parse(is); 

			} catch (ParserConfigurationException e) {
				Log.e("Error: ", e.getMessage());
				return null;
			} catch (SAXException e) {
				Log.e("Error: ", e.getMessage());
	            return null;
			} catch (IOException e) {
				Log.e("Error: ", e.getMessage());
				return null;
			}

	        return doc;
	}
	
	/** Getting node value
	  * @param elem element
	  */
	 public final String getElementValue( Node elem ) {
	     Node child;
	     if( elem != null){
	         if (elem.hasChildNodes()){
	             for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
	                 if( child.getNodeType() == Node.TEXT_NODE  ){
	                     return child.getNodeValue();
	                 }
	             }
	         }
	     }
	     return "";
	 }
	 
	 /**
	  * Getting node value
	  * @param Element node
	  * @param key string
	  * */
	 public String getValue(Element item, String str) {		
			NodeList n = item.getElementsByTagName(str);		
			return this.getElementValue(n.item(0));
	}
	}
