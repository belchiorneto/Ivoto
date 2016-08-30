package com.lojapro.can;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class ListaRegistros {
	
	XMLParser parser;
	static final String URL = "http://ivoto.com.br/data_eleicoes/ws.php";
	// XML node keys
	static final String KEY_SONG = "song"; // parent node
	static final String KEY_IDUSUARIO = "idusuario"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_ARTIST = "artist";
	static final String KEY_DURATION = "duration";
	static final String KEY_INDICACAO = "ind";
	static final String KEY_STATUSINDICADO = "status_indicado";
	static final String KEY_THUMB_URL = "thumb_url";
	NodeList nl;
	
	Context mContext;
	private Activity act;
	 public ListaRegistros (Activity act)
	 {
	     this.act = act;  
		 mContext = act.getApplicationContext();

	 }
	public ArrayList<HashMap<String, String>> RetornaLista(int idcidade){
		ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
		
		parser = new XMLParser(act, idcidade, 0, 1);
		Document doc = null;
		try {
			doc = parser.execute(URL).get();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		if(doc != null){
		nl = doc.getElementsByTagName(KEY_SONG);
		
		// looping through all song nodes <song>
				for (int i = 0; i < nl.getLength(); i++) {
					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					Element e = (Element) nl.item(i);
					// adding each child node to HashMap key => value
					map.put(KEY_ID,	parser.getValue(e, KEY_ID));
					map.put(KEY_IDUSUARIO,	parser.getValue(e, KEY_IDUSUARIO));
					map.put(KEY_TITLE, parser.getValue(e, KEY_TITLE));
					map.put(KEY_ARTIST, parser.getValue(e, KEY_ARTIST));
					map.put(KEY_DURATION, parser.getValue(e, KEY_DURATION));
					map.put(KEY_INDICACAO, parser.getValue(e, KEY_INDICACAO));
					map.put(KEY_STATUSINDICADO, parser.getValue(e, KEY_STATUSINDICADO));
					map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));
					
					// adding HashList to ArrayList
					songsList.add(map);
				}
		}else{
			Toast.makeText(mContext, "Erro ao carregar a lista", Toast.LENGTH_LONG).show();
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();
			
			// adding each child node to HashMap key => value
			map.put(KEY_ID,	"");
			map.put(KEY_IDUSUARIO,	"");
			map.put(KEY_TITLE, "Houve um erro ao processar a lista");
			map.put(KEY_ARTIST, "");
			map.put(KEY_DURATION, "");
			map.put(KEY_INDICACAO, "");
			map.put(KEY_STATUSINDICADO, "");
			map.put(KEY_THUMB_URL, "");
			// adding HashList to ArrayList
			songsList.add(map);
		}
		return songsList;
	}
	public Element RetornaElemento(int position){
		Element e = (Element) nl.item(position);
		return e;
	}
}
