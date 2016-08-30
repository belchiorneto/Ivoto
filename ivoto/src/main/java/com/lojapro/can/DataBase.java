package com.lojapro.can;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

	// database version
	private static final int database_VERSION = 1;
	
	// database name
		private static final String database_NAME = "ivoto";
		private static final String table_USER = "user";
		private static final String user_ID = "id";
		private static final String user_NAME = "name";
		private static final String user_EMAIL = "email";

		private static final String[] COLUMNS = { user_ID, user_NAME, user_EMAIL };

	public DataBase(Context context) {
		super(context, database_NAME, null, database_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create user table
		String CREATE_BOOK_TABLE = "CREATE TABLE "+ table_USER +" ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + ""+user_NAME+" TEXT, " + ""+user_EMAIL+" TEXT )";
		db.execSQL(CREATE_BOOK_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// drop books table if already exists
		db.execSQL("DROP TABLE IF EXISTS "+table_USER);
		this.onCreate(db);
	}

	public void createBook(TableUsuario tblusuario) {
		// get reference of the BookDB database
		SQLiteDatabase db = this.getWritableDatabase();

		// make values to be inserted
		ContentValues values = new ContentValues();
		values.put(user_NAME, tblusuario.getNome());
		values.put(user_EMAIL, tblusuario.getEmail());

		// insert book
		db.insert(table_USER, null, values);

		// close database transaction
		db.close();
	}

	public TableUsuario readUser(int id) {
		// get reference of the BookDB database
		SQLiteDatabase db = this.getReadableDatabase();

		// get book query
		Cursor cursor = db.query(table_USER, // a. table
				COLUMNS, " id = ?", new String[] { String.valueOf(id) }, null, null, null, null);

		// if results !=null, parse the first one
		if (cursor != null)
			cursor.moveToFirst();

		TableUsuario user = new TableUsuario();
		user.setId(Integer.parseInt(cursor.getString(0)));
		user.setNome(cursor.getString(1));
		user.setEmail(cursor.getString(2));

		return user;
	}

	public List getAllUsers() {
		List users = new LinkedList();

		// select book query
		String query = "SELECT  * FROM " + table_USER;

		// get reference of the BookDB database
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// parse all results
		TableUsuario user = null;
		if (cursor.moveToFirst()) {
			do {
				user = new TableUsuario();
				user.setId(Integer.parseInt(cursor.getString(0)));
				user.setNome(cursor.getString(1));
				user.setEmail(cursor.getString(2));

				// Add book to books
				users.add(user);
			} while (cursor.moveToNext());
		}
		return users;
	}

	public int updateBook(TableUsuario user) {

		// get reference of the BookDB database
		SQLiteDatabase db = this.getWritableDatabase();

		// make values to be inserted
		ContentValues values = new ContentValues();
		values.put(user_NAME, user.getNome());
		values.put(user_EMAIL, user.getEmail());

		// update
		int i = db.update(table_USER, values, user_ID + " = ?", new String[] { String.valueOf(user.getId()) });

		db.close();
		return i;
	}

	// Deleting single book
	public void deleteBook(TableUsuario tbusuario) {

		// get reference of the BookDB database
		SQLiteDatabase db = this.getWritableDatabase();

		// delete book
		db.delete(table_USER, user_ID + " = ?", new String[] { String.valueOf(tbusuario.getId()) });
		db.close();
	}
	
}
