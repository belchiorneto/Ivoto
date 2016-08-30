package com.lojapro.can;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.lojapro.candidato.AdaptadorPropostasEleitor;
import com.lojapro.candidato.AndroidMultiPartEntity;
import com.lojapro.candidato.AndroidMultiPartEntity.ProgressListener;
import com.lojapro.can.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class Contato extends Fragment {
	SharedPreferences sharedPref;
	Activity act;
	public static String idandroid;
	public static int idcidade;
	private ListaRegistrosContato lista;
	private static AdaptadorContato adapter;
	public static ArrayList<HashMap<String, String>> listacandidatos;
	ListView listview;
	private int idcid;
	private ProgressBar load;
	private boolean isViewShown; 
	
	@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
			View android = inflater.inflate(R.layout.contato, container, false);
			idandroid = Secure.getString(getActivity().getContentResolver(), Secure.ANDROID_ID);
			sharedPref = getActivity().getSharedPreferences("com.lojapro.can", Context.MODE_PRIVATE);
			listview = (ListView)android.findViewById(R.id.lista_can_contato);
			idcid = sharedPref.getInt("cidade", 0);
			
			load = (ProgressBar)android.findViewById(R.id.loadbar);
			ListaCandidatos(idcid);
			return android;
    	}
	@Override 
	public void onStart() { 
		super.onStart(); 
		load.setVisibility(View.GONE);
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    if (getView() != null) {
	        isViewShown = true;
	        
	    } else {
	        isViewShown = false;
	    }
	}		
	public void ListaCandidatos(int idcidade){
		lista = new ListaRegistrosContato(getActivity().getApplicationContext());
		listacandidatos = lista.RetornaLista(idcidade);
	  		adapter = new AdaptadorContato(getActivity(), listacandidatos);
	  		listview.setAdapter(adapter);
		}
		public static void updateadapter(){
			 adapter.notifyDataSetChanged(); 
		}
    }