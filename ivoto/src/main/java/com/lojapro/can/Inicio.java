package com.lojapro.can;

import com.lojapro.candidato.Candidato;
import com.lojapro.candidato.RegistrarComoCandidato;
import com.lojapro.can.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class Inicio extends Activity {
	SharedPreferences sharedPref;
	private static final int MENU3 = 1;
    private static final int MENU4 = 2;
    private static final int SUBMENU1 = 3;
    private static final int SUBMENU2 = 4;
    private static final int SUBMENU3 = 5;
    private static final int GROUP1 = 6;
    private static final int MENU5 = 7;
    private static final int MENU6 = 8;
public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inicio);
		Context context = this;
		sharedPref = this.getSharedPreferences(
      	      "com.lojapro.can", Context.MODE_PRIVATE);
		
		int perfil = sharedPref.getInt("perfil", 0);
		
		if(perfil == 0){
		
		}else if (perfil == 1){
			Intent intent = new Intent(Inicio.this, Eleitor.class);
        	startActivity(intent);
        	finish();
		}else if (perfil == 2){
			
			Intent intent = new Intent(Inicio.this, RegistrarComoCandidato.class);
			finish();
			startActivity(intent);
		}
		Button b1 = (Button)findViewById(R.id.eleitor);
		Button b2 = (Button)findViewById(R.id.candidato);
		b1.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
            	sharedPref.edit().putInt("perfil", 1).apply();
            	Intent intent = new Intent(Inicio.this, Eleitor.class);
            	finish();
            	startActivity(intent);
            }
		});
		b2.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
            	sharedPref.edit().putInt("perfil", 2).apply();
            	Intent intent = new Intent(Inicio.this, RegistrarComoCandidato.class);
            	finish();
            	startActivity(intent);
            }
		});
	}
	

}
