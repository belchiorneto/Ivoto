package com.lojapro.can;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Context;

public class ListaCandidato {
	XMLParser parser;
	static final String URL = "http://ivoto.com.br/data_eleicoes/candidato.php";
	static final String KEY_HEAD = "head";
	static final String KEY_IDCANDIDATO = "idcandidato";
	static final String KEY_NOME = "nome";
	static final String KEY_PARTIDO = "partido";
	static final String KEY_CARGO = "cargo";
	static final String KEY_INDICACAO = "indicacoes";
	static final String KEY_STATUSINDICADO = "status_indicado";
	static final String KEY_THUMB_URL = "thumb_url";
	NodeList nl;
	Context mContext;
	private Activity act;
	
	public ListaCandidato (Activity act)
	 {
	    this.act = act;   
		mContext = act.getApplicationContext();
	        
	 }
	public ArrayList<HashMap<String, String>> RetornaValor(int idcandidato){
		ArrayList<HashMap<String, String>> lista = new ArrayList<HashMap<String, String>>();
		parser = new XMLParser(act, 0, idcandidato, 1);
		Document doc = null;
		try {
			doc = parser.execute(URL).get();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		
		nl = doc.getElementsByTagName(KEY_HEAD);
		
		// looping through all song nodes <song>
				for (int i = 0; i < nl.getLength(); i++) {
					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					Element e = (Element) nl.item(i);
					// adding each child node to HashMap key => value
					
					map.put(KEY_IDCANDIDATO,parser.getValue(e, KEY_IDCANDIDATO));
					map.put(KEY_NOME, parser.getValue(e, KEY_NOME));
					map.put(KEY_PARTIDO, parser.getValue(e, KEY_PARTIDO));
					map.put(KEY_CARGO, parser.getValue(e, KEY_CARGO));
					map.put(KEY_INDICACAO, parser.getValue(e, KEY_INDICACAO));
					map.put(KEY_STATUSINDICADO, parser.getValue(e, KEY_STATUSINDICADO));
					map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));
					
					// adding HashList to ArrayList
					lista.add(map);
				}
		return lista;
	}
}
