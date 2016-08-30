package com.lojapro.candidato;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.lojapro.can.AsyncResponse;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ListaCandidatosBuscados {
	static final String URL = "http://ivoto.com.br/data_eleicoes/busca_candidato.php";
	RetornoXML parser;
	Context mContext;
	NodeList nl;
	static final String KEY_REGISTRO = "registro"; // parent node
	static final String KEY_ID = "id"; 
	static final String KEY_NOMEURNA = "nome_urna";
	static final String KEY_NOME = "nome";
	private String busca;
	public ListaCandidatosBuscados (Context context, String busca)
	 {
	       mContext = context;
	       this.busca = busca;

	 }
	public ArrayList<HashMap<String, String>> RetornaLista(){
		ArrayList<HashMap<String, String>> retorno = new ArrayList<HashMap<String, String>>();
		parser = new RetornoXML(mContext, busca);
		Document doc = null;
		try {
			doc = parser.execute(URL).get();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		
		nl = doc.getElementsByTagName(KEY_REGISTRO);
		
		// looping through all song nodes <song>
				for (int i = 0; i < nl.getLength(); i++) {
					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					Element e = (Element) nl.item(i);
					// adding each child node to HashMap key => value
					map.put(KEY_ID,	parser.getValue(e, KEY_ID));
					map.put(KEY_NOMEURNA,	parser.getValue(e, KEY_NOMEURNA));
					map.put(KEY_NOME, parser.getValue(e, KEY_NOME));
					// adding HashList to ArrayList
					retorno.add(map);
				}
		return retorno;
	}
}
class RetornoXML extends AsyncTask<String, Void, Document> {
	
	public AsyncResponse delegate = null;
	ProgressDialog pd;
	String busca;
	public RetornoXML(Context context, String busca) {
		pd = new ProgressDialog(context);
		this.busca = busca;
	}
	@Override
    protected void onPreExecute() {
		pd.setMessage("Aguarde ...");
		pd.setTitle("Buscando Candidatos");
		pd.show();
    }
	
	protected Document doInBackground(String... urls) {
		Document retorno = null;
	   
	   
	   String estado = RegistrarComoCandidato.estado;
	   URL url;
	   try {
			url = new URL(urls[0] + "?busca="+URLEncoder.encode(busca, "UTF-8")+"&estado="+estado);
		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
            retorno = doc;
    
         
        } catch (Exception e) {
            System.out.println("XML Pasing Excpetion = " + e);
            retorno = null;
        }
	return retorno;
	}
	
	protected void onPostExecute(Document feed) {
     	if (pd.isShowing()) {
            pd.dismiss();
        }else{
        }
        
	}
	public String getValue(Element item, String str) {		
		NodeList n = item.getElementsByTagName(str);		
		return this.getElementValue(n.item(0));
	}
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

	}