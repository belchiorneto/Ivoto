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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AdaptadorAgenda extends BaseAdapter {
    
    private Activity activity;
    public static ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    static HashMap<String, String> song = new HashMap<String, String>();
    String statusindicado;
    public static List<Boolean> listb; 
    public AdaptadorAgenda(Activity a, ArrayList<HashMap<String, String>> d) {
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
    HolderAgenda holder = new HolderAgenda();
    @SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
    	
    	
    	 if(convertView==null){
    	     convertView = inflater.inflate(R.layout.list_row_agenda, null);
    	     convertView.setTag(holder);
    	 }else{
    		 holder = (HolderAgenda) convertView.getTag();
    	 }
    	 
        
    	holder.data = (TextView)convertView.findViewById(R.id.data);
    	holder.hora = (TextView)convertView.findViewById(R.id.hora);
        holder.conteudo = (TextView)convertView.findViewById(R.id.conteudo); // title
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
       // song.get(ListaCandidatosBuscados.KEY_NOME);
        final String data = song.get(ListaAgenda.KEY_DATA);
        holder.data.setText(data);
        final String hora = song.get(ListaAgenda.KEY_HORA);
        holder.hora.setText(hora);
        final String conteudo = song.get(ListaAgenda.KEY_CONTEUDO);
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
    class HolderAgenda {
    	boolean visivel = false;
    	TextView data;
    	TextView hora;
    	TextView conteudo;
    	public boolean getvisivel(){
    		return this.visivel;
    	}
    	public void setvisivel(boolean vis){
    		this.visivel = vis;
    	}
    	
    }