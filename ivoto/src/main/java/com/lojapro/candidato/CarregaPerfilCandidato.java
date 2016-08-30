package com.lojapro.candidato;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.lojapro.can.AsyncResponse;
import com.lojapro.can.Principal;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class CarregaPerfilCandidato {
	private String URL = "http://ivoto.com.br/data_eleicoes/dados_can.php";
	private Context mContext;
	private String KEY_INICIO = "inicio";
	private NodeList node;
	private String id_candidato;
	public CarregaPerfilCandidato (Context context)
	 {
	       mContext = context;

	 }
	public ArrayList<String> retornalista(String idcandidato){
		id_candidato = idcandidato;
		ArrayList<String> retorno = new ArrayList<String>();
		Document doc = null;
		try {
			doc = new PegaXml(mContext).execute(URL).get();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		if(doc != null){
			node = doc.getElementsByTagName(KEY_INICIO);
			Element e = (Element) node.item(0);
			
			String id = e.getElementsByTagName("id").item(0).getTextContent();
			String name = e.getElementsByTagName("nome").item(0).getTextContent();
			String nomeurna = e.getElementsByTagName("nome_urna").item(0).getTextContent();
			String ocupacao = e.getElementsByTagName("ocupacao").item(0).getTextContent();
			String imagem = e.getElementsByTagName("imagem").item(0).getTextContent();
			String estado = e.getElementsByTagName("estado").item(0).getTextContent();
			String nome_cidade = e.getElementsByTagName("nome_cidade").item(0).getTextContent();
			String mensagem = e.getElementsByTagName("mensagem").item(0).getTextContent();
			String urlimagem = e.getElementsByTagName("urlimagem").item(0).getTextContent();
			String verify = e.getElementsByTagName("verify").item(0).getTextContent();
			retorno.add(name);
			retorno.add(nomeurna);
			retorno.add(ocupacao);
			retorno.add(imagem);
			retorno.add(estado);
			retorno.add(nome_cidade);
			retorno.add(mensagem);
			retorno.add(urlimagem);
			retorno.add(verify);
			retorno.add(id);
			
		}else{
			Toast.makeText(Principal.getAppContext(), "Erro ao carregar os dados", Toast.LENGTH_LONG).show();
			retorno.add(0, "Erro ao carregar os dados");
	
		}
		
		return retorno;
	}
	class PegaXml extends AsyncTask<String, Void, Document> {
	public AsyncResponse delegate = null; //Call back interface
	ProgressDialog pd;
	public PegaXml(Context context) {
		pd = new ProgressDialog(context);
	}
	@Override
    protected void onPreExecute() {
		pd.setMessage("Aguarde ...");
		pd.setTitle("Carregando lista da web");
		pd.show();
    }
	protected Document doInBackground(String... urls) {
		Document retorno = null;
	    URL url;
	    
	   try {
		   url = new URL(urls[0] + "?id_candidato="+id_candidato);
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
		if (pd.isShowing()) {
            pd.dismiss();
        }   
	}
	public Document getDomElement(String xml){
		Document doc = null;
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
	 public String getValue(Element item, String str) {		
			NodeList n = item.getElementsByTagName(str);		
			return this.getElementValue(n.item(0));
	 }
	}

}