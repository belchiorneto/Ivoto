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


public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private static ArrayList<HashMap<String, String>> data;
    public static ArrayList<HashMap<String, String>> filtrado;
    private filtraresultados filter;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    private static HashMap<String, String> song = new HashMap<String, String>();
    private ProgressDialog progressDialog;
    private Context ctx;
    private PopupWindow pw;
    private View popupView;
    String statusindicado;
    private ImageView btindi;
    private String android_id;
    
    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        
        ctx = a.getBaseContext();
        data=d;
        filtrado = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        android_id = Secure.getString(activity.getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
    }

    public int getCount() {
        return filtrado.size();
    }

    public Object getItem(int position) {
        return position;
    }

    @Override
   /*
    public long getItemId(int position){
    	song = filtrado.get(position);
    	String nome = song.get(Principal.KEY_ARTIST);
        return nome.hashCode();
    }
    */
    public long getItemId(int position) {
        return position;
    }
    ViewHolder holder = new ViewHolder();
    public View getView(final int position, View convertView, ViewGroup parent) {
    	
        if(convertView==null){
        convertView = inflater.inflate(R.layout.list_row, null);
        
      
        pw = new PopupWindow();
        popupView = inflater.inflate(R.layout.popup_layout, null); 
        
        
        convertView.setTag(holder);
      
        }else{
      
        holder = (ViewHolder) convertView.getTag();
        }
        

        final ViewGroup p = parent;
        holder.idcandidato = (TextView)convertView.findViewById(R.id.idcandidato); // title
        holder.title = (TextView)convertView.findViewById(R.id.title); // title
        holder.artist = (TextView)convertView.findViewById(R.id.artist); // name
        holder.duration = (TextView)convertView.findViewById(R.id.duration); // title
        holder.indicacoes = (TextView)convertView.findViewById(R.id.indicacao); // title
        holder.thumb_image = (ImageView)convertView.findViewById(R.id.list_image); // title
        holder.btind = (ImageView)convertView.findViewById(R.id.botaoindicacao); // title
        btindi = (ImageView)convertView.findViewById(R.id.botaoindicacao);
        
        
        
        song = filtrado.get(position);
       
        final String nomedocandidato = song.get(Principal.KEY_TITLE);
        // Setting all values in listview
        final String idcandidato = song.get(Principal.KEY_ID);
        holder.idcandidato.setText(idcandidato);
        holder.title.setText(song.get(Principal.KEY_TITLE));
        holder.artist.setText(song.get(Principal.KEY_ARTIST));
        holder.duration.setText(song.get(Principal.KEY_DURATION));
        holder.indicacoes.setText(song.get(Principal.KEY_INDICACAO));
        statusindicado = song.get(Principal.KEY_STATUSINDICADO);

  
        
        if(statusindicado.equals("no")){
      
        holder.btind.setImageResource(R.drawable.indicar);	
        holder.btind.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
            	holder.btind.setImageResource(R.drawable.indicado);
            	pw.setWidth((int)(p.getWidth() * 0.8f));
                if(p.getHeight() > 200){
            	pw.setHeight((int)(p.getHeight() * 0.3f));
                }else{
                pw.setHeight(100);
                }
                pw.setOutsideTouchable(false);
                pw.setContentView(popupView);
                
                pw.showAtLocation(p, Gravity.CENTER, 0, 0);
                TextView textoindica = (TextView)popupView.findViewById(R.id.textoindica);
                textoindica.setText("Indicar " + nomedocandidato + " como pré-candidato?");
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
		            	
		            	//new CadastraUsuario(ctx).execute(Principal.URL_SEND);
		            	new IndicaCandidato(ctx, idcandidato, android_id).execute(Principal.URL_SEND);
		            	new android.os.Handler().postDelayed(
		            		    new Runnable() {
		            		        public void run() {
		            		        	int pos = (Integer) indica.getTag();
		            		        	pw.dismiss();
		            		        	int indic = Integer.valueOf(song.get(Principal.KEY_INDICACAO).replace("Indicações: ",""));
		            		        	HashMap<String,String> m = filtrado.get(pos);
		            		        	m.put(Principal.KEY_STATUSINDICADO, "sim");
		            		        	m.put(Principal.KEY_INDICACAO, "Indicações: " + (indic + 1));
		            		        	Indicar.updateadapter();
		            		        }
		            		    }, 
		            		1500);
		            }
				});
		        
            } 
        });
        
        }else{
      
        	holder.btind.setImageResource(R.drawable.indicado);
        }
        holder.thumb_image.setTag(song.get(Principal.KEY_THUMB_URL));
        imageLoader.DisplayImage(song.get(Principal.KEY_THUMB_URL), holder.thumb_image);
        return convertView;
    }
    
    public static String getname(int pos){
    	song = data.get(pos);
    	return song.get(Principal.KEY_ARTIST);
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
		TextView title;
        TextView artist;
        TextView duration;
        TextView indicacoes;
        ImageView thumb_image;
        ImageView btind;
    }
}
