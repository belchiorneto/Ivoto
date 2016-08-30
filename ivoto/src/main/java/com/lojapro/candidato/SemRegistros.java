package com.lojapro.candidato;
/*
 * Esta classe será responsável por autenticar usuários ainda não foram candidatos
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.lojapro.can.ListaCidades;
import com.lojapro.can.ParseCidadeXML;
import com.lojapro.can.ParseEstadoXML;
import com.lojapro.can.Principal;
import com.lojapro.can.XMLParseCidades;
import com.lojapro.candidato.AndroidMultiPartEntity.ProgressListener;
import com.lojapro.can.R;

import analytcs.GoogleAnalyticsApp;
import analytcs.GoogleAnalyticsApp.TrackerName;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class SemRegistros extends Activity {
	String idandroid;
	ImageView enviarfoto;
	String filePathenvio;
	TextView txtPercentage;
	long totalSize = 0;
	Bitmap imagemcan;
	Button cadastro;
	XMLParseCidades listacidades;
	ArrayList<String> listacidadesparsed;
	Spinner spinnerestados;
	Spinner spinnercidades;
	Activity act;
	int[] idcidades;
	// nome do arquivo de imagem a ser enviado
	String nomedoarquivo = null;
	private SharedPreferences sharedPref;
	private static int RESULT_LOAD_IMAGE = 1;
	
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 
		 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
         StrictMode.setThreadPolicy(policy);
         sharedPref = this.getSharedPreferences("com.lojapro.can", Context.MODE_PRIVATE);
		 setContentView(R.layout.activity_semregistro);
		 act = this;
		 idandroid = Secure.getString(act.getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
		 nomedoarquivo = idandroid;
		 // se��o google analytcs
		// Tracker t = ((GoogleAnalyticsApp) getApplication()).getTracker(TrackerName.APP_TRACKER);
		// t.setScreenName("Home");
		// t.send(new HitBuilders.AppViewBuilder().build());

		 spinnerestados = (Spinner) findViewById( R.id.SpinnerEstado );
		 spinnercidades = (Spinner) findViewById( R.id.spinnercidade );
		 enviarfoto = (ImageView)findViewById(R.id.enviarimagem);
		 cadastro = (Button)findViewById(R.id.botcadastrar);
		 spinnerestados.setOnItemSelectedListener(new OnItemSelectedListener() {
			    @Override
			    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			    	spinnercidades.setAdapter(null);
			    	ParseEstadoXML p = new ParseEstadoXML();
			    	String text = spinnerestados.getSelectedItem().toString();
			    	int ide = p.retornaid(text,SemRegistros.this);
			    	listacidades = new XMLParseCidades(act, ide);
			    	listacidadesparsed = carregacidades();
			    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(SemRegistros.this,
			    	        android.R.layout.simple_spinner_item, listacidadesparsed);
			    	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			    	    spinnercidades.setAdapter(adapter);
			    }

			    @Override
			    public void onNothingSelected(AdapterView<?> parentView) {
			        // your code here
			    }

			});
		 
		 enviarfoto.setOnClickListener(new OnClickListener() {
	            public void onClick(View v)
	            {
	            	enviarfoto.setImageResource(R.drawable.load);
	            	uploadfoto();
	            }
		 });
		 cadastro.setOnClickListener(new OnClickListener() {
	            public void onClick(View v)
	            {
	            	cadastracandidato();
	            }
		 });
	           
	 }
	 public void uploadfoto(){
		 Intent i = new Intent(
				    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				    startActivityForResult(i, RESULT_LOAD_IMAGE);
	 }
	 @Override
		protected void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			GoogleAnalytics.getInstance(SemRegistros.this).reportActivityStart(this);
		}


		@Override
		protected void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
			GoogleAnalytics.getInstance(SemRegistros.this).reportActivityStop(this);
		}
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	     super.onActivityResult(requestCode, resultCode, data);
	      
	     if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
	         Uri selectedImage = data.getData();
	         String[] filePathColumn = { MediaStore.Images.Media.DATA };
	 
	         Cursor cursor = getContentResolver().query(selectedImage,
	                 filePathColumn, null, null, null);
	         cursor.moveToFirst();
	 
	         int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	         String picturePath = cursor.getString(columnIndex);
	         cursor.close();
	        Intent intent = new Intent(SemRegistros.this, UploadActivity.class);
	        intent.putExtra("filepath", picturePath);
	        uploadimagem(picturePath);
	        //startActivity(intent);
	     }
	}
	 public void uploadimagem(String path){
		 detectaface(path);
	 }
	 public void detectaface(String patharquivo){
		 	Bitmap bm = getBitmap(patharquivo);	
		 	/*
		 	try
		    {
		      BitmapFactory.Options options = new BitmapFactory.Options();
		      options.inSampleSize = 1;
		      bm =  BitmapFactory.decodeFile(patharquivo, options);
		    }
		    catch(Exception e)
		    {
		    System.out.println(e.toString());
		    }
		 	 */
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
	    		
	    		filePathenvio = this.saveToInternalSorage(resizedBitmap) + "/"+nomedoarquivo+".jpg";
	    		imagemcan = resizedBitmap;
	    		
	    		new UploadFileToServer().execute();
	    		
	    	}
	    	}
	    	}else{
	    		Bitmap myBitmap = BitmapFactory.decodeFile(patharquivo);
	    	
	    		filePathenvio = this.saveToInternalSorage(myBitmap) + "/"+nomedoarquivo+".jpg";
	    		imagemcan = myBitmap;
	    		new UploadFileToServer().execute();
	    	}
	    	}else{
	    		Bitmap myBitmap = BitmapFactory.decodeFile(patharquivo);
		    	
	    		filePathenvio = this.saveToInternalSorage(myBitmap) + "/"+nomedoarquivo+".jpg";
	    		imagemcan = myBitmap;
	    		new UploadFileToServer().execute();
	    	}
	    	
	    }
	 private Bitmap getBitmap(String path) {
 	    final int IMAGE_MAX_SIZE = 6200000; // 1.2MP
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
	        ContextWrapper cw = new ContextWrapper(getApplicationContext());
	       
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
	        	enviarfoto.setImageBitmap(imagemcan);
	        	super.onPostExecute(result);
	        }
	 
	    }
	    @SuppressWarnings("deprecation")
		public void cadastracandidato(){
	    TextView editnome = (TextView)findViewById(R.id.EditTextName);
	    TextView editemail = (TextView)findViewById(R.id.EditTextEmail);
	    String nomedocandidato = editnome.getText().toString();
	    String emaildocandidato = editemail.getText().toString();
	    String cidade = spinnercidades.getSelectedItem().toString();
	    String estado = spinnerestados.getSelectedItem().toString();
	    // checa campos
	    if (nomedocandidato.length() < 3){
	    	Toast toast = Toast.makeText(act.getApplicationContext(), "Preencha corretamente o campo Nome", 5);
	    	toast.show();
	    	return;
	    }
	    if (emaildocandidato.length() < 3){
	    	Toast toast = Toast.makeText(act.getApplicationContext(), "Preencha corretamente o campo Email", 5);
	    	toast.show();
	    	return;
	    }
	    
	    @SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://ivoto.com.br/data_eleicoes/cadastra_candidato.php");

        String responseString;
		try {
        	AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new ProgressListener() {
                        @Override
                        public void transferred(long num) {
                            
                        }
                    });
        	
            entity.addPart("nome", new StringBody(nomedocandidato));
            entity.addPart("email", new StringBody(emaildocandidato));
            entity.addPart("idandroid", new StringBody(idandroid));
            entity.addPart("cidade", new StringBody(cidade));
            entity.addPart("estado", new StringBody(estado));
                      totalSize = entity.getContentLength();
            httppost.setEntity(entity);

            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
            	responseString = EntityUtils.toString(r_entity);
            	String[] parts = responseString.split("-");
            	sharedPref.edit().putInt("idcan", Integer.parseInt(parts[1])).apply();
            	Intent in = new Intent(SemRegistros.this, Candidato.class);
                startActivity(in); 
                
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
	    public ArrayList<String> carregacidades(){
			
	    	try {
				org.w3c.dom.Document d = listacidades.execute("http://ivoto.com.br/data_eleicoes/cidades.php").get();
				 listacidadesparsed = CidadesFromDoc(d);
					    
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	return listacidadesparsed;
		}
	    private ArrayList<String> CidadesFromDoc(org.w3c.dom.Document doc)
		{
			NodeList nl = doc.getElementsByTagName("cidade");
			ArrayList<String> retorno = new ArrayList<String>();
			idcidades = new int[nl.getLength()];
			for(int i = 0; i<nl.getLength(); i++){
				Node node = nl.item(i);
				Element fstElmnt = (Element) node;
				NodeList nameList = fstElmnt.getElementsByTagName("nome");
				NodeList idlist = fstElmnt.getElementsByTagName("id");
				Element nameElement = (Element) nameList.item(0);
				Element idelement = (Element) idlist.item(0);
				nameList = nameElement.getChildNodes();
				idlist = idelement.getChildNodes();
				String namevalue = ((Node) nameList.item(0)).getNodeValue();
				int idecid = Integer.parseInt(((Node) idlist.item(0)).getNodeValue());
				retorno.add(namevalue);
				idcidades[i] = idecid;
			}
			
			return retorno;
		}
}
