package com.lojapro.candidato;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;

public class Agenda extends Fragment {
	private TextView mensageminicial;
	private TextView mensagemajuda;
	private int idcan;
	public static String idandroid;
	private SharedPreferences sharedPref;
	private Activity act;
	// dados da lista
	private ArrayList<HashMap<String, String>> listaitens; // array
	private ListaAgenda lista; // classe
	private ListView listview; // view
	private AdaptadorAgenda adapt; // adaptador
	private int iditemativo = 0; 
	
	// dados do forulario
	private PopupWindow pw;
	private View popupView;
	private EditText contdata;
	private EditText conthora;
	private EditText conteudo;
	private TextView textView1;
	private ImageView botaocadastra;
	private ImageButton botaomais;
	private ImageButton imagemedita;
	private ImageButton imagemapaga;
	private ImageView botaosair;
	private String iditem = "0";
	
	// ui
	private ProgressBar load;
	private String Sdata;
	private String Shora;
	private String Sconteudo;
	private int screenw;
	private int screenh;
	
	@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
 
            View android = inflater.inflate(R.layout.agenda, container, false);
            act = getActivity();
            mensageminicial = (TextView)android.findViewById(R.id.textView1);
            mensagemajuda = (TextView)android.findViewById(R.id.textView2);
            idandroid = Secure.getString(act.getContentResolver(), Secure.ANDROID_ID);
            sharedPref = act.getSharedPreferences("com.lojapro.can", Context.MODE_PRIVATE);
            listview = (ListView)android.findViewById(R.id.listaagenda);
            // u.i.
            load = (ProgressBar)android.findViewById(R.id.loadbar);
            Display display = act.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenw = size.x;
            screenh = size.y;
            // dados do formulario
            pw = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, true);
    		  popupView = inflater.inflate(R.layout.popup_agenda, null);
    		  
    		  contdata = (EditText)popupView.findViewById(R.id.data);
    		  conthora = (EditText)popupView.findViewById(R.id.hora);
    		  conteudo = (EditText)popupView.findViewById(R.id.textoconteudo);
    		  
    		  botaocadastra = (ImageButton) popupView.findViewById(R.id.fab_image_button_save);
    		  botaosair = (ImageButton) popupView.findViewById(R.id.fab_image_button_cancel);
    		  imagemedita = (ImageButton) android.findViewById(R.id.fab_image_button_edit);
    		  imagemapaga = (ImageButton) android.findViewById(R.id.fab_image_button_remove);
    		  imagemedita.setVisibility(View.INVISIBLE);
    		  imagemapaga.setVisibility(View.INVISIBLE);
    		  botaomais = (ImageButton) android.findViewById(R.id.fab_image_button);
    		  // funções do formulário
    		  conthora.setOnClickListener(new OnClickListener() {

    		        @Override
    		        public void onClick(View v) {
    		            Calendar mcurrentTime = Calendar.getInstance();
    		            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    		            int minute = mcurrentTime.get(Calendar.MINUTE);
    		            TimePickerDialog mTimePicker;
    		            mTimePicker = new TimePickerDialog(act, new TimePickerDialog.OnTimeSetListener() {
    		                
    		            	@Override
    		                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
    		                	conthora.setText( selectedHour + ":" + selectedMinute +":00");
    		                }

    		            }, hour, 0, false);
    		            mTimePicker.setTitle("Selecione uma hora");
    		            mTimePicker.show();

    		        }
    		    });
    		  contdata.setOnClickListener(new OnClickListener() {

  		        @Override
  		        public void onClick(View v) {
  		         
  		        	// TODO Auto-generated method stub
  		            Calendar mcurrentdate = Calendar.getInstance();
  		            int dia = mcurrentdate.get(Calendar.DAY_OF_MONTH);
  		            int mes = mcurrentdate.get(Calendar.MONTH);
  		            int ano = mcurrentdate.get(Calendar.YEAR);
  		            
  		            DatePickerDialog mTimePicker;
  		            mTimePicker = new DatePickerDialog(act, new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int ano, int mes, int dia) {
							contdata.setText( dia + "/" + mes +"/"+ano);
							
						}
  		                

  		            }, ano, mes, dia);
  		            mTimePicker.setTitle("Selecione uma data");
  		            mTimePicker.show();

  		        }
  		    });
    		  botaomais.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
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
    				lista = new ListaAgenda(act);
    				listaitens = lista.RetornaLista();
    			
    				// if exists
    				int qtitens = listaitens.size();
    				mensagemajuda.setText("Nesta seção você poderá manter atualizada sua agenda para facilitar o contato com os eleitores");
    			if(qtitens > 0){
    				if(qtitens > 1){
    					mensageminicial.setText(qtitens + " encontrados, clique no ícone abaixo para adicionar registros em sua agenta");
    				}else{
    					mensageminicial.setText(qtitens + " encontrado, clique no ícone abaixo para adicionar registros em sua agenta");
    				}
    				// load itens
    				carregaitens();
    			}else{
    				// se não mostra mensagem
    				mensageminicial.setText("Nada inserido ainda, clique no ícone abaixo para adicionar itens em sua agenda");
    				load.setVisibility(View.GONE);
    			}
    }
	private void carregaitens() {
		adapt = new AdaptadorAgenda(act, listaitens);
		listview.setAdapter(adapt);
		load.setVisibility(View.GONE);
		listview.setOnItemClickListener(new OnItemClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
				
				imagemapaga.setVisibility(View.VISIBLE);
				imagemedita.setVisibility(View.VISIBLE);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						imagemapaga.getWidth(), imagemapaga.getHeight());
						if(view.getY() < (screenh/2)){
						params.topMargin = (int) (view.getY() + view.getHeight() + 30);
						}else{
						params.topMargin = (int) (view.getY() - view.getHeight() - 60);
						}
						params.leftMargin = 5;
						imagemapaga.setLayoutParams(params);
						RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
						imagemapaga.getWidth(), imagemapaga.getHeight());
							if(view.getY() < (screenh/2)){
							params2.topMargin = (int) (view.getY() + view.getHeight() + 30);
							}else{
							params2.topMargin = (int) (view.getY() - view.getHeight() - 60);
							}
					
						params2.leftMargin = 5 + imagemedita.getWidth() + 10;
						
						imagemedita.setLayoutParams(params2);
						for (int i = 0;  i < AdaptadorAgenda.data.size(); i++) {
						    // do some work here on intValue
							if(i != position){
							AdaptadorAgenda.listb.set(i, false);
							}else{
								if(AdaptadorAgenda.listb.get(position)){
									AdaptadorAgenda.listb.set(position, false);
									imagemapaga.setVisibility(View.GONE);
									imagemedita.setVisibility(View.GONE);
									botaomais.setVisibility(View.VISIBLE);
								}else{
									AdaptadorAgenda.listb.set(position, true);
									imagemapaga.setVisibility(View.VISIBLE);
									imagemedita.setVisibility(View.VISIBLE);
									botaomais.setVisibility(View.GONE);
								}
							}
						}
						
			    int iditem = Integer.parseInt(AdaptadorAgenda.data.get(position).get("id"));
			    iditemativo = iditem;
			    Sdata = AdaptadorAgenda.data.get(position).get("data");
				Shora = AdaptadorAgenda.data.get(position).get("hora");
				Sconteudo = AdaptadorAgenda.data.get(position).get("descricao");
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
			HttpPost httppost = new HttpPost("http://ivoto.com.br/data_eleicoes/cadastraagenda.php");

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
	     	String conteudodata = contdata.getText().toString();
	     	String conteudohora = conthora.getText().toString();
	     	System.out.println("Cadastrando item na agenda: " + conteudodata);
	     	entity.addPart("id_item", new StringBody(iditem));
	     	entity.addPart("id_android", new StringBody(idandroid));
	     	entity.addPart("data", new StringBody(conteudodata, contentType));
	     	entity.addPart("hora", new StringBody(conteudohora, contentType));
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
			contdata.setText(Sdata);
			conthora.setText(Shora);
			conteudo.setText(Sconteudo);
			iditem = Integer.toString(iditemativo);
			abreformulario();
			
		}
		private void apagaitem() {
			@SuppressWarnings("resource")
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://ivoto.com.br/data_eleicoes/apagaagenda.php");
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

