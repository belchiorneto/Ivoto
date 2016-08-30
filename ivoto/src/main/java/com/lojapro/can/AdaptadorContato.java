package com.lojapro.can;

import java.util.ArrayList;
import java.util.HashMap;

import android.R.menu;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import com.lojapro.can.R;


public class AdaptadorContato extends BaseAdapter {
   
	private String KEY_INICIO= "candidato";
	private String KEY_ID = "id";
	private String KEY_ID_USUARIO = "idusuario";
	private String KEY_NOME_URNA = "nome_urna";
	private String KEY_CARGO = "cargo";
	private String KEY_STATUS_VOTO = "status_voto";
	private String KEY_VOTOS = "votos";
	private String KEY_URL = "url";

	
    private Activity activity;
    private static ArrayList<HashMap<String, String>> data;
    public static ArrayList<HashMap<String, String>> filtrado;
    private filtraresultados filter;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    private static HashMap<String, String> candidato = new HashMap<String, String>();
    private ProgressDialog progressDialog;
    private Context ctx;
    private PopupWindow pw;
    private View popupView;
    String statusindicado;
    private ImageView btvotar;
    private EditText mensagem;
    private String android_id;
   
    
    public AdaptadorContato(Activity a, ArrayList<HashMap<String, String>> d) {
    	activity = a;
        ctx = a.getBaseContext();
        data=d;
        filtrado = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        android_id = Contato.idandroid;
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
        convertView = inflater.inflate(R.layout.list_row_contato, null);
        
      
        pw = new PopupWindow();
        popupView = inflater.inflate(R.layout.popup_layout_mensagem, null); 
        
        
        convertView.setTag(holder);
      
        }else{
      
        holder = (ViewHolder) convertView.getTag();
        }
        
        holder.idcandidato = (TextView)convertView.findViewById(R.id.idcandidato); // title
        holder.nome = (TextView)convertView.findViewById(R.id.nome); // title
        holder.cargo = (TextView)convertView.findViewById(R.id.cargo); // name
        
        holder.thumb_image = (ImageView)convertView.findViewById(R.id.list_image); // title
        holder.btvotar = (ImageView)convertView.findViewById(R.id.botaovotar); // title
        btvotar = (ImageView)convertView.findViewById(R.id.botaovotar);
        
        
        
        candidato = filtrado.get(position);
       
        final String nomedocandidato = candidato.get(KEY_NOME_URNA);
        // Setting all values in listview
        final String idcandidato = candidato.get(KEY_ID);
        holder.idcandidato.setText(idcandidato);
        holder.idcandidato.setTag(idcandidato);
        holder.nome.setText(nomedocandidato);
        holder.nome.setTag(nomedocandidato);
        holder.cargo.setText(candidato.get(KEY_CARGO));
        
        statusindicado = candidato.get(KEY_STATUS_VOTO);
      
      
        //holder.btvotar.setImageResource(R.drawable.msg);	
        holder.btvotar.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
            	HashMap<String, String> c = new HashMap<String, String>();
            	c = filtrado.get(position);
            	Intent i=new Intent(activity,ActContato.class);
            	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            	String idc = c.get(KEY_ID).toString();
            	i.putExtra("idcandidato", idc);
            	String nomec = c.get(KEY_NOME_URNA);
            	i.putExtra("nomecandidato", nomec);
            	
            	ctx.startActivity(i);
            	
            } 
        });
        
      
        holder.thumb_image.setTag(candidato.get(KEY_URL));
       
        //imageLoader.DisplayImage(candidato.get(KEY_URL), holder.thumb_image);
        return convertView;
    }
    
   
    public Filter getFilter() {
        
    	if (filter == null){
          filter  = new filtraresultados();
         }
         return filter;
        }
    private class filtraresultados extends Filter
    {
   
     @Override
     protected FilterResults performFiltering(CharSequence constraint) {
   
      constraint = constraint.toString().toLowerCase();
      FilterResults result = new FilterResults();
      if(constraint != null && constraint.toString().length() > 0)
      {
    
    	  ArrayList<HashMap<String, String>> filteredItems = new ArrayList<HashMap<String, String>>();
   
      for(int i = 0, l = data.size(); i < l; i++)
      {
    	  HashMap<String, String> dados = data.get(i);
          
    	  String nome = dados.get(Principal.KEY_TITLE);
    	  if(nome.toString().toLowerCase().contains(constraint) || constraint == "*"){
    		  filteredItems.add(dados);
    	  }
      }
      result.count = filteredItems.size();
      result.values = filteredItems;
      }
      else
      {
    	
       synchronized(this)
       {
        
        result.values = data;
        result.count = data.size();
        
       }
      }
      
      return result;
     }
   
     
     @SuppressWarnings("unchecked")
	protected void publishResults(CharSequence constraint, FilterResults results) {
    	if(results.count== 0){
    	filtrado = (ArrayList<HashMap<String, String>>) results.values;
    	notifyDataSetInvalidated();
    	}
    	else{
    	filtrado = (ArrayList<HashMap<String, String>>) results.values;
    		notifyDataSetChanged();
    	}

    	}
     	
    }
    static class ViewHolder {
    	TextView idcandidato;
		TextView nome;
        TextView cargo;
        TextView votos;
        ImageView thumb_image;
        ImageView btvotar;
    }
}
