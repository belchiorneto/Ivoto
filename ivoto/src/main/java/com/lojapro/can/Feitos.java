package com.lojapro.can;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.lojapro.candidato.AdaptadorPropostasEleitor;
import com.lojapro.candidato.AndroidMultiPartEntity;
import com.lojapro.candidato.AndroidMultiPartEntity.ProgressListener;
import com.lojapro.can.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class Feitos extends Fragment {
	SharedPreferences sharedPref;
	Activity act;
	public static String idandroid;
	public static int idcidade;
	private ListView list;
	private AdaptadorPropostasEleitor adapt;
	
	@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
			View android = inflater.inflate(R.layout.feitos, container, false);
           
            return android;
    	}
    }