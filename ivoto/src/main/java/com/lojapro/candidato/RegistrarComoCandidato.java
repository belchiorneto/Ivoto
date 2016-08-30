package com.lojapro.candidato;


import com.lojapro.can.R;
import android.app.Activity;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;

import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

public class RegistrarComoCandidato extends Activity{
	private ImageButton sim;
	private ImageButton nao;
	public static String palavrabuscada;
	Adaptador adapt;
	ListView list;
	ViewGroup parent;
	public static final String dadostse = "tsekey";
	public static String estado = null;
	SharedPreferences sharedpreferences;
	long tStart;
	long tnow;
	private SharedPreferences sharedPref;
	private int idcan;
	boolean checa = false;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        sharedPref = this.getSharedPreferences("com.lojapro.can", Context.MODE_PRIVATE);
        setContentView(R.layout.registrarcomocandidato);
        Intent intent = new Intent(RegistrarComoCandidato.this, ComRegistros.class);
        startActivity(intent);
        finish();
        // String valueidcan = sharedPref.getString("idcan", null);
        try {
        	String idc = sharedPref.getString("idcan",null);
        	idcan = Integer.parseInt(idc);
        } catch (Exception e) {
        	try {
            	idcan = sharedPref.getInt("idcan", 0);
            } catch (Exception e2) {
                System.out.println("I caught: " + e);
            }
        }
      
        
       
     	list=(ListView)findViewById(R.id.lista_busca_can);
		sim = (ImageButton)findViewById(R.id.botaosim);
		sim.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(RegistrarComoCandidato.this, ComRegistros.class);
	            startActivity(intent);
	            finish();
			}
		});

		nao = (ImageButton)findViewById(R.id.botaonao);
		nao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(RegistrarComoCandidato.this, SemRegistros.class);
	            startActivity(intent);
	            finish();
			}
		});
		System.out.println("Id candidato: " + idcan);
        if(idcan > 0){
        	Intent i2 = new Intent(RegistrarComoCandidato.this, Candidato.class);
            startActivity(i2);
            finish();
        }
        
    }
	
}