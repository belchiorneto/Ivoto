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


public class AdaptadorPropostas2 extends BaseAdapter {
    
    private Activity activity;
    public static ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    static HashMap<String, String> song = new HashMap<String, String>();
    String statusindicado;
    public static List<Boolean> listb; 
    public AdaptadorPropostas2(Activity a, ArrayList<HashMap<String, String>> d) {
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
    HolderPropostas2 holder = new HolderPropostas2();
    @SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
    	
    	
    	 if(convertView==null){
    	     convertView = inflater.inflate(R.layout.list_row_propostas, null);
    	     convertView.setTag(holder);
    	 }else{
    		 holder = (HolderPropostas2) convertView.getTag();
    	 }
    	 
        holder.titulo = (TextView)convertView.findViewById(R.id.titulo); // title
        holder.conteudo = (TextView)convertView.findViewById(R.id.conteudo); // title
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
       // song.get(ListaCandidatosBuscados.KEY_NOME);
        final String titulo = song.get(ListaPropostas.KEY_TITULO);
       
        holder.titulo.setText(titulo);
        final String conteudo = song.get(ListaPropostas.KEY_CONTEUDO);
        holder.conteudo.setText(conteudo);
        holder.r = (RatingBar) convertView.findViewById(R.id.ratingBar1);
        float rating = Float.parseFloat(song.get(ListaPropostas.KEY_RATE));
        holder.r.setRating(rating);
        if(listb.get(position)==true){
        holder.conteudo.setVisibility(View.VISIBLE);
        holder.r.setVisibility(View.VISIBLE);
       	final ImageView splash = (ImageView) convertView.findViewById(R.id.imagemseta);
       	splash.setImageResource(R.drawable.arrowdown);
        }else{
        holder.conteudo.setVisibility(View.GONE);
        holder.r.setVisibility(View.GONE);
        }
        

        
        return convertView;
    }
    
}
    class HolderPropostas2 {
    	boolean visivel = false;
    	TextView titulo;
    	TextView conteudo;
    	RatingBar r;
    	public boolean getvisivel(){
    		return this.visivel;
    	}
    	public void setvisivel(boolean vis){
    		this.visivel = vis;
    	}
    	
    }