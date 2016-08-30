package com.lojapro.can;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class ListaRegistrosContato {
	
	XMLParserContato parser;
	static final String URL = "http://ivoto.com.br/data_eleicoes/contato.php";
	// XML node keys
	private String KEY_INICIO= "candidato";
	private String KEY_ID = "id";
	private String KEY_ID_USUARIO = "idusuario";
	private String KEY_NOME_URNA = "nome_urna";
	private String KEY_CARGO = "cargo";
	private String KEY_STATUS_VOTO = "status_voto";
	private String KEY_VOTOS = "votos";
	private String KEY_URL = "url";
	NodeList nl;
	
	Context mContext;
	 public ListaRegistrosContato (Context context)
	 {
	       mContext = context;

	 }
	public ArrayList<HashMap<String, String>> RetornaLista(int idcidade){
		ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
		
		parser = new XMLParserContato(mContext, idcidade);
		Document doc = null;
		try {
			doc = parser.execute(URL).get();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		if(doc != null){
		nl = doc.getElementsByTagName(KEY_INICIO);
		
		// looping through all song nodes <song>
				for (int i = 0; i < nl.getLength(); i++) {
					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					Element e = (Element) nl.item(i);
					// adding each child node to HashMap key => value
					map.put(KEY_ID,	parser.getValue(e, KEY_ID));
					map.put(KEY_ID_USUARIO,	parser.getValue(e, KEY_ID_USUARIO));
					map.put(KEY_NOME_URNA, parser.getValue(e, KEY_NOME_URNA));
					map.put(KEY_CARGO, parser.getValue(e, KEY_CARGO));
					map.put(KEY_STATUS_VOTO, parser.getValue(e, KEY_STATUS_VOTO));
					map.put(KEY_VOTOS, parser.getValue(e, KEY_VOTOS));
					map.put(KEY_URL, parser.getValue(e, KEY_URL));
					
					// adding HashList to ArrayList
					songsList.add(map);
				}
		}else{
			Toast.makeText(Principal.getAppContext(), "Erro ao carregar a lista", Toast.LENGTH_LONG).show();
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();
			
			// adding each child node to HashMap key => value
			map.put(KEY_ID,	"");
			map.put(KEY_ID_USUARIO,	"");
			map.put(KEY_NOME_URNA, "Houve um erro ao processar a lista");
			map.put(KEY_CARGO, "");
			map.put(KEY_STATUS_VOTO, "");
			map.put(KEY_VOTOS, "");
			map.put(KEY_URL, "");
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
