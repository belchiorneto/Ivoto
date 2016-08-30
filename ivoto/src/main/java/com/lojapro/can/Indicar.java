package com.lojapro.can;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lojapro.adaptadores.Cidades;
import com.lojapro.buscas.ListaCidadesBuscadas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


@SuppressLint("HandlerLeak")
public class Indicar  extends Fragment {
	// busca de cidades
	private ListView lv_cidades;
	// adaptador cidades
	Cidades cidadeadapter;
	
    // ArrayList para listview cidades
	private ArrayList<HashMap<String, String>> lista_cidades;
   
	SharedPreferences sharedPref;
	static ProgressDialog pd;
	GPSTracker gps;
	
	 static double latitude = 0;
	 static double longitude = 0;
	 
	 // variaveis do endere�o
	 static String rua;
	 static String numero;
	 static String bairro;
	 static String cidade;
	 static String estado;
	 static String cep;
	 static String pais;
	// All static variables
	static final String URL = "http://ivoto.com.br/data_eleicoes/ws.php";
	static LazyAdapter adapter;
	NodeList nl;
	ListaCidadesBuscadas listacidades;
	public static ArrayList<HashMap<String, String>> songsList;
	ListaRegistros lista;
	int idcid = 0;
	ListView list;
	int[] idcidades;
	View and;
	ProgressBar load;
	// create boolean for fetching data
	Context ctx;
	Activity act;

	// Search view configs
	private SearchView searchView;
	private static MenuItem mSearchMenuItem;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		getActivity();
		act = getActivity();
		ctx = getActivity().getApplicationContext();
		sharedPref = getActivity().getSharedPreferences("com.lojapro.can", Context.MODE_PRIVATE);
		getActivity().setTitle("Indicar candidatos");
		setHasOptionsMenu(true);

		and = inflater.inflate(R.layout.main, container, false);
		load = (ProgressBar)and.findViewById(R.id.loadbar);

		 list=(ListView)and.findViewById(R.id.lista_can);
		 idcid = sharedPref.getInt("cidade", 0);
		 listacidades = new ListaCidadesBuscadas(getActivity(), "");
		 lv_cidades = (ListView)and.findViewById(R.id.list_cidades);
		
		 
	    Button share = (Button)and.findViewById(R.id.sharebut);
	    share.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
            	//create the send intent
            	Intent shareIntent = 
            	 new Intent(android.content.Intent.ACTION_SEND);

            	//set the type
            	shareIntent.setType("text/plain");

            	//add a subject
            	shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, 
            	 "Eleições 2016");

            	//build the body of the message to be shared
            	String shareMessage = "Veja quem são os pré-candidatos para as eleições 2016 em sua cidade. https://play.google.com/store/apps/details?id=com.lojapro.can";

            	//add the message
            	shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
            	 shareMessage);

            	//start the chooser for sharing
            	startActivity(Intent.createChooser(shareIntent, 
            	 "Recomendar para um amigo"));
            }
	    });
	 
	    carregapagina("");
        if(idcid > 0){
        	ListaCandidatos(idcid, "");
        }else{
        	mostracidadeescolha();
        }
	    return and;
       
	}
	
	@Override 
	public void onStart() { 
		super.onStart(); 
	 	
        load.setVisibility(View.GONE);
	}
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		mSearchMenuItem = menu.findItem(R.id.search_field_eleitor);
		searchView = (SearchView) mSearchMenuItem.getActionView();
		final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

		SearchManager searchManager = (SearchManager) act.getSystemService(act.SEARCH_SERVICE);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(act.getComponentName()));
		MenuItemCompat.setShowAsAction(mSearchMenuItem,
				MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW |
						MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
		MenuItemCompat.setActionView(mSearchMenuItem, searchView);
		searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean queryTextFocused) {
				if(view.hasFocus()) {
					actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
				}else{
					actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
					searchView.setQuery("", false);
				}
			}
		});
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String queryText) {
				// Nothing needs to happen when the user submits the search string
				return true;
			}

			@Override
			public boolean onQueryTextChange(String SearchTerm) {
				final Handler handler = new Handler();
				final String searchterm = SearchTerm;
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						//Do something after 100ms
						Log.d("Search menu:" , "Buscando ... " + searchterm);
						ListaCandidatos(idcid, searchterm);
					}
				}, 500);

				return true;
			}
		});
		Log.d("Search menu:" , "Search: " + searchView);
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);

	}	
	
    
	public static void mostracidadeescolha(){
		mSearchMenuItem.expandActionView();
	}

	public void carregapagina(String SerachText){
		carregacidades(SerachText);

   }

	public void configuraconexao(){
		Toast.makeText(getActivity().getApplicationContext(), "Ative sua conexão com a internet", Toast.LENGTH_LONG).show();
        getActivity().startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
	}
	public void ListaCandidatos(int idcidade, String SearchText){
		registracidade(idcidade);
	   	if(adapter == null){
			lista = new ListaRegistros(getActivity());
			songsList = lista.RetornaLista(idcidade);
			adapter = new LazyAdapter(getActivity(), songsList);
		}else {
			adapter.getFilter().filter(SearchText);
			lista = new ListaRegistros(getActivity());
			songsList = lista.RetornaLista(idcidade);
			adapter = new LazyAdapter(getActivity(), songsList);
		}
  	  	list.setAdapter(adapter);
	}
	public static void updateadapter(){
		 adapter.notifyDataSetChanged(); 
	}
	
	@Override
	public void onResume() {
	      super.onResume();
	      /*
	      if(progressDialog.isShowing()){
				progressDialog.dismiss();
		  }
		  */
	}

	public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
	public void carregacidades(String busca){
			ArrayList<HashMap<String, String>> d;
			ListaCidadesBuscadas l = new ListaCidadesBuscadas(ctx, busca);
			d = l.RetornaLista();
			//= listacidades.execute("http://ivoto.com.br/data_eleicoes/cidades.php", busca);
			
			//listacidadesparsed = CidadesFromDoc(d);
		 	//adicionando itens para lista de cidades
			cidadeadapter = new Cidades(getActivity(), d);
			lv_cidades.setVisibility(View.VISIBLE);
			ViewGroup.LayoutParams params = lv_cidades.getLayoutParams();
		    params.height = 200;
		    lv_cidades.setLayoutParams(params);
		    lv_cidades.requestLayout();
			lv_cidades.setAdapter(cidadeadapter);  
			lv_cidades.setClickable(true);
			lv_cidades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		        @Override
		        public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		        	
		        	View nextChild = ((ViewGroup)v).getChildAt(0);
		        	String t1 = nextChild.getTag().toString();
		        	try{
		        		idcid = Integer.parseInt(t1.toString());
		        		if(idcid > 0){
							registracidade(idcid);
							ListaCandidatos(idcid, "");
		        		}
		        	}catch (NullPointerException e) {
		        		e.printStackTrace();
		            }
		        }
		    });
		   
	}    	    

	private void registracidade(int idcidade){
		Editor editor = sharedPref.edit();
		editor.putInt("cidade", idcid).commit();
		ViewGroup.LayoutParams params = lv_cidades.getLayoutParams();
		params.height = 5;
		lv_cidades.setLayoutParams(params);
		lv_cidades.requestLayout();
		lv_cidades.setVisibility(View.GONE);
	}
	void processFinish(String output){
	     //this you will received result fired from async class of onPostExecute(result) method.
		
	}
	
	
	  private ArrayList<String> CidadesFromDoc(org.w3c.dom.Document doc)
		{
			nl = doc.getElementsByTagName("cidade");
			ArrayList<String> retorno = new ArrayList<String>();
			idcidades = new int[nl.getLength()];
			for(int i = 0; i<nl.getLength(); i++){
				Node node = nl.item(i);
				Element fstElmnt = (Element) node;
				NodeList nameList = fstElmnt.getElementsByTagName("nome");
				NodeList idlist = fstElmnt.getElementsByTagName("id");
				NodeList estado = fstElmnt.getElementsByTagName("estado");
				Element nameElement = (Element) nameList.item(0);
				Element idelement = (Element) idlist.item(0);
				Element nameEstado = (Element) estado.item(0);
				nameList = nameElement.getChildNodes();
				idlist = idelement.getChildNodes();
				estado = nameEstado.getChildNodes();
				String namevalue = ((Node) nameList.item(0)).getNodeValue() +"/"+((Node) estado.item(0)).getNodeValue();
				int idecid = Integer.parseInt(((Node) idlist.item(0)).getNodeValue());
				retorno.add(namevalue);
				idcidades[i] = idecid;
			}
			
			return retorno;
		}
	
}
