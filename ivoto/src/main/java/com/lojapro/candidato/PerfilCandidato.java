package com.lojapro.candidato;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.lojapro.can.Eleitor;
import com.lojapro.can.ImageLoader;
import com.lojapro.can.Inicio;
import com.lojapro.can.Principal;
import com.lojapro.candidato.AndroidMultiPartEntity.ProgressListener;
import com.lojapro.can.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Bitmap.Config;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
@SuppressWarnings("deprecation")
public class PerfilCandidato extends Fragment {
	private ImageLoader imageLoader;
	private static final String MY_PREFS_NAME = "configs";
	private SharedPreferences sharedPref;
	private TextView nome;
	private TextView nomeurna;
	private TextView local;
	private TextView msgnegado;
	private TextView tmsgboasvindas;
	private EditText msgboasvindas;
	private ImageButton botatualizar;
	private ImageButton botaoeditar;
	private ImageButton botaoapagar;
	private ImageView imagemcandidato;
	private CarregaPerfilCandidato perfil;
	private ArrayList<String> dados; 
	private String idandroid;
	private InputMethodManager imm;
	public static int ver = 0;
	private Activity act;
	private String nomedoarquivo;
	private String filePathenvio;
	private static int RESULT_LOAD_IMAGE = 1;
	private Bitmap imagemcan;
	private long totalSize = 0;
	private Context ctx;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
 
            View android = inflater.inflate(R.layout.admin_perfil, container, false);
           
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy);
	        act = getActivity();
	        ctx = act.getApplicationContext();
	        imm = (InputMethodManager) getActivity ()
	                .getSystemService(Context.INPUT_METHOD_SERVICE);
	        idandroid = Secure.getString(getActivity ().getContentResolver(), Secure.ANDROID_ID);
 		    nomedoarquivo = idandroid;
	        perfil = new CarregaPerfilCandidato(getActivity ());
	        imageLoader = new ImageLoader(getActivity ());
	        
	        sharedPref = getActivity ().getSharedPreferences("com.lojapro.can", Context.MODE_PRIVATE);
	        
	        nome = (TextView)android.findViewById(R.id.nome);
	        nomeurna = (TextView)android.findViewById(R.id.nomeurna);
	        local = (TextView)android.findViewById(R.id.local);
	        tmsgboasvindas = (TextView)android.findViewById(R.id.tmsgboasvindas);
	        msgboasvindas = (EditText)android.findViewById(R.id.msgboasvindas);
	        msgboasvindas.clearFocus();
	        msgnegado = (TextView)android.findViewById(R.id.msgnegado);
	       
	        
	        
	        botatualizar = (ImageButton)android.findViewById(R.id.fab_image_button_save);
	        botaoeditar = (ImageButton)android.findViewById(R.id.fab_image_button_edit);
	        botaoapagar = (ImageButton)android.findViewById(R.id.fab_image_button_cancel);
	        Button botaosair = (Button)android.findViewById(R.id.botaosair);
	        imagemcandidato = (ImageView)android.findViewById(R.id.imagemcandidato);
	        registerForContextMenu(imagemcandidato);
	        imagemcandidato.setOnClickListener(new OnClickListener() {
	        
	        	public void onClick(View v)
	            {
	            	trocarimagem();
	            	
	            }
		    });
	        botaoeditar.setVisibility(View.GONE);
            
	        botaosair.setOnClickListener(new OnClickListener() {
	            public void onClick(View v)
	            {
	            	sair();
	            	
	            }
		    });
	        botaoeditar.setOnClickListener(new OnClickListener() {
	            public void onClick(View v)
	            {
	            	editamensagem();
	            	imagemcandidato.setVisibility(View.GONE);
	            	nome.setVisibility(View.GONE);
	            	nomeurna.setVisibility(View.GONE);
	            	local.setVisibility(View.GONE);
	            	
	            }
		    });
	        botatualizar.setOnClickListener(new OnClickListener() {
	            public void onClick(View v)
	            {
	            	
	            	imagemcandidato.setVisibility(View.VISIBLE);
	            	nome.setVisibility(View.VISIBLE);
	            	nomeurna.setVisibility(View.VISIBLE);
	            	local.setVisibility(View.VISIBLE);
	            	atualizamensagem();
	            }
		    });
	        botaoapagar.setOnClickListener(new OnClickListener() {
	            public void onClick(View v)
	            {
	            	
	            	imagemcandidato.setVisibility(View.VISIBLE);
	            	nome.setVisibility(View.VISIBLE);
	            	nomeurna.setVisibility(View.VISIBLE);
	            	local.setVisibility(View.VISIBLE);
	            	apagamensagem();
	            }
		    });
			
			
				Button voltar = (Button)android.findViewById(R.id.voltar);
				voltar.setOnClickListener(new OnClickListener() {
		            public void onClick(View v)
		            {
		            	sharedPref.edit().putInt("perfil", 1).apply();
	                	Intent intent = new Intent(ctx, Eleitor.class);
	                	startActivity(intent);
		            	getActivity ().finish();
		            }
			    });
				
				
				if(permitido()){
					 LinearLayout neg = (LinearLayout) android.findViewById(R.id.laynegado);
					 neg.setVisibility(View.GONE);
					
					 dados = perfil.retornalista(idandroid);
					 //índices: 0 = nome, 1 = nome_urna, 2 = ocupacao, 3 = imagem, 4 = estado, 5 = nome_cidade, 6 = mensagem,
					 //7 = urlimagem, 8 = verify , 9 = id
					 sharedPref.edit().putString("idcan", dados.get(9).toString()).apply();
					 ver = Integer.parseInt(dados.get(8).toString());
					 if(ver == 0){
						 LinearLayout adm = (LinearLayout) android.findViewById(R.id.laypermitido);
						 adm.setVisibility(View.GONE);
						 neg.setVisibility(View.VISIBLE);
						 msgnegado.setText("Sua solicitação para administrar o perfil de " + dados.get(0).toString() + " será analizado em no máximo 24 horas");
					 }
					 if(ver == 2){
						 Intent in = new Intent(getActivity (), RegistrarComoCandidato.class);
				         getActivity().finish();
						 startActivity(in);
					 }
					 if(dados.get(6).toString().length() > 3){
						 msgboasvindas.setVisibility(View.GONE);
						 tmsgboasvindas.setVisibility(View.VISIBLE);
						 tmsgboasvindas.setText(dados.get(6).toString());
						 botatualizar.setVisibility(View.GONE);
			         	 botaoeditar.setVisibility(View.VISIBLE);
					 }else{
						 msgboasvindas.setVisibility(View.VISIBLE);
						 tmsgboasvindas.setVisibility(View.GONE);
						 tmsgboasvindas.setText(dados.get(6).toString());
						 botatualizar.setVisibility(View.VISIBLE);
			         	 botaoeditar.setVisibility(View.GONE);
					 }
					 nome.setText(dados.get(0).toString());
					 nomeurna.setText(dados.get(1).toString());
					 local.setText(dados.get(5).toString() +" "+ dados.get(4).toString());
			         imageLoader.DisplayImage(dados.get(7).toString(), imagemcandidato);

					 
				 }else{
					 LinearLayout adm = (LinearLayout) android.findViewById(R.id.laypermitido);
					 adm.setVisibility(View.GONE);
				
				 }
			    return android;
            
    	}
	 
	@Override
	 public void onCreateContextMenu(ContextMenu menu, View v,
	   ContextMenuInfo menuInfo) {
	  super.onCreateContextMenu(menu, v, menuInfo);
	  
	  menu.setHeaderTitle("Gerenciar Imagem");
	  menu.add(0, v.getId(), 0, "Adicionar");
	  menu.add(0, v.getId(), 0, "Trocar");
	  menu.add(0, v.getId(), 0, "Remover");
	 }
	 @Override
	 public boolean onContextItemSelected(MenuItem item) {
	  if (item.getTitle() == "Adicionar") {
	   adicionarimagem();
	  } else if (item.getTitle() == "Trocar") {
	   trocarimagem();
	  } else if (item.getTitle() == "Remover") {
	   removerimagem();
	  } else {
	   return false;
	  }
	  return true;
	 }

		public boolean permitido(){
			boolean retorno = false;
		    @SuppressWarnings("resource")
			HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://ivoto.com.br/data_eleicoes/checa_perfil.php");
		    String responseString;
			try {
	     	AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
	                 new ProgressListener() {
	                     @Override
	                     public void transferred(long num) {
	                         
	                     }
	                 });
	     	
	         entity.addPart("idandroid", new StringBody(idandroid));
	         httppost.setEntity(entity);
	         HttpResponse response = httpclient.execute(httppost);
	         HttpEntity r_entity = response.getEntity();
	         int statusCode = response.getStatusLine().getStatusCode();
	         if (statusCode == 200) {
	        	 getActivity ();
				
	        	responseString = EntityUtils.toString(r_entity);
	        	if(responseString.length() > 10){
	        		sharedPref.edit().putString("admin", "sim").apply();
	        		
		            retorno = true;
	         	}else{
	         		sharedPref.edit().putString("admin", "no").apply();
		            
	         	}
	         } else {
	         }
	
	     } catch (ClientProtocolException e) {
	         responseString = e.toString();
	     } catch (IOException e) {
	         responseString = e.toString();
	     }
			return retorno;  
	 }
	 @SuppressLint("ShowToast")
	public void atualizamensagem(){
		 String idandroid = Secure.getString(getActivity ().getContentResolver(), Secure.ANDROID_ID);
		    @SuppressWarnings("resource")
			HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://ivoto.com.br/data_eleicoes/atualiza_mensagem.php");
		    String responseString;
			try {
	     	AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
	                 new ProgressListener() {
	                     @Override
	                     public void transferred(long num) {
	                         
	                     }
	                 });
	     	
	         entity.addPart("idandroid", new StringBody(idandroid));
	         String msg = msgboasvindas.getText().toString();
	         entity.addPart("msg", new StringBody(msg));
	         httppost.setEntity(entity);
	         HttpResponse response = httpclient.execute(httppost);
	         HttpEntity r_entity = response.getEntity();
	         int statusCode = response.getStatusLine().getStatusCode();
	         if (statusCode == 200) {
	        	
	        	responseString = EntityUtils.toString(r_entity);
	         	if(responseString.length() > 10){
	         		msgboasvindas.setVisibility(View.GONE);
	         		botatualizar.setVisibility(View.GONE);
	         		botaoeditar.setVisibility(View.VISIBLE);
	         		tmsgboasvindas.setText(msg);
	         		tmsgboasvindas.setVisibility(View.VISIBLE);
	         		
	         	}else{
	         	Toast.makeText(getActivity (), "Houve um erro", 700);
	         	}
	         } else {
	         }
	
	     } catch (ClientProtocolException e) {
	         responseString = e.toString();
	         System.out.println("resposta: " + responseString);
	     } catch (IOException e) {
	         responseString = e.toString();
	         System.out.println("resposta: " + responseString);
	     }
	 }
	 @SuppressLint("ShowToast")
		public void apagamensagem(){
			 String idandroid = Secure.getString(getActivity ().getContentResolver(), Secure.ANDROID_ID);
			    @SuppressWarnings("resource")
				HttpClient httpclient = new DefaultHttpClient();
			    HttpPost httppost = new HttpPost("http://ivoto.com.br/data_eleicoes/atualiza_mensagem.php");
			    String responseString;
				try {
		     	AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
		                 new ProgressListener() {
		                     @Override
		                     public void transferred(long num) {
		                         
		                     }
		                 });
		     	
		         entity.addPart("idandroid", new StringBody(idandroid));
		         String msg = msgboasvindas.getText().toString();
		         entity.addPart("msg", new StringBody(""));
		         
		         httppost.setEntity(entity);
		         HttpResponse response = httpclient.execute(httppost);
		         HttpEntity r_entity = response.getEntity();
		         int statusCode = response.getStatusLine().getStatusCode();
		         if (statusCode == 200) {
		        	
		        	responseString = EntityUtils.toString(r_entity);
		         	if(responseString.length() > 10){
		         		msgboasvindas.setVisibility(View.GONE);
		         		botatualizar.setVisibility(View.GONE);
		         		botaoeditar.setVisibility(View.VISIBLE);
		         		tmsgboasvindas.setText(msg);
		         		tmsgboasvindas.setVisibility(View.VISIBLE);
		         		
		         	}else{
		         	Toast.makeText(getActivity (), "Houve um erro", 700);
		         	}
		         } else {
		         }
		
		     } catch (ClientProtocolException e) {
		         responseString = e.toString();
		         System.out.println("resposta: " + responseString);
		     } catch (IOException e) {
		         responseString = e.toString();
		         System.out.println("resposta: " + responseString);
		     }
		 }
	 public void editamensagem(){
		msgboasvindas.setVisibility(View.VISIBLE);
		msgboasvindas.setText(tmsgboasvindas.getText().toString());
		msgboasvindas.clearFocus();
  		botatualizar.setVisibility(View.VISIBLE);
  		botaoeditar.setVisibility(View.GONE);
  		tmsgboasvindas.setVisibility(View.GONE);
	 }
	 public void sair(){
		 removecandidato();   
		 Intent in = new Intent(getActivity (), RegistrarComoCandidato.class);
         startActivity(in);
	 }
	 public void removecandidato(){
		 
		    @SuppressWarnings("resource")
			HttpClient httpclient = new DefaultHttpClient();
	     HttpPost httppost = new HttpPost("http://ivoto.com.br/data_eleicoes/cadastra_candidato_existente.php");

	     String responseString;
			try {
	     	AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
	                 new ProgressListener() {
	                     @Override
	                     public void transferred(long num) {
	                         
	                     }
	                 });
	     	
	         entity.addPart("idandroid", new StringBody(idandroid));
	         entity.addPart("remover", new StringBody("sim"));
	         httppost.setEntity(entity);
	         // Making server call
	         HttpResponse response = httpclient.execute(httppost);
	         HttpEntity r_entity = response.getEntity();

	         int statusCode = response.getStatusLine().getStatusCode();
	         if (statusCode == 200) {
	             // Server response
	             responseString = EntityUtils.toString(r_entity);
	             sharedPref.edit().putString("idcan", null).apply();
	             Intent intent = new Intent(getActivity(), RegistrarComoCandidato.class);
	             startActivity(intent); 
	         } else {
	             responseString = "Erro codigo: "
	                     + statusCode;
	             System.out.println("resposta: " + responseString);
	         }

	     } catch (ClientProtocolException e) {
	         responseString = e.toString();
	         System.out.println("resposta: " + responseString);
	     } catch (IOException e) {
	         responseString = e.toString();
	         System.out.println("resposta: " + responseString);
	     }
	 	}
	 public void trocarimagem(){
		 uploadfoto();
	 }
	 public void adicionarimagem(){
		 
	 }
	 public void removerimagem(){
		 
	 }
	 public void uploadfoto(){
		 Intent i = new Intent(
		 Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		 startActivityForResult(i, RESULT_LOAD_IMAGE);
	 }
	 @Override
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 System.out.println("Result activity: " + requestCode +"/"+ resultCode +"/"+ data); 
		 
		 if (requestCode == RESULT_LOAD_IMAGE && resultCode == act.RESULT_OK && null != data) {    
			 Uri selectedImage = data.getData();
	         String[] filePathColumn = { MediaStore.Images.Media.DATA };
	 
	         Cursor cursor = act.getContentResolver().query(selectedImage,
	                 filePathColumn, null, null, null);
	         cursor.moveToFirst();
	 
	         int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	         String picturePath = cursor.getString(columnIndex);
	         cursor.close();
	        System.out.println("Foto: " + picturePath);              
	       
	       
	        uploadimagem(picturePath);
	       
	     }
	 }
	 public void uploadimagem(String path){
		 detectaface(path);
	 }
	 public void detectaface(String patharquivo){
		 	Bitmap bm = getBitmap(patharquivo);	
		 
	    	if(bm != null){
	    	int width = bm.getWidth();
	    	int height = bm.getHeight();
	    	FaceDetector detector = new FaceDetector(width, height,3);
	    	Face[] faces = new Face[3];
	    	
	    	Bitmap bitmap565 = Bitmap.createBitmap(width, height, Config.RGB_565);
	    	Paint ditherPaint = new Paint();
	    	Paint drawPaint = new Paint();
	    	ditherPaint.setDither(true);
	    	drawPaint.setColor(Color.RED);
	    	drawPaint.setStyle(Paint.Style.STROKE);
	    	drawPaint.setStrokeWidth(2);
	    	Canvas canvas = new Canvas();
	    	canvas.setBitmap(bitmap565);
	    	canvas.drawBitmap(bm, 0, 0, ditherPaint);
	    	int facesFound = detector.findFaces(bitmap565, faces);
	    	
	    	PointF midPoint = new PointF();
	    	float eyeDistance = 0.0f;
	    	System.out.println(facesFound);
	    	if(facesFound > 0)
	    	{
	    	for(int index=0; index<facesFound; ++index){
	    	faces[index].getMidPoint(midPoint);
	    	Matrix matrix = new Matrix();
	        matrix.postScale(1f, 1f);
	    	eyeDistance = faces[index].eyesDistance();
	    	Bitmap resizedBitmap = null;
	    	
	    	resizedBitmap = Bitmap.createBitmap(bm,
	                Math.round(midPoint.x - (eyeDistance + (eyeDistance * 0.4f))),
	                Math.round(midPoint.y - (eyeDistance + (eyeDistance * 0.4f))),
	                Math.round(eyeDistance*3),
	                Math.round(eyeDistance*3.4f), matrix, true); 
	    	if(index == 0 && resizedBitmap != null){
	    		//enviarfoto.setImageBitmap(resizedBitmap);
	    		filePathenvio = this.saveToInternalSorage(resizedBitmap) + "/"+nomedoarquivo+".jpg";
	    		imagemcan = resizedBitmap;
	    		System.out.println("file path envio: " + filePathenvio);
	    		new UploadFileToServer().execute();
	    		
	    	}
	    	}
	    	}else{
	    		Toast.makeText(act.getApplicationContext(), "Nenhum rosto detectado na imagem, escolha uma imgem que contenha um rosto", Toast.LENGTH_LONG).show();
	    		imagemcandidato.setImageResource(R.id.enviarimagem);
	    	}
	    	}else{
	    		Toast.makeText(act.getApplicationContext(), "Erro ao processar a imagem, envie um nova imagem", Toast.LENGTH_LONG).show();
	    		imagemcandidato.setImageResource(R.id.enviarimagem);
	    	}
	    	
	    }
	 private Bitmap getBitmap(String path) {
	 	    final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
	 	      	    // Decode image size
	 	    BitmapFactory.Options o = new BitmapFactory.Options();
	 	    o.inJustDecodeBounds = true;
	 	    BitmapFactory.decodeFile(path,o);
	 	
	 	    int scale = 1;
	 	    while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > 
	 	          IMAGE_MAX_SIZE) {
	 	       scale++;
	 	    }

	 	    Bitmap b = null;
	 	
	 	    if (scale > 1) {
	 	        scale--;
	 	        // scale to max possible inSampleSize that still yields an image
	 	        // larger than target
	 	        o = new BitmapFactory.Options();
	 	        o.inSampleSize = scale;
	 	        
	 	        b = BitmapFactory.decodeFile(path,o);

	 	        // resize to desired dimensions
	 	        int height = b.getHeight();
	 	        int width = b.getWidth();
	 	            	        double y = Math.sqrt(IMAGE_MAX_SIZE
	 	                / (((double) width) / height));
	 	        double x = (y / height) * width;

	 	        Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x, 
	 	           (int) y, true);
	 	        b.recycle();
	 	        b = scaledBitmap;

	 	        System.gc();
	 	    } else {
	 	        b = BitmapFactory.decodeFile(path);
	 	    }
	 	   
	 	    return b;
	 	}
	 private String saveToInternalSorage(Bitmap bitmapImage){
	        ContextWrapper cw = new ContextWrapper(act.getApplicationContext());
	       
	        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
	        File mypath=new File(directory,nomedoarquivo+".jpg");

	        FileOutputStream fos = null;
	        try {           

	            fos = new FileOutputStream(mypath);

	       // Use the compress method on the BitMap object to write image to the OutputStream
	            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
	            fos.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return directory.getAbsolutePath();
	    }
	 /**
	     * Uploading the file to server
	     * */
	    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
	        @Override
	        protected void onPreExecute() {
	            // setting progress bar to zero
	        	super.onPreExecute();
	        }
	 
	        @Override
	        protected void onProgressUpdate(Integer... progress) {
	            //txtPercentage.setText(String.valueOf(progress[0]) + "%");
	        }
	 
	        @Override
	        protected String doInBackground(Void... params) {
	            return uploadFile();
	        }
	 
	        @SuppressWarnings("deprecation")
	        private String uploadFile() {
	            String responseString = null;
	 
	            @SuppressWarnings("resource")
				HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost("http://ivoto.com.br/data_eleicoes/upload_image.php");
	 
	            try {
	                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
	                        new ProgressListener() {
	                            @Override
	                            public void transferred(long num) {
	                                publishProgress((int) ((num / (float) totalSize) * 100));
	                            }
	                        });
	 
	                File sourceFile = new File(filePathenvio);
	 
	                // Adding file data to http body
	                entity.addPart("image", new FileBody(sourceFile));
	 
	                // Extra parameters if you want to pass to server
	                entity.addPart("website",
	                        new StringBody("www.androidhive.info"));
	                entity.addPart("email", new StringBody("abc@gmail.com"));
	 
	                totalSize = entity.getContentLength();
	                httppost.setEntity(entity);
	 
	                // Making server call
	                HttpResponse response = httpclient.execute(httppost);
	                HttpEntity r_entity = response.getEntity();
	 
	                int statusCode = response.getStatusLine().getStatusCode();
	                if (statusCode == 200) {
	                    // Server response
	                    responseString = EntityUtils.toString(r_entity);
	                } else {
	                    responseString = "Error occurred! Http Status Code: "
	                            + statusCode;
	                }
	 
	            } catch (ClientProtocolException e) {
	                responseString = e.toString();
	            } catch (IOException e) {
	                responseString = e.toString();
	            }
	 
	            return responseString;
	 
	        }
	 
	        @Override
	        protected void onPostExecute(String result) {
	        	imagemcandidato.setImageBitmap(imagemcan);
	        	super.onPostExecute(result);
	        }
	 
	    }
    }

