package com.lojapro.candidato;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.lojapro.can.ImageLoader;
import com.lojapro.can.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;


public class AdaptadorMensagens extends BaseAdapter {
    
    private Activity activity;
    public static ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    static HashMap<String, String> song = new HashMap<String, String>();
    String statusindicado;
    public static List<Boolean> listb; 
    public AdaptadorMensagens(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        a.getBaseContext();
        data=d;
        listb = new ArrayList<Boolean>(Arrays.asList(new Boolean[data.size()]));
        Collections.fill(listb, Boolean.FALSE);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
    }

    public int getCount() {
    	return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

  
    public long getItemId(int position) {
        return position;
    }
    HolderMensagens holder = new HolderMensagens();
    @SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
    	
    	
    	 if(convertView==null){
    	     convertView = inflater.inflate(R.layout.list_row_mensagens, null);
    	     convertView.setTag(holder);
    	 }else{
    		 holder = (HolderMensagens) convertView.getTag();
    	 }
    	 
        holder.titulo = (TextView)convertView.findViewById(R.id.titulo); // title
        holder.conteudo = (TextView)convertView.findViewById(R.id.conteudo); // title
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
       // song.get(ListaCandidatosBuscados.KEY_NOME);
        final String titulo = song.get(ListaMensagens.KEY_TITULO);
       
        holder.titulo.setText(titulo);
        final String conteudo = song.get(ListaMensagens.KEY_CONTEUDO);
        holder.conteudo.setText(conteudo);
        
      
      
        if(listb.get(position)==true){
        holder.conteudo.setVisibility(View.VISIBLE);
       
       	final ImageView splash = (ImageView) convertView.findViewById(R.id.imagemseta);
       	splash.setImageResource(R.drawable.arrowdown);
        }else{
        holder.conteudo.setVisibility(View.GONE);
      
        }
        

        
        return convertView;
    }
    
}
    class HolderMensagens {
    	boolean visivel = false;
    	TextView titulo;
    	TextView conteudo;
    	
    	public boolean getvisivel(){
    		return this.visivel;
    	}
    	public void setvisivel(boolean vis){
    		this.visivel = vis;
    	}
    	
    }