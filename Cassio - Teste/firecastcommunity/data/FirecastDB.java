package com.example.gabriela.firecastcommunity.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gabriela.firecastcommunity.domain.OccurrenceType;
import com.example.gabriela.firecastcommunity.domain.User;

public class FirecastDB {

	private SQLiteDatabase db;

	private static final String DB_NAME = "firecastComunityDB.db";

	// 0 -> Mode Private
	private static final int DB_ACESS_TYPE = 0;

	// SQL para cria��o da tabel
	private final String TABLE_CREATE_USER = ""
			+ "CREATE TABLE IF NOT EXISTS "+new User().getClass().getSimpleName()+" ("
			+ "id integer primary key autoincrement not null,"
			+ "radiusKilometers integer), "
			+ "id_city_occurrence integer, "
			+ "id_city_radio integer, "
			+ "nome varchar(45),"
			+ "login varchar(15),"
			+ "password varchar(100))";

	private final String TABLE_CREATE_TYPE_OCCURRENCE = ""
			+ "CREATE TABLE IF NOT EXISTS "
            + new OccurrenceType(0,"").getClass().getSimpleName()
            + "(id int not null primary key,"
            + "name varchar(40) not null)";

	private final String TABLE_CREATE_TYPE_OCCURRENCE_USER = ""
			+ "CREATE TABLE IF NOT EXISTS user_occurrencetype (" +
            "  `id_user` INT NOT NULL," +
            "  `id_occurrencetype` INT NOT NULL," +
            "  PRIMARY KEY (`id_user`, `id_occurrencetype`)";

	private final String TABLE_INSERT_TYPE_OCCURRENCE() {
        OccurrenceType ot = new OccurrenceType(0,"");
        return "INSERT INTO " + ot.getClass().getSimpleName() + "(id, name) VALUES(8, 'ACIDENTE DE TRÂNSITO'); " +
                "INSERT INTO " + ot.getClass().getSimpleName() + "(id, name) VALUES(5, 'ATENDIMENTO PRÉ-HOSPITALAR'); " +
                "INSERT INTO " + ot.getClass().getSimpleName() + "(id, name) VALUES(2, 'AUXÍLIOS / APOIOS'); " +
                "INSERT INTO " + ot.getClass().getSimpleName() + "(id, name) VALUES(10,'AVERIGUAÇÃO / CORTE DE ÁRVORE'); " +
                "INSERT INTO " + ot.getClass().getSimpleName() + "(id, name) VALUES(11,'AVERIGUAÇÃO / MANEJO DE INSETO'); " +
                "INSERT INTO " + ot.getClass().getSimpleName() + "(id, name) VALUES(9, 'AÇÕES PREVENTIVAS'); " +
                "INSERT INTO " + ot.getClass().getSimpleName() + "(id, name) VALUES(7, 'DIVERSOS'); " +
                "INSERT INTO " + ot.getClass().getSimpleName() + "(id, name) VALUES(1, 'INCÊNDIO'); " +
                "INSERT INTO " + ot.getClass().getSimpleName() + "(id, name) VALUES(6, 'OCORRÊNCIA NÃO ATENDIDA'); " +
                "INSERT INTO " + ot.getClass().getSimpleName() + "(id, name) VALUES(3, 'PRODUTOS PERIGOSOS'); " +
                "INSERT INTO " + ot.getClass().getSimpleName() + "(id, name) VALUES(4, 'SALVAMENTO / BUSCA / RESGATE');";
    }

//    private int COUNT_TABLE_TYPE_OCCURRENCE(){
//        this.cursor = this.db.rawQuery(SQL_SELECT_ALL, null);
//        while (this.cursor.moveToNext()) {
//	    return "Select count(*) from "+new OccurrenceType(0,"").getClass().getSimpleName();
//    }


	// SQL para selecionar todas viagens
	private final String SQL_SELECT_ALL(Class clazz) {
		return "SELECT * FROM "+ clazz.getClass().getSimpleName();
	}

	// Variavel para armazenar resultados de selects
	private Cursor cursor;

	/**
	 * Classe respons�vel pela conex�o com o banco de dados
	 */
	public FirecastDB(Context context) {
		this.db = context.openOrCreateDatabase(DB_NAME, DB_ACESS_TYPE,
				null);
		this.db.execSQL(TABLE_CREATE_USER);
		this.db.execSQL(TABLE_CREATE_TYPE_OCCURRENCE);
		this.db.execSQL(TABLE_CREATE_TYPE_OCCURRENCE_USER);
//		if(this.db.execSQL(COUNT_TABLE_TYPE_OCCURRENCE()) > 0) {
//            this.db.execSQL(TABLE_INSERT_TYPE_OCCURRENCE());
//        }
	}

	public boolean Insert(User user) {
		long res = this.db.insert(user.getClass().getSimpleName(), null,
				retornarContentValues(user));
		return res != -1;
	}

	public boolean Delete(User user) {
		String[] argumentos = new String[] { String.valueOf(user.getId()) };
		int res = this.db.delete(user.getClass().getSimpleName(), "id=?", argumentos);
		return res != -1;
	}

	public boolean Update(User user) {
		String[] argumentos = new String[] { String.valueOf(user.getId()) };
		int res = this.db.update(user.getClass().getSimpleName(),
				retornarContentValues(user), "id=?", argumentos);
		return res != -1;
	}

	public ArrayList<User> ListAll(Context context) {
		ArrayList<User> list = new ArrayList<User>();
		// Executa o sql select_all
		this.cursor = this.db.rawQuery(SQL_SELECT_ALL(new User().getClass()), null);
		while (this.cursor.moveToNext()) {
			User viagem = new User();
//			viagem.setId(cursor.getInt(this.cursor.getColumnIndex("id")));
//			viagem.setEhUmPagamento(this.cursor.getInt(this.cursor
//					.getColumnIndex("ehUmPagamento")) == 1);
//			viagem.setValor(this.cursor.getDouble(this.cursor
//					.getColumnIndex("valor")));
//			viagem.setPraQuem(this.cursor.getString(this.cursor
//					.getColumnIndex("praQuem")));
//			viagem.setData(ConverterStringEmData(this.cursor
//					.getString(this.cursor.getColumnIndex("data"))));
			list.add(viagem);
		}
		return list;
	}

	public ContentValues retornarContentValues(User viagem) {
		ContentValues values = new ContentValues();
//		values.put("ehUmPagamento", viagem.isEhUmPagamento());
//		values.put("valor", viagem.getValor());
//		values.put("praQuem", viagem.getPraQuem());
//		values.put("data", ConverterDataEmString(viagem.getData()));
		return values;
	}
}
