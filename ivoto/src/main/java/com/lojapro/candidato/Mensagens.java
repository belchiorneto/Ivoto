package com.lojapro.candidato;

import java.util.ArrayList;
import java.util.HashMap;

import com.lojapro.can.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class Mensagens extends Fragment {
	private SharedPreferences sharedPref;
	private Activity act;
	private ListView list;
	private AdaptadorMensagens adapt;
	private String idandroid;
	private TextView mensagemajuda;
	private TextView mensageminicial;
	private ListaMensagens lista;
	private ProgressBar load;
	private ArrayList<HashMap<String, String>> retorno;
	@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
			View android = inflater.inflate(R.layout.mensagens, container, false);	
			act = getActivity();
			sharedPref = act.getSharedPreferences("com.lojapro.can", Context.MODE_PRIVATE);
			idandroid = Secure.getString(act.getContentResolver(), Secure.ANDROID_ID);
			mensagemajuda = (TextView)android.findViewById(R.id.textView2);
			mensageminicial = (TextView)android.findViewById(R.id.textView1);
			load = (ProgressBar)android.findViewById(R.id.loadbar);
			list = (ListView)android.findViewById(R.id.lista_mensagens);
			String idcan = sharedPref.getString("idcan", "1");
			ListaMensagens lista = new ListaMensagens(act, idcan);
			retorno = new ArrayList<HashMap<String, String>>();
			retorno = lista.RetornaLista();
			adapt = new AdaptadorMensagens(act, retorno);
			list.setAdapter(adapt);
	        list.setOnItemClickListener(new OnItemClickListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
				final int position, long id) {
					
					 
					// Collections.fill(AdaptadorPropostas.listb, Boolean.FALSE);
				    if(AdaptadorMensagens.listb.get(position) == false){ 
				    	AdaptadorMensagens.listb.set(position, true);
				    }else{
				    	AdaptadorMensagens.listb.set(position, false);
				    }
					adapt.notifyDataSetChanged();
				}
			});
            return android;
	}
	@Override 
	public void onStart() { 
		super.onStart();
		precarregaitens();
		
	}
	private void precarregaitens(){
    	// List tens
		String idcan = sharedPref.getString("idcan", "1");
		lista = new ListaMensagens(act, idcan);			
		retorno = lista.RetornaLista();
		// if exists
		int qtitens = retorno.size();
    	mensagemajuda.setText("Aqui serão listadas as mensagens recebidas de outros usuários e de eleitores");
    			if(qtitens > 0){
    				if(qtitens > 1){
    					mensageminicial.setText(qtitens + " mensagens encontradas");
    				}else{
    					mensageminicial.setText(qtitens + " mensagem encontrada");
    				}
    				// load itens
    				carregaitens();
    			}else{
    				// se não mostra mensagem
    				mensageminicial.setText("Nenhuma mensagem foi recebida ainda");
    				load.setVisibility(View.GONE);
    			}
    }
	private void carregaitens() {
		adapt = new AdaptadorMensagens(act, retorno);
		list.setAdapter(adapt);
		load.setVisibility(View.GONE);
        list.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
				// Collections.fill(AdaptadorPropostas.listb, Boolean.FALSE);
			    if(AdaptadorMensagens.listb.get(position) == false){ 
			    	AdaptadorMensagens.listb.set(position, true);
			    }else{
			    	AdaptadorMensagens.listb.set(position, false);
			    }
				adapt.notifyDataSetChanged();
			}
		});
	}
}

