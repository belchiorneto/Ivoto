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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ParseCidadeXML {
	NodeList nl;
	int idestado;
	Context ctx;
	ArrayList<String> listacidades;
	public ArrayList<String> retornalistacidades(int estado, Context c){
		idestado = estado;
		ctx = c;
		ArrayList<String> r = new ArrayList<String>();
		String xml = null;
		org.w3c.dom.Document doc;
		try{
		xml=getXML(ctx.getAssets().open("cidadesjson.xml"));
		
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
		r = listacidades;
		return r;
	}
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
	class ParseXMLTask extends AsyncTask<String, Integer, ArrayList<String>> {
		
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	       
	    }
		
		@Override
	    protected ArrayList<String> doInBackground(String... params) {
	        String yourXml = params[0]; 
	        ArrayList<String> retorno = new ArrayList<String>();
	        org.w3c.dom.Document doc;
	        doc = XMLfromString(yourXml);
	        retorno = parseXML(doc, idestado);
	        //Parse your xml here
	        return retorno;
	    }

	    protected void onPostExecute(ArrayList<String> result) {
	        super.onPostExecute(result);
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
	 
	 private ArrayList<String> parseXML(org.w3c.dom.Document doc, int idestado)
		{
			nl = doc.getElementsByTagName("cidade");
			ArrayList<String> retorno = new ArrayList<String>();
			System.out.println("Tamanho do nod: " + nl.getLength());
			//for(int i = 0; i<nl.getLength(); i++){
			for(int i = 0; i<30; i++){
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
				int idlocal = Integer.parseInt(idestadovalue);
				System.out.println("Id estado: " + idestado +" / Id local: "+ idlocal);
				if(idlocal == idestado){
					
					//retorno.add(namevalue);
				}
			}
			return retorno;
		}
}
