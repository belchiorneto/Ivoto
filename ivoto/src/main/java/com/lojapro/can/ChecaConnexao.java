package com.lojapro.can;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


public class ChecaConnexao {

	private Context ctx;
    
	public ChecaConnexao(Context context) {
		this.ctx = context;
	}
	
	   public boolean ativo(){
		   boolean ret = false;
		   ConnectivityManager c = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		   if(c != null){
			   @SuppressWarnings("deprecation")
			NetworkInfo[] info = c.getAllNetworkInfo();
			   if(info != null){
				   for(int i = 0; i<info.length; i++){
					   if(info[i].getState() == NetworkInfo.State.CONNECTED){
						   ret = true;
					   }
				   }
			   }
		   }
		   return ret;
	   }
}
