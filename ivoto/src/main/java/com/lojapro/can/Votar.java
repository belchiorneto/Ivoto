package com.lojapro.can;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lojapro.candidato.AdaptadorPropostasEleitor;
import com.lojapro.candidato.AndroidMultiPartEntity;
import com.lojapro.candidato.AndroidMultiPartEntity.ProgressListener;
import com.lojapro.can.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Votar extends Fragment {
	private SharedPreferences sharedPref;
	private Activity act;
	public static String idandroid;
	public static int idcidade;
	private ListaRegistrosVotar lista;
	private static AdaptadorVotar adapter;
	public static ArrayList<HashMap<String, String>> listacandidatos;
	private ListView listview;
	private int idcidnovo;
	private int idcid;

	private ProgressBar load;
	
	private int visibleThreshold = 5;
    private int currentPage = 1;
    private int previousTotal = 0;
    private int previousPage = 0;
    private int previousVisible = 0;
    private int itensretornados = 20;
    private int ultimoitem = 0;
    private int primeiroitemvisivel = 0;
    private boolean loading = true;
    public boolean carregar = false;

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
			View android = inflater.inflate(R.layout.votar, container, false);
			load = (ProgressBar)android.findViewById(R.id.loadbar);
			idandroid = Secure.getString(getActivity().getContentResolver(), Secure.ANDROID_ID);
			sharedPref = getActivity().getSharedPreferences(
				"com.lojapro.can", Context.MODE_PRIVATE);
			Log.d("async", "Shared: " + sharedPref);
			listview = (ListView)android.findViewById(R.id.lista_can_votar);
			String idcidtemp = 	sharedPref.getString("idcidade", "1");
			idcidade = Integer.parseInt(idcidtemp);
			idcid = idcidade;
			//idcid = sharedPref.getInt("cidade", 1);
			Log.d("async", "Id cidade: " + idcid +"/"+idcidade);
			ListaCandidatos(idcid, currentPage);
			return android;
    	}
	@Override 
	public void onStart() { 
		super.onStart(); 
		
	} 
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    if (isVisibleToUser) {

	    }
	}	
	
	
		public void ListaCandidatos(int idcidade, int limit){
			lista = new ListaRegistrosVotar(getActivity());
			listacandidatos = lista.RetornaLista(idcidade, limit);
			itensretornados = listacandidatos.size();
	  		adapter = new AdaptadorVotar(getActivity(), listacandidatos);
	  		listview.setAdapter(adapter);
	  		listview.setOnScrollListener(new EndlessScrollListener());
	  		load.setVisibility(View.GONE);
	  		carregar = false;
		}
		public static void updateadapter(){
			 adapter.notifyDataSetChanged(); 
		}
		public class EndlessScrollListener implements OnScrollListener {
		    public EndlessScrollListener() {
		    }
		    public EndlessScrollListener(int vis) {
		        visibleThreshold = vis;
		    }

		    @Override
		    public void onScroll(AbsListView view, int firstVisibleItem,
		            int visibleItemCount, int totalItemCount) {
		    	if(firstVisibleItem + visibleItemCount > ultimoitem){
		    		ultimoitem = firstVisibleItem;
		    	}
		    	int total = listview.getCount();
		    
		    }

		    @Override
		    public void onScrollStateChanged(AbsListView view, int scrollState) {
	    		int threshold = 1;
				int count = listview.getCount();

				if (scrollState == SCROLL_STATE_IDLE) {
					if (listview.getLastVisiblePosition() >= count - threshold) {
						
						ListaCandidatos(idcid, currentPage + 1);
						currentPage++;
						//ultimoitem = ultimoitem + 20;
						listview.setSelection(ultimoitem);
					}
				}
				
		    }
		}
    }
