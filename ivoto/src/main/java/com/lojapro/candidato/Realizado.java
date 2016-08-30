package com.lojapro.candidato;

import java.io.IOException;
import java.util.ArrayList;
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

import com.lojapro.candidato.AndroidMultiPartEntity.ProgressListener;
import com.lojapro.can.R;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


	public class Realizado extends Fragment {
    private TextView mensageminicial;
    private TextView mensagemajuda;
	private int idcan;
	public static String idandroid;
	private SharedPreferences sharedPref;
	private Activity act;
	// dados da lista
	private ArrayList<HashMap<String, String>> listaitens; // array
	private ListaRealizado lista; // classe
	private ListView listview; // view
	private AdaptadorRealizado adapt; // adaptador
	
	// dados do forulario
	private PopupWindow pw;
	private View popupView;
	private EditText titulo;
	private EditText conteudo;
	private String idfeito = "0";
	private int iditemativo = 0;
	private String Stitulo;
	private String Sconteudo;
	
	// ui
	private ProgressBar load;
	private ImageButton botaocadastra;
	private ImageButton botaosair;
	private ImageButton botaomais;
	private ImageButton imagemedita;
	private ImageButton imagemapaga;
	private int screenw;
	private int screenh;
	
	
	@Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
          View android = inflater.inflate(R.layout.realizado, container, false);
          act = getActivity();
          mensageminicial = (TextView)android.findViewById(R.id.textView1);
          mensagemajuda = (TextView)android.findViewById(R.id.textView2);
          idandroid = Secure.getString(act.getContentResolver(), Secure.ANDROID_ID);
          sharedPref = act.getSharedPreferences("com.lojapro.can", Context.MODE_PRIVATE);
          listview = (ListView)android.findViewById(R.id.listafeitos);
          // u.i.
          load = (ProgressBar)android.findViewById(R.id.loadbar);
          Display display = act.getWindowManager().getDefaultDisplay();
          Point size = new Point();
          display.getSize(size);
          screenw = size.x;
          screenh = size.y;
          // dados do formulario
          pw = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, true);
  		  popupView = inflater.inflate(R.layout.popup_realizado, null);
  		  titulo = (EditText)popupView.findViewById(R.id.textotitulo);
  		  conteudo = (EditText)popupView.findViewById(R.id.textoconteudo);
  		  botaocadastra = (ImageButton) popupView.findViewById(R.id.fab_image_button_save);
		  botaosair = (ImageButton) popupView.findViewById(R.id.fab_image_button_cancel);
		  imagemedita = (ImageButton) android.findViewById(R.id.fab_image_button_edit);
		  imagemapaga = (ImageButton) android.findViewById(R.id.fab_image_button_remove);
		  imagemedita.setVisibility(View.INVISIBLE);
		  imagemapaga.setVisibility(View.INVISIBLE);
		  botaomais = (ImageButton) android.findViewById(R.id.fab_image_button);
		  // funções do formulário
  		  botaomais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	idfeito = "0";
            	titulo.setText("");
            	conteudo.setText("");
            	abreformulario();
            }
  		  });
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
  		  
          
  		  String idc = sharedPref.getString("idcan",null);
          if(idc != null){
        	  idcan = Integer.parseInt(idc);
          }
          return android;
   	}
    @Override 
	public void onStart() { 
		super.onStart();
		precarregaitens();
	}
    private void precarregaitens(){
    	// List tens
    				lista = new ListaRealizado(act);
    				listaitens = lista.RetornaLista();
    			
    				// if exists
    				int qtitens = listaitens.size();
    			mensagemajuda.setText("Nesta seção você pode incluir qualquer informação importante para seu eleitor relacionada à sua atividade em benefício da comunidade");
    			if(qtitens > 0){
    				mensageminicial.setText(qtitens + " encontrados, clique no ícone verde para adicionar realizações");
    				// load itens
    				carregaitens();
    			}else{
    				// se não mostra mensagem
    				mensageminicial.setText("Nada inserido ainda, clique no ícone verde para adicionar realizações");
    				load.setVisibility(View.GONE);
    			}
    }
	
	private void carregaitens() {
		adapt = new AdaptadorRealizado(act, listaitens);
		listview.setAdapter(adapt);
		load.setVisibility(View.GONE);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
				
				imagemapaga.setVisibility(View.VISIBLE);
				imagemedita.setVisibility(View.VISIBLE);
				
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(imagemapaga.getWidth(), imagemapaga.getHeight());
				params.topMargin = (int) (screenh - imagemedita.getHeight() - 200);
				params.leftMargin = 5;
				imagemapaga.setLayoutParams(params);
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(imagemapaga.getWidth(), imagemapaga.getHeight());
				params2.topMargin = (int) (screenh - imagemedita.getHeight() - 200);
				params2.leftMargin = 5 + imagemedita.getWidth() + 10;
				imagemedita.setLayoutParams(params2);
				for (int i = 0;  i < AdaptadorRealizado.data.size(); i++) {
					if(i != position){
						AdaptadorRealizado.listb.set(i, false);
					}else{
						if(AdaptadorRealizado.listb.get(position)){
							AdaptadorRealizado.listb.set(position, false);
							imagemapaga.setVisibility(View.GONE);
							imagemedita.setVisibility(View.GONE);
							botaomais.setVisibility(View.VISIBLE);
						}else{
							AdaptadorRealizado.listb.set(position, true);
							imagemapaga.setVisibility(View.VISIBLE);
							imagemedita.setVisibility(View.VISIBLE);
							botaomais.setVisibility(View.GONE);
						}
					}
				}
						
			    int iditem = Integer.parseInt(AdaptadorRealizado.data.get(position).get("id"));
			    iditemativo = iditem;
			    Stitulo = AdaptadorRealizado.data.get(position).get("titulo");
				Sconteudo = AdaptadorRealizado.data.get(position).get("conteudo");
				imagemapaga.setOnClickListener(new View.OnClickListener() {
			    	public void onClick(View v) {
			    		imagemapaga.setVisibility(View.GONE);
						imagemedita.setVisibility(View.GONE);
						load.setVisibility(View.VISIBLE);
			    		apagaitem();
					}
				});
				imagemedita.setOnClickListener(new View.OnClickListener() {
			    	public void onClick(View v) {
			    		imagemapaga.setVisibility(View.GONE);
						imagemedita.setVisibility(View.GONE);
			    		editaitem();
					}
				});
				
			    adapt.notifyDataSetChanged();
			}
		});
	
	}
	
	// funções do formulário
	private void abreformulario() {
		pw.setWidth((int)(getView().getWidth() * 0.97f));
		pw.setHeight((int)(getView().getHeight() * 0.90f));
		pw.setOutsideTouchable(false);
        pw.setContentView(popupView);
        pw.showAtLocation(getView(), Gravity.CENTER, 0, 0);
	}
	
	private void cadastraitem() {
		@SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://ivoto.com.br/data_eleicoes/cadastrafeito.php");

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
     	entity.addPart("id_feito", new StringBody(idfeito));
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
	private void editaitem() {
		titulo.setText(Stitulo);
		conteudo.setText(Sconteudo);
		idfeito = Integer.toString(iditemativo);
		abreformulario();
		
	}
	private void apagaitem() {
		@SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://ivoto.com.br/data_eleicoes/apagafeito.php");
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
     	entity.addPart("id_feito", new StringBody(Integer.toString(iditemativo)));
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
