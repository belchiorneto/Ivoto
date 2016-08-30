package com.lojapro.can;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class ContatoCandidato extends AsyncTask<String, String, String> {
	Context ctx;
	String idcan;
	String android;
	String mensagem;
	public ContatoCandidato(Context context, String idcandidato, String idandroid, String msg) {
		this.ctx = context;
		this.idcan = idcandidato;
		this.android = idandroid;
		this.mensagem = msg;
	}
	GetUserData dadosusuario = new GetUserData(ctx);
	private List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	
	
	@Override
	protected String doInBackground(String... urls) {
		// TODO Auto-generated method stub
		String retorno = null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("id_android", android);
		
		map.put("id_candidato", idcan);
		map.put("mensagem", mensagem);
		
		
		try {

			HttpClient client = new DefaultHttpClient();
			//HttpClient client = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(urls[0]);
			for (Map.Entry<String, String> entry : map.entrySet())
			{
			    pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				
			}
			
			request.setEntity(new UrlEncodedFormEntity(pairs));
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();  
			final String content;
			content = EntityUtils.toString(entity);
			return content;
         
        } catch (Exception e) {
            System.out.println("Cadastro exeption = " + e);
            return null;
        }
		
		
	}
	protected void onPostExecute(String result) {
		System.out.println("On post execute");
	      Toast toast=Toast.makeText(ctx,result,3);
	             toast.show();

	}
	

}

