package com.lojapro.candidato;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lojapro.can.ChecaConnexao;
import com.lojapro.can.ListaRegistros;
import com.lojapro.can.ParseEstadoXML;
import com.lojapro.can.XMLParseCidades;
import com.lojapro.candidato.AndroidMultiPartEntity.ProgressListener;
import com.lojapro.can.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/*
 * Esta classe será responsável por autenticar usuários que já foram candidatos
 */
public class ComRegistros extends Activity {
	 private EditText caixadebusca;
		private Spinner spinnerestados;
		private Spinner spinnercidades;
		private Activity act;
		private SharedPreferences sharedPref;
		private ProgressBar load;
		ListView list;
		private XMLParseCidades listacidades;
		private ListaRegistros lista;
		private boolean checa;
		private int idcid;
		private ArrayList<String> listacidadesparsed;
		private NodeList nl;
		private int[] idcidades;
		private ArrayList<HashMap<String, String>> songsList;
		private AdaptadorListaCandidatos adapter;
		private String idandroid;
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy);
	        act = this;
	        sharedPref = this.getSharedPreferences("com.lojapro.can", Context.MODE_PRIVATE);
	        idcid = sharedPref.getInt("cidade", 0);
	        setContentView(R.layout.comregistro);
	        load = (ProgressBar)findViewById(R.id.loadbar);
	        spinnercidades = (Spinner)findViewById(R.id.spinnercidade);
			spinnerestados = (Spinner)findViewById(R.id.SpinnerEstado);
			list=(ListView)findViewById(R.id.lista_busca_can);
			caixadebusca = (EditText)findViewById(R.id.nomecandidato);
			idandroid = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID); 
			
	}
	@Override 
	public void onStart() { 
		super.onStart(); 
	    carregapagina();
        
	}
	public void carregapagina(){
		ParseEstadoXML p = new ParseEstadoXML();	
	   	 String text = spinnerestados.getSelectedItem().toString();
	   	 int ide = p.retornaid(text,act);
			 listacidades = new XMLParseCidades(act, ide);
			 checa = new ChecaConnexao(act.getApplicationContext()).ativo();
			 
			  spinnerestados.setOnItemSelectedListener(new OnItemSelectedListener() {
				    @Override
				    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				    	//spinnercidades.setAdapter(null);
				    	ParseEstadoXML p = new ParseEstadoXML();
				    	String text = spinnerestados.getSelectedItem().toString();
				    	int ide = p.retornaid(text,act);
				    
				    	listacidades = new XMLParseCidades(act, ide);
				    	 if(!checa){
							 configuraconexao();
						 }else{
							 carregacidades();
						 }
				    }

				    @Override
				    public void onNothingSelected(AdapterView<?> parentView) {
				        // your code here
				    }

				});
		  load.setVisibility(View.GONE);
	   }
	public void configuraconexao(){
		Toast.makeText(act.getApplicationContext(), "Ative sua conexão com a internet", Toast.LENGTH_LONG).show();
        act.startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
	}
	public void carregacidades(){
		try {
			org.w3c.dom.Document d = listacidades.execute("http://ivoto.com.br/data_eleicoes/cidades.php").get();
			 listacidadesparsed = CidadesFromDoc(d);
			 System.out.println("Tamanho Cidade: " + listacidadesparsed.size());
			 ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,
		    	        android.R.layout.simple_spinner_item, listacidadesparsed);
		    	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    	    spinnercidades.setAdapter(adapter);
		    	    idcid = spinnercidades.getSelectedItemPosition();
		    	    spinnercidades.setOnItemSelectedListener(new OnItemSelectedListener() {
					    @Override
					    public void onItemSelected(AdapterView<?> pview, View selected, int pos, long id) {
					    	idcid = idcidades[pos];
					    	ListaCandidatos(idcid);
					    }

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
						}
		    	    });
		    	    
		    	    ListaCandidatos(idcid);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
	}
	public void ListaCandidatos(int idcidade){
		lista = new ListaRegistros(act);
  	  	songsList = lista.RetornaLista(idcidade);
  	  	adapter = new AdaptadorListaCandidatos(act, songsList);
  	  	caixadebusca.addTextChangedListener(new TextWatcher() {
          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {

        	  adapter.getFilter().filter(s);
        	 
          }

          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {

          	
          }

          @Override
          public void afterTextChanged(Editable s) {

          	
          }
      });
  	  	list.setAdapter(adapter);
  	    list.setOnItemClickListener(new OnItemClickListener() {

 			@Override
 			public void onItemClick(AdapterView<?> parent, View view,
 					int position, long id) {
 			    	String idcan = AdaptadorListaCandidatos.filtrado.get(position).get("id");
 			    	if(idcan == null){
 			    		idcan = "0";
 			    	}
 			    	System.out.println("Id candidato com r: " + idcan);
 			    	cadastracandidato(idcan);
 			    	
 	                
 			}
 		});
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
			Element nameElement = (Element) nameList.item(0);
			Element idelement = (Element) idlist.item(0);
			nameList = nameElement.getChildNodes();
			idlist = idelement.getChildNodes();
			String namevalue = ((Node) nameList.item(0)).getNodeValue();
			int idecid = Integer.parseInt(((Node) idlist.item(0)).getNodeValue());
			retorno.add(namevalue);
			idcidades[i] = idecid;
		}
		
		return retorno;
	}
	 public void cadastracandidato(String idcandidato){
	    @SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://ivoto.com.br/data_eleicoes/cadastra_candidato_existente.php");
	    String responseString;
			try {
	     	AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
	     			new ProgressListener() {
	     				@Override
	     				public void transferred(long num) {
	     				}
	     			});
	       entity.addPart("idandroid", new StringBody(idandroid));
	       entity.addPart("idcandidato", new StringBody(idcandidato));
	       httppost.setEntity(entity);
	       // Making server call
	       HttpResponse response = httpclient.execute(httppost);
	       HttpEntity r_entity = response.getEntity();
	       int statusCode = response.getStatusLine().getStatusCode();
	       	if (statusCode == 200) {
	       		// Server response
	       		sharedPref.edit().putInt("idcan", Integer.parseInt(idcandidato)).apply();
			    	Intent intent = new Intent(ComRegistros.this, Candidato.class);
	                startActivity(intent);
	            responseString = EntityUtils.toString(r_entity);
	            
	            finish();
	         } else {
	            responseString = "Erro codigo: "+ statusCode;
	            System.out.println("resposta: " + responseString);
	         }
	     } catch (ClientProtocolException e) {
	         responseString = e.toString();
	         System.out.println("resposta: " + responseString);
	     } catch (IOException e) {
	         responseString = e.toString();
	         System.out.println("resposta: " + responseString);
	     }
	}

}
