package com.lojapro.can;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.lojapro.can.R;

public class ActSigle extends Activity {
	LazyAdapter adapter;
    public ImageLoader imageLoader;
    private ProgressDialog progressDialog;

    @Override
          public void onCreate(Bundle savedInstanceState) {
          imageLoader = new ImageLoader(this);
          progressDialog = new ProgressDialog(ActSigle.this);
          super.onCreate(savedInstanceState);
	      setContentView(R.layout.item);
	      TextView tnome = (TextView) findViewById(R.id.nome);
             TextView tpartido = (TextView) findViewById(R.id.partido);
             TextView tnumero = (TextView) findViewById(R.id.numero);
             TextView tcargo = (TextView) findViewById(R.id.cargo);
          ImageView thumb_image = (ImageView) findViewById(R.id.SingleView);
	      
	      // Get intent data
	      Intent i = getIntent();
	      
	      i.getExtras().getInt("id");
          String nome = i.getStringExtra("idtnome");
             String numero = i.getStringExtra("idtcargo");
             String partido = i.getStringExtra("idtnumero");
             tnome.setText(nome);
             tpartido.setText(partido);
             tnumero.setText(numero);
             tcargo.setText(numero);
             imageLoader.DisplayImage(i.getStringExtra("url"), thumb_image);
	     
	   }
	    
	     @Override
		   protected void onResume() {
		      super.onResume();
		      if(progressDialog.isShowing()){
					progressDialog.dismiss();
			  }
		}
	}