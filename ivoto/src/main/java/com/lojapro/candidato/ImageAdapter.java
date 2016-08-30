package com.lojapro.candidato;

import com.lojapro.can.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class ImageAdapter extends BaseAdapter{
    private Context mContext;
    private final String[] images;
    
      public ImageAdapter(Context c,String[] images) {
          mContext = c;
          this.images = images;
      }

      @Override
      public int getCount() {
          // TODO Auto-generated method stub
          return images.length;
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

      @SuppressLint("InflateParams")
	@Override
      public View getView(int position, View convertView, ViewGroup parent) {
          // TODO Auto-generated method stub
          View grid;
          LayoutInflater inflater = (LayoutInflater) mContext
              .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

          if (convertView == null) {

              grid = new View(mContext);
              grid = inflater.inflate(R.layout.imagensnogrid, null);
              TextView textView = (TextView) grid.findViewById(R.id.grid_image);
              textView.setText(images[position]);
              
              
              grid.setTag(images[position]);
            
          } else {
              grid = (View) convertView;
          }

          return grid;
      }
}