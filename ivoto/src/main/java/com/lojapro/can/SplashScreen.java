package com.lojapro.can;

import com.lojapro.alertas.SampleAlarmReceiver;
import com.lojapro.can.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.LocaleDisplayNames;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import android.location.Geocoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class SplashScreen extends Activity {
	// ProgressBar configs
    private static final int PROGRESS = 0x1;
    private ProgressBar mProgress;
    private TextView progressMessage;
    SharedPreferences sharedPref;
	SampleAlarmReceiver alarm = new SampleAlarmReceiver();

    // context and activity data
    private Context ctx;
    private Activity act;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1500;

    // dados a serem registrados ao iniciar o aplicativo
    private String androidid;
    private String idcidade;
    private double lat;
    private double lon;
    private boolean savefile;

    // dados necessarios para pegar o id da cidade
    private GPSTracker gps;
    private Location location;
    private String nomeCidade;
    private String nomeEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        act = this;
        ctx = act.getApplicationContext();
        gps = new GPSTracker(ctx);
        sharedPref = getSharedPreferences(
         	      "com.lojapro.can", Context.MODE_PRIVATE);
        androidid = sharedPref.getString("androidid", "0");
        idcidade = sharedPref.getString("idcidade", "0");
        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        progressMessage = (TextView) findViewById(R.id.progress_message);
        if (androidid == "0"){
        	androidid = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
        	sharedPref.edit().putString("androidid", androidid).apply();
        }

        //alarm.cancelAlarm(this);
        alarm.setAlarm(this);
        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {

            }
        }, SPLASH_TIME_OUT);
        String[] tasks = new String[4];
        tasks[0] = "location";
        tasks[1] = "cidade";
        tasks[2] = "candidatos";
        tasks[3] = "propostas";
        savefile = SaveFile(idcidade);
        if(savefile || (idcidade.equals("0"))) {
            new CarregaDados().execute(tasks);
            new GetGpsData().execute();
            new Carregacidades().execute("https://ivoto.com.br/data_eleicoes/cidade.php");
            new Carregacandidatos().execute("http://ivoto.com.br/data_eleicoes/listacan.php");
            // This method will be executed once the timer is over
            // Start your app main activity
            //Intent i = new Intent(SplashScreen.this, Inicio.class);
            //startActivity(i);

            // close this activity
            //finish();
        }else{
            // This method will be executed once the timer is over
            // Start your app main activity
            Intent i = new Intent(SplashScreen.this, Inicio.class);
             startActivity(i);

            // close this activity
            finish();
        }
    }

    private class CarregaDados extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i<params.length; i++){
                Log.d("async", params[i]);
                try {
                    Thread.sleep(1000);
                    publishProgress((int) ((i / (float) 4) * 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            /*
            int count = 50;
            for (int i = 0; i < 50; i++) {
                try {
                    Thread.sleep(200);
                    publishProgress((int) ((i / (float) count) * 100));
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            */
            return "Executed";
        }
        @Override
        protected void onPostExecute(String result) {
            Log.d("async", "Executado ");
        }
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            setProgressPercent(values[0], "Carregando, ... dados");
        }
    }
    private class Carregacidades extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String retorno = null;
            URL url;
            try {
                publishProgress((int) ((2 / (float) 4) * 100));
                url = new URL(params[0] +"?lat="+lat+"&lon="+lon+"&cid="+ URLEncoder.encode(nomeCidade)+"&estado="+URLEncoder.encode(nomeEstado));
                Log.d("async", "Executando URL: " + params[0] +"/"+nomeCidade);
                String conteudo = contents(url).trim();
                String[] data = conteudo.split("-");
                String cidade = data[1].trim();
                Log.d("asybc", "Cidade Encontrada: " + cidade);
                idcidade = cidade;
                sharedPref.edit().putString("idcidade", idcidade).apply();

                Thread.sleep(500);
                publishProgress((int) ((4 / (float) 4) * 100));
                retorno = cidade;
          } catch (Exception e) {
                System.out.println(e.getCause());
                retorno = null;
            }
            return retorno;
        }
        @Override
        protected void onPostExecute(String result) {

        }
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            setProgressPercent(values[0], "Carregando Cidade");
        }
    }
    private class Carregacandidatos extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String retorno = null;
            URL url;
            String FILENAME = idcidade;
            try {
                Thread.sleep(500);
                publishProgress((int) ((1 / (float) 4) * 100));
                if(savefile) {
                    publishProgress((int) ((2 / (float) 4) * 100));
                    url = new URL(params[0] + "?cid=" + idcidade);
                    String conteudo = contents(url).trim();
                    retorno = conteudo;
                    try {
                        FileOutputStream fos = openFileOutput(FILENAME, ctx.MODE_PRIVATE);
                        fos.write(conteudo.getBytes());
                        fos.close();
                    } catch (Exception e) {
                        System.out.println(e.getStackTrace());
                    }
                    Thread.sleep(500);
                    publishProgress((int) ((3 / (float) 4) * 100));
                }else{
                   Log.d("async", "Sem necessidade de salvar arquivo");
                }
                publishProgress((int) ((4 / (float) 4) * 100));
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
                retorno = null;
            }
            return retorno;
        }
        @Override
        protected void onPostExecute(String result) {

        }
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            setProgressPercent(values[0], "Carregando Candidatos");
        }
    }
    private class GetGpsData extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.d("asybc", "Executando GPS");
            String retorno = null;

            if(gps.canGetLocation){
                try {
                    lat = -3.71062; //gps.getLatitude();
                    lon = -38.56630; //gps.getLongitude();
                    Geocoder geoCoder = new Geocoder(ctx, Locale.getDefault());
                    StringBuilder builder = new StringBuilder();
                    List<Address> address = geoCoder.getFromLocation(lat, lon, 1);
                    if (address.size() > 0)
                        nomeCidade = address.get(0).getLocality();
                        nomeEstado = address.get(0).getAdminArea();
                        Log.d("async", "Cidade --  estado:" + nomeCidade +"/"+ nomeEstado);
                    retorno = nomeCidade;
                } catch (IOException e) {
                    Log.d("Erro", e.getLocalizedMessage());
                    Log.d("Erro", e.getMessage());
                    Log.d("Erro", String.valueOf(e.getStackTrace()));
                } catch (NullPointerException e) {
                    Log.d("Erro", e.getLocalizedMessage());
                    Log.d("Erro", e.getMessage());
                    Log.d("Erro", String.valueOf(e.getStackTrace()));
                }
            }else{
                Log.d("erro", "Classe GPSTRacker não iniciada");
            }

            return retorno;
        }
        @Override
        protected void onPostExecute(String result) {

        }
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            setProgressPercent(values[0], "Carregando Cidade");
        }
    }
    public void setProgressPercent(Integer percent, String message){
        mProgress.setProgress(percent);
        progressMessage.setText(message +" "+percent.toString() + "%");
        Log.d("async", message +"/"+percent.toString());
    }
    public static String contents(URL url) throws IOException {
        StringBuilder b = new StringBuilder();
        BufferedReader r = new BufferedReader(
                new InputStreamReader(url.openStream()));

        try {
            for(String line; (line = r.readLine()) != null; ) {
                b.append(line).append("\n");
            }
        } finally {
            close(r);
        }
        return b.toString();
    }


    public static void close(Closeable stream) {
        try {
            stream.close();
        } catch (IOException e) {
        }
    }

    public boolean SaveFile(String arquivo){
        boolean ret = false;
        // checa se o arquivo existe e se tem idade maior que meio-dia
        File file = new File(ctx.getFilesDir() + "/" + arquivo);
        if(file == null) {
            return false;
        }else {
            long age = (System.currentTimeMillis() - file.lastModified()) / 1000;
            if (age > (3600 * 12)) {
                return true;
            }
        }
        return ret;
    }

}