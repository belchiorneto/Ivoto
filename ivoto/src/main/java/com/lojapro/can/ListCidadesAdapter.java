package com.lojapro.can;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lojapro.can.R;
import android.widget.Filter;

@SuppressLint("DefaultLocale")
public class ListCidadesAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, String>> cidades;
	
	private ArrayList<HashMap<String, String>> countryList;
    private Activity activity;
    private CountryFilter filter;
    private static LayoutInflater inflater=null;
    
    
    public ListCidadesAdapter(Activity a, ArrayList<HashMap<String, String>> lcidades) {
        activity = a;
        this.cidades = lcidades;
        this.countryList = lcidades;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       
    }

	public int getCount() {
        return countryList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
       
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_cidades, null);

        TextView nomecidade = (TextView)vi.findViewById(R.id.nomecidade); // title
        HashMap <String, String> mp = new HashMap<String, String>();
        mp = countryList.get(position);
        
        nomecidade.setText(mp.get("nomecidade"));
        vi.setTag(mp.get("idcidade"));
        
        return vi;
    }
    public Filter getFilter() {
    
	if (filter == null){
      filter  = new CountryFilter();
     }
     return filter;
    }
   
    private class CountryFilter extends Filter
    {
   
     @Override
     protected FilterResults performFiltering(CharSequence constraint) {
   
      constraint = constraint.toString().toLowerCase();
      FilterResults result = new FilterResults();
      if(constraint != null && constraint.toString().length() > 0)
      {
    	  ArrayList<HashMap<String,String>> filteredItems = new ArrayList<HashMap<String,String>>();
   
      for(int i = 0, l = cidades.size(); i < l; i++)
      {
    	  HashMap <String, String> mp = new HashMap<String, String>();
          mp = cidades.get(i);
         
       String country = mp.get("nomecidade").toString().toLowerCase();
       if(country.contains(constraint)){
    	   filteredItems.add(mp);
    	  // System.out.println(country +" encontrado /"+ constraint+"/"+ filteredItems.size()); 
    	   
       }else{
    	  // System.out.println(country +"/n encontrado /"+ constraint +"/"+ filteredItems.size());
       }
      }
      result.count = filteredItems.size();
      result.values = filteredItems;
      }
      else
      {
       synchronized(this)
       {
        result.values = cidades;
        result.count = cidades.size();
       }
      }
      System.out.println("Resultados: " + result.count);
      return result;
     }
   
     
     @SuppressWarnings("unchecked")
	protected void publishResults(CharSequence constraint, FilterResults results) {
    	// TODO Auto-generated method stub
    	System.out.println(results.count);
    	 if(results.count== 0){
    		 countryList=(ArrayList<HashMap<String,String>>) results.values;
    		 notifyDataSetInvalidated();
    	}
    	else{
    		countryList=(ArrayList<HashMap<String,String>>) results.values;
    		notifyDataSetChanged();
    	}

    	}
     	
    }
   
   
   }
   