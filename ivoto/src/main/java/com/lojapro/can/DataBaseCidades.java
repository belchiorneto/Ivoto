package com.lojapro.can;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseCidades extends SQLiteOpenHelper {

	// database version
	private static final int database_VERSION = 1;
	
	// database name
		private static final String database_NAME = "ivoto";
		private static final String table_CIDADES = "cidades";
		private static final String cidade_ID = "id";
		private static final String cidade_NAME = "nome";
		private static final String cidade_IDESTADO = "idestado";

		private static final String[] COLUMNS = { cidade_ID, cidade_NAME, cidade_IDESTADO };

	public DataBaseCidades(Context context) {
		super(context, database_NAME, null, database_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("Debug ivoto chamada oncreate database cidades");
		// SQL statement to create user table
		String CREATE_BOOK_TABLE = "CREATE TABLE "+ table_CIDADES +" ( " + "id INTEGER PRIMARY KEY, " + ""+cidade_NAME+" TEXT, " + ""+cidade_IDESTADO+" INTEGER )";
		db.execSQL(CREATE_BOOK_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// drop books table if already exists
		
		Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+table_CIDADES+"'", null);
	    if(cursor!=null) {
	        if(cursor.getCount()>0) {
	         cursor.close();
	         System.out.println("Tabela existe" + cursor.getCount());
	        }else{
	        cursor.close();
	         System.out.println("Tabela nao existe");
	        this.onCreate(db);
	        }
	    }
		
	}

	public void createRegistro(TableCidades tbcidade) {
		// get reference of the BookDB database
		SQLiteDatabase db = this.getWritableDatabase();

		// make values to be inserted
		ContentValues values = new ContentValues();
		values.put(cidade_NAME, tbcidade.getNome());
		values.put(cidade_IDESTADO, tbcidade.getIdEstado());
		values.put(cidade_ID, tbcidade.getId());

		// insert book
		if(!checaexistencia(tbcidade.getId())){
			db.insert(table_CIDADES, null, values);
		}

		// close database transaction
		db.close();
	}

	public TableMeusCandidatos readCandidato(int id) {
		// get reference of the BookDB database
		SQLiteDatabase db = this.getReadableDatabase();

		// get book query
		Cursor cursor = db.query(table_CIDADES, // a. table
				COLUMNS, " id = ?", new String[] { String.valueOf(id) }, null, null, null, null);

		// if results !=null, parse the first one
		if (cursor != null)
			cursor.moveToFirst();

		TableMeusCandidatos user = new TableMeusCandidatos();
		user.setId(Integer.parseInt(cursor.getString(0)));
		user.setNome(cursor.getString(1));
		user.setEmailUser(cursor.getString(2));

		return user;
	}
	public int getTableCount() {
		int count = 0;

		// select book query
		String query = "SELECT count(*) FROM " + table_CIDADES;

		// get reference of the BookDB database
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// parse all results
		
		if(cursor.getCount()>0) {
			//count = Integer.parseInt(cursor.getString(0));
			System.out.println(cursor.getCount());
		}
		return count;
	}
	public List getAllUsers() {
		List candidatos = new LinkedList();

		// select book query
		String query = "SELECT  * FROM " + table_CIDADES;

		// get reference of the BookDB database
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// parse all results
		TableMeusCandidatos can = null;
		if (cursor.moveToFirst()) {
			do {
				can = new TableMeusCandidatos();
				can.setId(Integer.parseInt(cursor.getString(0)));
				can.setNome(cursor.getString(1));
				can.setEmailUser(cursor.getString(2));

				// Add book to books
				candidatos.add(can);
			} while (cursor.moveToNext());
		}
		return candidatos;
	}

	public int updateCandidato(TableCidades tablcidade) {

		// get reference of the BookDB database
		SQLiteDatabase db = this.getWritableDatabase();

		// make values to be inserted
		ContentValues values = new ContentValues();
		values.put(cidade_NAME, tablcidade.getNome());
		values.put(cidade_IDESTADO, tablcidade.getIdEstado());
		values.put(cidade_ID, tablcidade.getId());

		// update
		int i = db.update(table_CIDADES, values, cidade_ID + " = ?", new String[] { String.valueOf(tablcidade.getId()) });

		db.close();
		return i;
	}

	// Deleting single book
	public void deleteCidade(TableCidades tablcidade) {

		// get reference of the BookDB database
		SQLiteDatabase db = this.getWritableDatabase();

		// delete book
		db.delete(table_CIDADES, cidade_ID + " = ?", new String[] { String.valueOf(tablcidade.getId()) });
		db.close();
	}
	public void clearCandidatos() {
		// get reference of the BookDB database
				SQLiteDatabase db = this.getWritableDatabase();

				// delete book
				db.rawQuery("DELETE FROM " + table_CIDADES, null);
				//db.delete(table_CIDADES, cidade_ID + " = ?", new String[] { String.valueOf(tbusuario.getId()) });
				//db.close();
	}
	public boolean checaexistencia(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from "+ table_CIDADES+ " where id = '"+id+"'", null);
		 if(cursor.getCount()>0) {
			 return true;
		 }else{
			 return false;
		 }
	}
	
}
