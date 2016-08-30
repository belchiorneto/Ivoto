package com.lojapro.can;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseCandidatos extends SQLiteOpenHelper {

	// database version
	private static final int database_VERSION = 1;
	
	// database name
		private static final String database_NAME = "ivoto";
		private static final String table_CANDIDATOS = "candidatos";
		private static final String candidato_ID = "id";
		private static final String candidato_NAME = "nome";
		private static final String candidato_EMAILUSER = "emailuser";

		private static final String[] COLUMNS = { candidato_ID, candidato_NAME, candidato_EMAILUSER };

	public DataBaseCandidatos(Context context) {
		super(context, database_NAME, null, database_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create user table
		String CREATE_BOOK_TABLE = "CREATE TABLE "+ table_CANDIDATOS +" ( " + "id INTEGER PRIMARY KEY, " + ""+candidato_NAME+" TEXT, " + ""+candidato_EMAILUSER+" TEXT )";
		db.execSQL(CREATE_BOOK_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// drop books table if already exists
		db.execSQL("DROP TABLE IF EXISTS "+table_CANDIDATOS);
		this.onCreate(db);
	}

	public void createRegistro(TableMeusCandidatos tblcandidato) {
		// get reference of the BookDB database
		SQLiteDatabase db = this.getWritableDatabase();

		// make values to be inserted
		ContentValues values = new ContentValues();
		values.put(candidato_NAME, tblcandidato.getNome());
		values.put(candidato_EMAILUSER, tblcandidato.getEmailUser());
		values.put(candidato_ID, tblcandidato.getId());

		// insert book
		db.insert(table_CANDIDATOS, null, values);

		// close database transaction
		db.close();
	}

	public TableMeusCandidatos readCandidato(int id) {
		// get reference of the BookDB database
		SQLiteDatabase db = this.getReadableDatabase();

		// get book query
		Cursor cursor = db.query(table_CANDIDATOS, // a. table
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

	public List getAllUsers() {
		List candidatos = new LinkedList();

		// select book query
		String query = "SELECT  * FROM " + table_CANDIDATOS;

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

	public int updateCandidato(TableMeusCandidatos candidato) {

		// get reference of the BookDB database
		SQLiteDatabase db = this.getWritableDatabase();

		// make values to be inserted
		ContentValues values = new ContentValues();
		values.put(candidato_NAME, candidato.getNome());
		values.put(candidato_EMAILUSER, candidato.getEmailUser());
		values.put(candidato_ID, candidato.getId());

		// update
		int i = db.update(table_CANDIDATOS, values, candidato_ID + " = ?", new String[] { String.valueOf(candidato.getId()) });

		db.close();
		return i;
	}

	// Deleting single book
	public void deleteBook(TableUsuario tbusuario) {

		// get reference of the BookDB database
		SQLiteDatabase db = this.getWritableDatabase();

		// delete book
		db.delete(table_CANDIDATOS, candidato_ID + " = ?", new String[] { String.valueOf(tbusuario.getId()) });
		db.close();
	}
	public void clearCandidatos() {
		// get reference of the BookDB database
				SQLiteDatabase db = this.getWritableDatabase();

				// delete book
				db.rawQuery("DELETE FROM " + table_CANDIDATOS, null);
				//db.delete(table_CANDIDATOS, candidato_ID + " = ?", new String[] { String.valueOf(tbusuario.getId()) });
				//db.close();
	}
	
}
