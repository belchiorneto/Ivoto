package com.lojapro.can;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.Settings.Secure;
import android.util.Patterns;

@SuppressLint("NewApi")
public class GetUserData {
	Context ctx;
	static DataBaseCandidatos db;
    static List<TableMeusCandidatos> list;
    public GetUserData(Context context) {
		this.ctx = context;
	}
	public String GetName(){
		Cursor c = ctx.getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
		String nome = "";
			// c�gido para pegar o nome do usuario do celular
		c.moveToFirst();
		nome = c.getString(c.getColumnIndex("display_name"));
		c.close();
		return nome;
	}
	public String GetIdCandidato(){
		final VariaveiGlobais variaveis = (VariaveiGlobais) Principal.getAppContext();
		String idcandidato = variaveis.getIdCandidato();
		return idcandidato;
	}
	public String GetEmail(){
		ArrayList<String> emlRecs = this.getNameEmailDetails();
		String email;
		email = emlRecs.get(0);
		return email;
	}
	public String GetAndroidId(){
		String androidid = "";
			// pegar o id do celular
		androidid = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
		return androidid;
	}
	public String GetBairro(){
		return Principal.getbairro();
	}
	public String GetCidade(){
		return Principal.getcidade();
	}
	public String GetEstado(){
		return Principal.getestado();
	}
	public String GetCep(){
		return Principal.getcep();
	}
	public String GetNumero(){
		return Principal.getnumero();
	}
	public String GetPais(){
		return Principal.getpais();
	}
	public String GetRua(){
		return Principal.getrua();
	}
	public ArrayList<String> getNameEmailDetails() {
	    ArrayList<String> emlRecs = new ArrayList<String>();
	    HashSet<String> emlRecsHS = new HashSet<String>();
	    ContentResolver cr = Principal.getAppContext().getContentResolver();
	    String[] PROJECTION = new String[] { ContactsContract.RawContacts._ID, 
	            ContactsContract.Contacts.DISPLAY_NAME,
	            ContactsContract.Contacts.PHOTO_ID,
	            ContactsContract.CommonDataKinds.Email.DATA, 
	            ContactsContract.CommonDataKinds.Photo.CONTACT_ID };
	    String order = "CASE WHEN " 
	            + ContactsContract.Contacts.DISPLAY_NAME 
	            + " NOT LIKE '%@%' THEN 1 ELSE 2 END, " 
	            + ContactsContract.Contacts.DISPLAY_NAME 
	            + ", " 
	            + ContactsContract.CommonDataKinds.Email.DATA
	            + " COLLATE NOCASE";
	    String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
	    Cursor cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, order);
	    
	    Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
	    Account[] accounts = AccountManager.get(Principal.getAppContext()).getAccounts();
	    for (Account account : accounts) {
	        if (emailPattern.matcher(account.name).matches()) {
	            String possibleEmail = account.name;
	            emlRecs.add(possibleEmail);
	        }
	    }
	    if (cur.moveToFirst()) {
	        do {
	            // names comes in hand sometimes
	            String name = cur.getString(1);
	            String emlAddr = cur.getString(3);

	            // keep unique only
	            if (emlRecsHS.add(emlAddr.toLowerCase())) {
	                emlRecs.add(emlAddr);
	            }
	        } while (cur.moveToNext());
	    }
	    
	    cur.close();
	    return emlRecs;
	}
	
}
