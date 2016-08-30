package com.lojapro.candidato;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lojapro.can.Principal;
import com.lojapro.candidato.AndroidMultiPartEntity.ProgressListener;
import com.lojapro.can.R;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
 
@SuppressWarnings("deprecation")
public class UploadActivity extends Activity {
    private ProgressBar progressBar;
    private String filePath = null;
    private String filePathenvio = null;
    String nomedoarquivo = null;
    private TextView txtPercentage;
    private ImageView crop1;
    private ImageView crop2;
    private ImageView crop3;
    long totalSize = 0;
    private Intent intent;
    ContentResolver mContentResolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        crop1 = (ImageView)findViewById(R.id.crop1);
        crop2 = (ImageView)findViewById(R.id.crop2);
        crop3 = (ImageView)findViewById(R.id.crop3);
        intent = getIntent();
        nomedoarquivo = Secure.getString(Principal.getAppContext().getContentResolver(), Secure.ANDROID_ID);
        // Changing action bar background color
        getActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor(getResources().getString(
                        R.color.white))));
 
        getIntent();
 
        // image or video path that is captured in previous activity
        //filePath = i.getStringExtra("filePath");
        filePath = intent.getStringExtra("filepath");
      }
    @Override
    protected void onStart() {
        super.onStart();

        detectaface();
    }
    public void detectaface(){
    	Bitmap bitmap = getBitmap(filePath);
    	int width = bitmap.getWidth();
    	int height = bitmap.getHeight();
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
    	canvas.drawBitmap(bitmap, 0, 0, ditherPaint);
    	int facesFound = detector.findFaces(bitmap565, faces);
    	
    	PointF midPoint = new PointF();
    	float eyeDistance = 0.0f;
    	if(facesFound > 0)
    	{
    	for(int index=0; index<facesFound; ++index){
    	faces[index].getMidPoint(midPoint);
    	Matrix matrix = new Matrix();
        matrix.postScale(1f, 1f);
    	eyeDistance = faces[index].eyesDistance();
    	faces[index].confidence();
    	Bitmap resizedBitmap = null;
    	
    	resizedBitmap = Bitmap.createBitmap(bitmap,
                Math.round(midPoint.x - (eyeDistance + (eyeDistance * 0.4f))),
                Math.round(midPoint.y - (eyeDistance + (eyeDistance * 0.4f))),
                Math.round(eyeDistance*3),
                Math.round(eyeDistance*3.4f), matrix, true); 
    	if(index == 0 && resizedBitmap != null){
    		crop1.setImageBitmap(resizedBitmap);
    		filePathenvio = this.saveToInternalSorage(resizedBitmap) + "/"+nomedoarquivo+".jpg";
    		
    		System.out.println("file path envio: " + filePathenvio);
    		new UploadFileToServer().execute();
    	}
    	if(index == 1 && resizedBitmap != null){
    		crop2.setImageBitmap(resizedBitmap);
    	}
    	if(index == 2 && resizedBitmap != null){
    		crop3.setImageBitmap(resizedBitmap);
    	}
    	}
    	}
    	
    }
    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }
 
        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);
 
            // updating progress bar value
            progressBar.setProgress(progress[0]);
 
            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }
 
        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }
 
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
            // showing the server response in an alert dialog
            showAlert(result);
 
            super.onPostExecute(result);
        }
 
    }
 
    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private String saveToInternalSorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
         // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        
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
}