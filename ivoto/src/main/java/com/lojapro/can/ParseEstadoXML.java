package com.lojapro.can;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
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

public class ParseEstadoXML {
	NodeList nl;
	String sigla;
	Context ctx;
	int retorno;
	public int retornaid(String s, Context c){
		sigla = s;
		ctx = c;
		String xml = null;
		org.w3c.dom.Document doc;
		try{
		xml=getXML(c.getAssets().open("estadosxml.xml"));
		}catch(Exception e){
		    Log.d("Error",e.toString());
		}
		ParseXMLTask parseXMLTask = new ParseXMLTask();
	
		try {
			retorno = parseXMLTask.execute(xml).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retorno;
	}
	public static String getXML(InputStream is)throws IOException {
		System.out.println(is.available());
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
	class ParseXMLTask extends AsyncTask<String, Integer, Integer> {
		
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	       
	    }
		
		@Override
	    protected Integer doInBackground(String... params) {
	        String yourXml = params[0]; 
	        int retorno = 0;
	        org.w3c.dom.Document doc;
	        doc = XMLfromString(yourXml);
	        retorno = parseXML(doc, sigla);
	        //Parse your xml here
	        return retorno;
	    }

	    protected void onPostExecute(Integer result) {
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
	 
	 private int parseXML(org.w3c.dom.Document doc, String sigla)
		{
			nl = doc.getElementsByTagName("estado");
			int retorno = 0;
			
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
				
				if(namevalue.equals(sigla)){
					
					retorno = Integer.parseInt(idvalue);
				}
			}
			return retorno;
		}
}
