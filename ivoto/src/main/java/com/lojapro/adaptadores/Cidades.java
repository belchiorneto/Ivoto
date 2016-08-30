package com.lojapro.adaptadores;


import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.lojapro.can.R;


public class Cidades extends BaseAdapter {
   
	private String KEY_ID = "id";
	private String KEY_NOME = "nome";
	private String KEY_ESTADO = "estado";
	public static ArrayList<HashMap<String, String>> filtrado;
    private static LayoutInflater inflater=null;
    private static HashMap<String, String> cidade = new HashMap<String, String>();
    private Context ctx;
    String statusindicado;
    public Cidades(Activity a, ArrayList<HashMap<String, String>> d) {
        ctx = a.getBaseContext();
        filtrado = d;
        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return filtrado.size();
    }

    public Object getItem(int position) {
        return position;
    }

 
    public long getItemId(int position) {
        return position;
    }
    ViewHolder holder = new ViewHolder();
    public View getView(final int position, View convertView, ViewGroup parent) {
    	
        if(convertView==null){
        	convertView = inflater.inflate(R.layout.list_row_cidades, null);
        	convertView.setTag(holder);
        }else{
        	holder = (ViewHolder) convertView.getTag();
        }
        
        holder.idcidade = (TextView)convertView.findViewById(R.id.idcidade); // title
        holder.nome = (TextView)convertView.findViewById(R.id.nome); // title
        
        
        
        cidade = filtrado.get(position);
       
        final String nomecidade = cidade.get(KEY_NOME);
        final String estado = cidade.get(KEY_ESTADO);
        // Setting all values in listview
        final String idcidade = cidade.get(KEY_ID);
        holder.idcidade.setText(idcidade);
        holder.idcidade.setTag(idcidade);
        holder.nome.setText(nomecidade +"/"+estado);
        holder.nome.setTag(idcidade);
        return convertView;
    }
    
   
    public static class ViewHolder {
    	TextView idcidade;
		TextView nome;
		
    }
}
