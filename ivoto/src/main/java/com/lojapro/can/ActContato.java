package com.lojapro.can;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.lojapro.can.R;

public class ActContato extends Activity {
	LazyAdapter adapter;
    private String idcandidato;
    private TextView textocontato; 
    private EditText mensagem;
    private ImageView atualiza;
    private ImageView botaocancelar;
    private String android_id;
    private String URL_CONTATO = "http://ivoto.com.br/data_eleicoes/contatocandidato.php";
    private Activity act;
	private String nomecandidato; 
    @Override
          public void onCreate(Bundle savedInstanceState) {
          
          super.onCreate(savedInstanceState);
	      setContentView(R.layout.actcontato);
	      act = this;
	      android_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
	      Intent i = getIntent();
	      
	      idcandidato = i.getExtras().getString("idcandidato");
	      nomecandidato = i.getExtras().getString("nomecandidato");
	      textocontato = (TextView) findViewById(R.id.textocontato);
	      textocontato.setText("Enviar mensagem para " + nomecandidato);
	      mensagem = (EditText)findViewById(R.id.mensagem);
	      atualiza = (ImageView)findViewById(R.id.atualiza);
	      botaocancelar = (ImageView)findViewById(R.id.botaocancelar);
	      botaocancelar.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                finish();
	            }
	      });
	      atualiza.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	String msg = mensagem.getText().toString();
	            	new ContatoCandidato(act, idcandidato, android_id, msg).execute(URL_CONTATO);
	            	finish();
	            }
	      });
	   }
	    
	     @Override
		   protected void onResume() {
		      super.onResume();
		
		}
	}