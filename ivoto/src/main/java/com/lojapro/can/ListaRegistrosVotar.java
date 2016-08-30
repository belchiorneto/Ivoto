package com.lojapro.can;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class ListaRegistrosVotar {
	private SharedPreferences sharedPref;
	private String idcidade;
	private XMLParser parser;
	static final String URL = "http://ivoto.com.br/data_eleicoes/precandidatos_android.php";
	// XML node keys
	private String KEY_INICIO= "candidato";
	private String KEY_ID = "id";
	private String KEY_ID_USUARIO = "idusuario";
	private String KEY_NOME_URNA = "nome_urna";
	private String KEY_CARGO = "cargo";
	private String KEY_STATUS_VOTO = "status_voto";
	private String KEY_VOTOS = "votos";
	private String KEY_URL = "url";
	private String KEY_NUMERO = "numero";

	NodeList nl;
	private Activity act;
	
	Context mContext;
	 public ListaRegistrosVotar (Activity act)
	 {
	     this.act = act;  
	     mContext = act.getApplicationContext();
		 sharedPref = act.getSharedPreferences(
				 "com.lojapro.can", Context.MODE_PRIVATE);
		 idcidade = 	sharedPref.getString("idcidade", "1");
	 }
	public ArrayList<HashMap<String, String>> RetornaLista(int idcidade, int limit){
		ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
		// checa se tem arquivo local
		File file = new File(mContext.getFilesDir() + "/" + idcidade);
		if(file == null) {
			parser = new XMLParser(act, idcidade, 0, limit);
			Document doc = null;
			try {
				doc = parser.execute(URL).get();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				e1.printStackTrace();
			}
			if (doc != null) {
				nl = doc.getElementsByTagName(KEY_INICIO);

				// looping through all song nodes <song>
				for (int i = 0; i < nl.getLength(); i++) {
					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					Element e = (Element) nl.item(i);
					// adding each child node to HashMap key => value

					map.put(KEY_ID, parser.getValue(e, KEY_ID));
					map.put(KEY_ID_USUARIO, parser.getValue(e, KEY_ID_USUARIO));
					String nome = parser.getValue(e, KEY_NOME_URNA);
					map.put(KEY_NOME_URNA, nome);
					map.put(KEY_CARGO, parser.getValue(e, KEY_CARGO));
					map.put(KEY_STATUS_VOTO, parser.getValue(e, KEY_STATUS_VOTO));
					map.put(KEY_VOTOS, parser.getValue(e, KEY_VOTOS));
					map.put(KEY_URL, parser.getValue(e, KEY_URL));

					// adding HashList to ArrayList
					songsList.add(map);
				}
			} else {

				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				// adding each child node to HashMap key => value
				map.put(KEY_ID, "");
				map.put(KEY_ID_USUARIO, "");
				map.put(KEY_NOME_URNA, "Houve um erro ao processar a lista");
				map.put(KEY_CARGO, "");
				map.put(KEY_STATUS_VOTO, "");
				map.put(KEY_VOTOS, "");
				map.put(KEY_URL, "");
				// adding HashList to ArrayList
				songsList.add(map);
			}
		}else{
			//Read text from file
			StringBuilder text = new StringBuilder();

			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;

				while ((line = br.readLine()) != null) {
					text.append(line);
					text.append('\n');
				}
				br.close();
				String candidatos[] = text.toString().split("-registrocandidato-");
				for (String candidato : candidatos){

					String campos[] = candidato.toString().split("-campo-");
					Log.d("async", "Adicionando , map - " + campos.length +"/"+ candidato);
					if(campos.length == 4){
						String nome_urna = campos[0];
						String numero = campos[1];
						String cargo = campos[2];
						String url_imagem = campos[3];
						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();
						map.put(KEY_ID, "1");
						map.put(KEY_ID_USUARIO, "1");
						map.put(KEY_NOME_URNA, nome_urna);
						map.put(KEY_CARGO, cargo);
						map.put(KEY_STATUS_VOTO, "nao");
						map.put(KEY_VOTOS, "0");
						map.put(KEY_NUMERO, numero);
						map.put(KEY_URL, url_imagem);
						// adding HashList to ArrayList
						songsList.add(map);
						Log.d("async", "Adicionando map");
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.d("async", "Tamanho do songlist : " + songsList.size());
		return songsList;
	}
	public Element RetornaElemento(int position){
		Element e = (Element) nl.item(position);
		return e;
	}
}
