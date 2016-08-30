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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import com.lojapro.can.R;


public class AdaptadorVotar extends BaseAdapter {
   
	private String KEY_INICIO= "candidato";
	private String KEY_ID = "id";
	private String KEY_ID_USUARIO = "idusuario";
	private String KEY_NOME_URNA = "nome_urna";
	private String KEY_CARGO = "cargo";
	private String KEY_STATUS_VOTO = "status_voto";
	private String KEY_VOTOS = "votos";
	private String KEY_URL = "url";
	private String URL_VOTO = "http://ivoto.com.br/data_eleicoes/votar.php";
	
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
    private String android_id;
    
    public AdaptadorVotar(Activity a, ArrayList<HashMap<String, String>> d) {
    	activity = a;
        ctx = a.getBaseContext();
        data=d;
        filtrado = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        android_id = Votar.idandroid;
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
        convertView = inflater.inflate(R.layout.list_row_votar, null);
        
      
        pw = new PopupWindow();
        popupView = inflater.inflate(R.layout.popup_layout_votar, null); 
        
        
        convertView.setTag(holder);
      
        }else{
      
        holder = (ViewHolder) convertView.getTag();
        }
        

        final ViewGroup p = parent;
        holder.idcandidato = (TextView)convertView.findViewById(R.id.idcandidato); // title
        holder.nome = (TextView)convertView.findViewById(R.id.nome); // title
        holder.cargo = (TextView)convertView.findViewById(R.id.cargo); // name
        holder.votos = (TextView)convertView.findViewById(R.id.votos); // title
        holder.thumb_image = (ImageView)convertView.findViewById(R.id.list_image); // title
        holder.btvotar = (ImageView)convertView.findViewById(R.id.botaovotar); // title
        btvotar = (ImageView)convertView.findViewById(R.id.botaovotar);
        
        
        
        candidato = filtrado.get(position);
       
        final String nomedocandidato = candidato.get(KEY_NOME_URNA);
        // Setting all values in listview
        final String idcandidato = candidato.get(KEY_ID);
        holder.idcandidato.setText(idcandidato);
        holder.nome.setText(nomedocandidato);
        holder.cargo.setText(candidato.get(KEY_CARGO));
        holder.votos.setText(candidato.get(KEY_VOTOS));
        statusindicado = candidato.get(KEY_STATUS_VOTO);
      
        if(statusindicado.equals("no")){
      
        holder.btvotar.setImageResource(R.drawable.confirma);	
        holder.btvotar.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
            	
            	
            	pw.setWidth((int)(p.getWidth() * 0.95f));
                if(p.getHeight() < 200){
            	pw.setHeight(200);
                }else{
                pw.setHeight((int)(p.getHeight() * 0.6f));	
                }
                pw.setOutsideTouchable(false);
                pw.setContentView(popupView);
                
                pw.showAtLocation(p, Gravity.CENTER, 0, 0);
                TextView textoindica = (TextView)popupView.findViewById(R.id.textoindica);
                textoindica.setText("Confirma voto em " + nomedocandidato + " ?");
                ImageView cancela = (ImageView)popupView.findViewById(R.id.botaocancelar);
				cancela.setMinimumWidth(Math.round(pw.getWidth() * 0.45f));
				cancela.setMinimumHeight(Math.round(pw.getHeight() * 0.3f));
				
                cancela.setOnClickListener(new OnClickListener() {
		            public void onClick(View v)
		            {
		            	pw.dismiss();
		            }
				});
		        final ImageView indica = (ImageView)popupView.findViewById(R.id.botaoindicar); // artist name
		       
		        indica.setImageResource(R.drawable.sim);
		        indica.setMinimumWidth(Math.round(pw.getWidth() * 0.40f));
				indica.setMinimumHeight(Math.round(pw.getHeight() * 0.3f));
				indica.setTag(position);
		        indica.setOnClickListener(new OnClickListener() {
		            public void onClick(View v)
		            {
		            	
		            	indica.setImageResource(R.drawable.load);
		            	new VotaCandidato(ctx, idcandidato, android_id).execute(URL_VOTO);
		            	new android.os.Handler().postDelayed(
		            		    new Runnable() {
		            		        public void run() {
		            		        	int pos = (Integer) indica.getTag();
		            		        	pw.dismiss();
		            		        	int indic = Integer.valueOf(candidato.get(KEY_VOTOS).replace("Votos: ",""));
		            		        	HashMap<String,String> m = filtrado.get(pos);
		            		        	m.put(KEY_STATUS_VOTO, "sim");
		            		        	m.put(KEY_VOTOS, "Votos: " + (indic + 1));
		            		        	Votar.updateadapter();
		            		        }
		            		    }, 
		            		1500);
		            }
				});
		        
            } 
        });
        
        }else{
      
        	holder.btvotar.setImageResource(R.drawable.confirmado);
        }
        holder.thumb_image.setTag(candidato.get(KEY_URL));
        imageLoader.DisplayImage(candidato.get(KEY_URL), holder.thumb_image);
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
