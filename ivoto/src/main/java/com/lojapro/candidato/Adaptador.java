package com.lojapro.candidato;

import java.util.ArrayList;
import java.util.HashMap;

import com.lojapro.can.ImageLoader;
import com.lojapro.can.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;


public class Adaptador extends BaseAdapter {
    
    private Activity activity;
    public static ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    static HashMap<String, String> song = new HashMap<String, String>();
    String statusindicado;
    
    public Adaptador(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        a.getBaseContext();
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
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
    ViewHolder holder = new ViewHolder();
    @SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
    	
        if(convertView==null){
        convertView = inflater.inflate(R.layout.list_row_busca, null);
        new PopupWindow();
        inflater.inflate(R.layout.popup_layout, null); 
        convertView.setTag(holder);
        }else{
        holder = (ViewHolder) convertView.getTag();
        }
        

        holder.nome_urna = (TextView)convertView.findViewById(R.id.nome_urna); // title
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        song.get(ListaCandidatosBuscados.KEY_NOME);
        final String nomeurna = song.get(ListaCandidatosBuscados.KEY_NOMEURNA);
        holder.nome_urna.setText(nomeurna);
        return convertView;
    }
    
}
    class ViewHolder {
    	TextView nome_urna;
    }