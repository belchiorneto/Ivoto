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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class AvaliarProposta extends Fragment {
	SharedPreferences sharedPref;
	Activity act;
	public static String idandroid;
	public static int idcidade;
	private ListView list;
	private AdaptadorPropostasEleitor adapt;
	private boolean isViewShown = false;
	private ProgressBar load;
	@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
 			View android = inflater.inflate(R.layout.avaliarproposta, container, false);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy); 
            act = getActivity();
            sharedPref = act.getSharedPreferences("com.lojapro.can", Context.MODE_PRIVATE);
            idcidade = sharedPref.getInt("cidade", 0);
            idandroid = Secure.getString(act.getContentResolver(), Secure.ANDROID_ID);
            list = (ListView)android.findViewById(R.id.lista_propostas);
            load = (ProgressBar)android.findViewById(R.id.loadbar);
            return android;
    	}
	@Override 
	public void onStart() { 
		super.onStart(); 
		listapropostas();
        load.setVisibility(View.GONE);
	} 
		
		public void listapropostas(){
		
			ListaPropostasEleitor lista = new ListaPropostasEleitor(act);
    		ArrayList<HashMap<String, String>> retorno = new ArrayList<HashMap<String, String>>();
    		retorno = lista.RetornaLista();
			adapt = new AdaptadorPropostasEleitor(act, retorno);
		list.setAdapter(adapt);
        list.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
				// Collections.fill(AdaptadorPropostas.listb, Boolean.FALSE);
			    if(AdaptadorPropostasEleitor.listb.get(position) == false){ 
			    	AdaptadorPropostasEleitor.listb.set(position, true);
			    }else{
			    	AdaptadorPropostasEleitor.listb.set(position, false);
			    }
				

				RatingBar rat =  ((RatingBar)view.findViewById(R.id.ratingBar1));
				rat.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					public void onRatingChanged(RatingBar ratingBar, float rating,
						boolean fromUser) {
						String idproposta = AdaptadorPropostasEleitor.data.get(position).get("id");
						rateproposta(idproposta , Float.toString(rating));

					}
				});
				adapt.notifyDataSetChanged();
			}
		});
		
    }
public void rateproposta(String pos, String rating){
	@SuppressWarnings("resource")
	HttpClient httpclient = new DefaultHttpClient();
 HttpPost httppost = new HttpPost("http://ivoto.com.br/data_eleicoes/rateproposta.php");

 String responseString;
	try {
 	AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
             new ProgressListener() {
                 @Override
                 public void transferred(long num) {
                     
                 }
             });
 	 
     entity.addPart("id_android", new StringBody(idandroid));
     entity.addPart("id_proposta", new StringBody(pos));
     entity.addPart("rating", new StringBody(rating));
    
     httppost.setEntity(entity);
     // Making server call
     HttpResponse response = httpclient.execute(httppost);
     HttpEntity r_entity = response.getEntity();

     int statusCode = response.getStatusLine().getStatusCode();
     if (statusCode == 200) {
         // Server response
         responseString = EntityUtils.toString(r_entity);
       
     } else {
         responseString = "Erro codigo: "
                 + statusCode;
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