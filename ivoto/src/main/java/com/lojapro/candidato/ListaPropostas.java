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

public class ListaPropostas {
	static final String URL = "http://ivoto.com.br/data_eleicoes/propostas.php";
	RetornoXMLpropostas parser;
	Context mContext;
	NodeList nl;
	static final String KEY_REGISTRO = "proposta"; // parent node
	static final String KEY_ID = "id"; 
	static final String KEY_TITULO = "titulo";
	static final String KEY_CONTEUDO = "conteudo";
	static final String KEY_RATE = "rate";
	public ListaPropostas (Context context)
	 {
	       mContext = context;

	 }
	public ArrayList<HashMap<String, String>> RetornaLista(){
		ArrayList<HashMap<String, String>> retorno = new ArrayList<HashMap<String, String>>();
		parser = new RetornoXMLpropostas(mContext);
		Document doc = null;
		try {
			doc = parser.execute(URL).get();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		
		if(doc != null){
		nl = doc.getElementsByTagName(KEY_REGISTRO);
		
		// looping through all song nodes <song>
				for (int i = 0; i < nl.getLength(); i++) {
					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					Element e = (Element) nl.item(i);
					// adding each child node to HashMap key => value
					map.put(KEY_ID,	parser.getValue(e, KEY_ID));
					map.put(KEY_TITULO,	parser.getValue(e, KEY_TITULO));
					map.put(KEY_CONTEUDO, parser.getValue(e, KEY_CONTEUDO));
					map.put(KEY_RATE, parser.getValue(e, KEY_RATE));
					// adding HashList to ArrayList
					retorno.add(map);
				}
		}else{
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(KEY_ID,	"1");
			map.put(KEY_TITULO,	"Ocorreu um erro ao processar a lista");
			map.put(KEY_CONTEUDO,	"0");
			map.put(KEY_RATE,	"0");
			retorno.add(map);
		}
		return retorno;
	}
}
class RetornoXMLpropostas extends AsyncTask<String, Void, Document> {
	
	public AsyncResponse delegate = null;
	ProgressDialog pd;
	
	public RetornoXMLpropostas(Context context) {
		pd = new ProgressDialog(context);
	}
	@Override
    protected void onPreExecute() {
		pd.setMessage("Aguarde ...");
		pd.setTitle("Buscando propostas");
		pd.show();
    }
	
	protected Document doInBackground(String... urls) {
		Document retorno = null;
	   String idandroid = Propostas.idandroid;
	   
	   URL url;
	   try {
			url = new URL(urls[0] + "?id_android="+idandroid);
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