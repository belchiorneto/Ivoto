package com.lojapro.candidato;

import com.lojapro.can.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class TrocarImagem extends Activity {
    private ImageView imagemcandidato;
    private ImageView botaoatualizar;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
        	 setContentView(R.layout.trocarimagem);
        	 imagemcandidato = (ImageView)findViewById(R.id.imagemcandidato);
        	 botaoatualizar = (ImageView)findViewById(R.id.atualizarimagem);
    	}
    }
