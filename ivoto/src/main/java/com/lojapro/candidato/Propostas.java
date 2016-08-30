package com.lojapro.candidato;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.lojapro.can.CollectionDemoActivity;
import com.lojapro.can.LazyAdapter;
import com.lojapro.can.ListaCandidatos;
import com.lojapro.can.Principal;
import com.lojapro.candidato.AndroidMultiPartEntity.ProgressListener;
import com.lojapro.can.R;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class Propostas extends Fragment {
	SharedPreferences sharedPref;
	Activity act;
	
	public static String idandroid;
	private ListView list;
	
	private AdaptadorPropostas adapt;
	private ArrayList<HashMap<String, String>> retorno;
	private ListaPropostas lista;
	// dados do forulario
		private PopupWindow pw;
		private View popupView;
		private EditText titulo;
		private EditText conteudo;
		private TextView textView1;
		private String idfeito = "0";
		private int iditemativo = 0;
		private String Stitulo;
		private String Sconteudo;
		
	// u.i
	private ProgressBar load;
	private ImageView botaocadastra;
	private ImageButton botaomais;
	private ImageButton botaosair;
	private ImageButton imagemedita;
	private ImageButton imagemapaga;
	private ImageButton imagemvolta;
	private int screenw;
	private int screenh;
	private ArrayList<HashMap<String, String>> listaitens;
	private TextView mensageminicial;
	private TextView mensagemajuda;
	@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View android = inflater.inflate(R.layout.propostas, container, false);
		
		act = getActivity();
		sharedPref = act.getSharedPreferences("com.lojapro.can", Context.MODE_PRIVATE);
		mensageminicial = (TextView)android.findViewById(R.id.textView1);
		mensagemajuda = (TextView)android.findViewById(R.id.textView2);
		idandroid = Secure.getString(act.getContentResolver(), Secure.ANDROID_ID);
		pw = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, true);
		popupView = inflater.inflate(R.layout.popup_proposta, null);
		titulo = (EditText)popupView.findViewById(R.id.textotitulo);
		conteudo = (EditText)popupView.findViewById(R.id.textoconteudo);
		list = (ListView)android.findViewById(R.id.lista_propostas);
		lista = new ListaPropostas(act);
		textView1 = (TextView)android.findViewById(R.id.textView1);
		  // u.i.
        load = (ProgressBar)android.findViewById(R.id.loadbar);
        Display display = act.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenw = size.x;
        screenh = size.y;
		  botaocadastra = (ImageButton) popupView.findViewById(R.id.fab_image_button_save);
		  botaosair = (ImageButton) popupView.findViewById(R.id.fab_image_button_cancel);
		  imagemedita = (ImageButton) android.findViewById(R.id.fab_image_button_edit);
		  imagemapaga = (ImageButton) android.findViewById(R.id.fab_image_button_remove);
		  imagemvolta = (ImageButton) android.findViewById(R.id.fab_image_button_cancel);
		  imagemedita.setVisibility(View.INVISIBLE);
		  imagemapaga.setVisibility(View.INVISIBLE);
		  imagemvolta.setVisibility(View.INVISIBLE);
		  botaomais = (ImageButton) android.findViewById(R.id.fab_image_button);
		  botaocadastra.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                cadastraitem();
	            }
	  		  });
	  		  botaosair.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                pw.dismiss();
	            }
	  		  });
		  botaomais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	abreformulario();
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
    				lista = new ListaPropostas(act);
    				listaitens = lista.RetornaLista();
    			
    				// if exists
    				int qtitens = listaitens.size();
    				mensagemajuda.setText("Esta seção é essencial para o candidato, as propostas inclusas aqui "
    						+ "serão divulgadas tanto no aplicativo móvel quanto no site ivoto, e poderão ser"
    						+ " avaliadas pelos eleitores.");
    			if(qtitens > 0){
    				mensageminicial.setText(qtitens + " encontrados, clique no ícone verde para adicionar realizações");
    				// load itens
    				carregaitens();
    			}else{
    				// se não mostra mensagem
    				mensageminicial.setText("Nada inserido ainda, clique no ícone abaixo para adicionar e divulgar suas propostas e idéias");
    				load.setVisibility(View.GONE);
    			}
    }
	
    private void carregaitens() {
    	adapt = new AdaptadorPropostas(act, listaitens);
		load.setVisibility(View.GONE);
		list.setAdapter(adapt);
        list.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
				imagemapaga.setVisibility(View.VISIBLE);
				imagemedita.setVisibility(View.VISIBLE);
				imagemvolta.setVisibility(View.VISIBLE);
				
				
				
				for (int i = 0;  i < AdaptadorPropostas.data.size(); i++) {
				    
					if(i != position){
					AdaptadorPropostas.listb.set(i, false);
					}else{
						if(AdaptadorPropostas.listb.get(position)){
							AdaptadorPropostas.listb.set(position, false);
							imagemapaga.setVisibility(View.GONE);
							imagemedita.setVisibility(View.GONE);
							imagemvolta.setVisibility(View.GONE);
							botaomais.setVisibility(View.VISIBLE);
						}else{
							AdaptadorPropostas.listb.set(position, true);
							imagemapaga.setVisibility(View.VISIBLE);
							imagemedita.setVisibility(View.VISIBLE);
							imagemvolta.setVisibility(View.VISIBLE);
							botaomais.setVisibility(View.GONE);
						}
					}
				}
				
	    int iditem = Integer.parseInt(AdaptadorPropostas.data.get(position).get("id"));
	    iditemativo = iditem;
	    Stitulo = AdaptadorPropostas.data.get(position).get("titulo");
		Sconteudo = AdaptadorPropostas.data.get(position).get("conteudo");
		imagemapaga.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		load.setVisibility(View.VISIBLE);
	    		imagemedita.setVisibility(View.GONE);
				imagemvolta.setVisibility(View.GONE);
				imagemapaga.setVisibility(View.GONE);
				apagaitem();
			}
		});
		imagemedita.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		imagemapaga.setVisibility(View.GONE);
				imagemedita.setVisibility(View.GONE);
				imagemvolta.setVisibility(View.GONE);
	    		editaitem();
			}
		});
		imagemvolta.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		imagemapaga.setVisibility(View.GONE);
				imagemedita.setVisibility(View.GONE);
				imagemvolta.setVisibility(View.GONE);
				botaomais.setVisibility(View.VISIBLE);
				AdaptadorPropostas.listb.set(position, false);
			}
		});
				RatingBar rat =  ((RatingBar)view.findViewById(R.id.ratingBar1));
				rat.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					public void onRatingChanged(RatingBar ratingBar, float rating,
						boolean fromUser) {
						String idproposta = AdaptadorPropostas.data.get(position).get("id");
						rateproposta(idproposta , Float.toString(rating));

					}
				});
				
				adapt.notifyDataSetChanged();
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(imagemapaga.getWidth(), imagemapaga.getHeight());
				params.topMargin = (int) (screenh - imagemapaga.getHeight() - 200);
				params.leftMargin = 5;
				imagemapaga.setLayoutParams(params);
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(imagemapaga.getWidth(), imagemapaga.getHeight());
				params2.topMargin = (int) (screenh - imagemedita.getHeight() - 200);
				params2.leftMargin = 5 + imagemedita.getWidth() + 10;
				imagemedita.setLayoutParams(params2);
				RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(imagemvolta.getWidth(), imagemvolta.getHeight());
				params3.topMargin = (int) (screenh - imagemvolta.getHeight() - 200);
				params3.leftMargin = 5 + (imagemvolta.getWidth() * 2) + 20;
				imagemvolta.setLayoutParams(params3);
			
			}
		
        });
		
    }
	public void mensagemnegado(){
		System.out.println("Negado o acesso");
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
             pw.dismiss();
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
	private void editaitem() {
		titulo.setText(Stitulo);
		conteudo.setText(Sconteudo);
		idfeito = Integer.toString(iditemativo);
		abreformulario();
		
	}
	public void cadastraitem(){
	    @SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient();
     HttpPost httppost = new HttpPost("http://ivoto.com.br/data_eleicoes/cadastraproposta.php");

     String responseString;
		try {
     	AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                 new ProgressListener() {
                     @Override
                     public void transferred(long num) {
                         
                     }
                 });
     	ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8); 
     	String conteudoproposta = conteudo.getText().toString();
     	 String tituloproposta = titulo.getText().toString();
         entity.addPart("id_android", new StringBody(idandroid));
         entity.addPart("titulo", new StringBody(tituloproposta, contentType));
         entity.addPart("conteudo", new StringBody(conteudoproposta, contentType));
        
         httppost.setEntity(entity);
         // Making server call
         HttpResponse response = httpclient.execute(httppost);
         HttpEntity r_entity = response.getEntity();

         int statusCode = response.getStatusLine().getStatusCode();
         if (statusCode == 200) {
             // Server response
             responseString = EntityUtils.toString(r_entity);
             pw.dismiss();
             precarregaitens();
             carregaitens();
             
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
	public void abreformulario(){
		pw.setWidth((int)(screenw * 0.97f));
		pw.setHeight((int)(screenh * 0.90f));
		pw.setOutsideTouchable(false);
        pw.setContentView(popupView);
        pw.showAtLocation(getView(), Gravity.CENTER, 0, 0);
	}
	private void apagaitem() {
		@SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://ivoto.com.br/data_eleicoes/apagaproposta.php");
		String responseString;
		try {
     	AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                 new ProgressListener() {
                     @Override
                     public void transferred(long num) {
                         
                     }
                 });
     	ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8); 
     	entity.addPart("id_android", new StringBody(idandroid));
     	entity.addPart("id_item", new StringBody(Integer.toString(iditemativo)));
        httppost.setEntity(entity);
        // Making server call
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity r_entity = response.getEntity();
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
             // Server response
             responseString = EntityUtils.toString(r_entity);
             
             precarregaitens();
             carregaitens();
             load.setVisibility(View.GONE);
        } else {
             responseString = "Erro codigo: " + statusCode;
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

