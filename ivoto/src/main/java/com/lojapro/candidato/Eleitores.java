package com.lojapro.candidato;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lojapro.can.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;

	
@SuppressLint("NewApi")
public class Eleitores extends Fragment implements OnMapReadyCallback {
	private static final LatLng CIDADE = new LatLng(-3.892882, -38.456219);
	
	MapView mapView;
    GoogleMap map;
	@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
 
            View android = inflater.inflate(R.layout.eleitores, container, false);
          //  map = ((MapFragment) getActivity().getFragmentManager()
          //          .findFragmentById(R.id.map)).getMap();
          //  map.animateCamera(CameraUpdateFactory.newLatLngZoom(CIDADE, 13));
        return android;
 
    }
	@Override
	public void onMapReady(GoogleMap arg0) {
		// TODO Auto-generated method stub
		System.out.println("Mapa pronto");
	}
	
	 


    
}	
	
