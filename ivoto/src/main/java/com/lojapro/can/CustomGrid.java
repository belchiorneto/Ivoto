package com.lojapro.can;


	import android.content.Context;
	import android.view.LayoutInflater;
	import android.view.View;
	import android.view.ViewGroup;
	import android.widget.BaseAdapter;
	import android.widget.ImageView;
	import android.widget.TextView;
import com.lojapro.can.R;
	 
	public class CustomGrid extends BaseAdapter{
	      private Context mContext;
	      private final String[] web;
	      private final int[] ids;
	      
	 
	        public CustomGrid(Context c,String[] web, int[] ids) {
	            mContext = c;
	            this.ids = ids;
	            this.web = web;
	        }
	 
	        @Override
	        public int getCount() {
	            // TODO Auto-generated method stub
	            return web.length;
	        }
	 
	        @Override
	        public Object getItem(int position) {
	            // TODO Auto-generated method stub
	            return null;
	        }
	 
	        @Override
	        public long getItemId(int position) {
	            // TODO Auto-generated method stub
	            return 0;
	        }
	 
	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            // TODO Auto-generated method stub
	            View grid;
	            LayoutInflater inflater = (LayoutInflater) mContext
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
	            if (convertView == null) {
	 
	                grid = new View(mContext);
	                grid = inflater.inflate(R.layout.estadonome, null);
	                TextView textView = (TextView) grid.findViewById(R.id.grid_text);
	                textView.setText(web[position]);
	                
	                
	                grid.setTag(ids[position]);
	              
	            } else {
	                grid = (View) convertView;
	            }
	 
	            return grid;
	        }
	}
