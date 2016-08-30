package com.lojapro.candidato;

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

import com.lojapro.can.ImageLoader;
import com.lojapro.can.R;


public class AdaptadorListaCandidatos extends BaseAdapter {
    
    private Activity activity;
    public static ArrayList<HashMap<String, String>> data;
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
 // All static variables
 	static final String URL = "http://ivoto.com.br/data_eleicoes/ws.php";
 	static final String URL_SEND = "http://ivoto.com.br/data_eleicoes/send.php";
 	// XML node keys
 	static final String KEY_SONG = "song"; // parent node
 	static final String KEY_ID = "id";
 	static final String KEY_IDUSUARIO = "idusuario";
 	static final String KEY_TITLE = "title";
 	static final String KEY_ARTIST = "artist";
 	static final String KEY_DURATION = "duration";
 	static final String KEY_INDICACAO = "ind";
 	static final String KEY_STATUSINDICADO = "status_indicado";
 	static final String KEY_THUMB_URL = "thumb_url";
    
    public AdaptadorListaCandidatos(Activity a, ArrayList<HashMap<String, String>> d) {
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
        convertView = inflater.inflate(R.layout.list_row_candidato, null);
        
        convertView.setTag(holder);
      
        }else{
      
        holder = (ViewHolder) convertView.getTag();
        }
        

        final ViewGroup p = parent;
        holder.idcandidato = (TextView)convertView.findViewById(R.id.idcandidato); // title
        holder.title = (TextView)convertView.findViewById(R.id.title); // title
        holder.artist = (TextView)convertView.findViewById(R.id.artist); // name
        holder.duration = (TextView)convertView.findViewById(R.id.duration); // title
     
        holder.thumb_image = (ImageView)convertView.findViewById(R.id.list_image); // title
      
        
        song = filtrado.get(position);
       
        final String nomedocandidato = song.get(KEY_TITLE);
        final String idcandidato = song.get(KEY_ID);
        holder.idcandidato.setText(idcandidato);
        holder.title.setText(song.get(KEY_TITLE));
        holder.artist.setText(song.get(KEY_ARTIST));
        holder.duration.setText(song.get(KEY_DURATION));
        holder.thumb_image.setTag(song.get(KEY_THUMB_URL));
        imageLoader.DisplayImage(song.get(KEY_THUMB_URL), holder.thumb_image);
        return convertView;
    }
    
    public static String getname(int pos){
    	song = data.get(pos);
    	return song.get(KEY_ARTIST);
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
          
    	  String nome = dados.get(KEY_TITLE);
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
