package com.example.gabriela.firecastcommunity.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gabriela.firecastcommunity.domain.City;
import com.example.gabriela.firecastcommunity.domain.OccurrenceType;
import com.example.gabriela.firecastcommunity.domain.RadioCity;
import com.example.gabriela.firecastcommunity.domain.User;

public class FirecastDB {

	private SQLiteDatabase db;

	private static final String DB_NAME = "firecastComunityDB.db";

	// 0 -> Mode Private
	private static final int DB_ACESS_TYPE = 0;

	private final String TABLE_CREATE_USER = ""
			+ "CREATE TABLE IF NOT EXISTS "+new User().getClass().getSimpleName()+" ("
			+ "id integer primary key autoincrement not null,"
			+ "radiusKilometers integer), "
			+ "id_city_occurrence integer, "
			+ "id_city_radio integer, "
			+ "name varchar(45),"
			+ "email varchar(50),"
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

	private final String SQL_SELECT_ALL(Class clazz) {
		return "SELECT * FROM "+ clazz.getClass().getSimpleName() + ";";
	}

    private final String SQL_SELECT_ALL_WHERE(Class clazz, String fieldWhere, int id_value) {
        return "SELECT * FROM "+ clazz.getClass().getSimpleName() + " where "+ fieldWhere + " = "+ id_value + ";";
    }

	private Cursor cursor;

	public FirecastDB(Context context) {
		this.db = context.openOrCreateDatabase(DB_NAME, DB_ACESS_TYPE,
				null);
		this.db.execSQL(TABLE_CREATE_USER);
		this.db.execSQL(TABLE_CREATE_TYPE_OCCURRENCE);
		this.db.execSQL(TABLE_CREATE_TYPE_OCCURRENCE_USER);
		if(ListAllUser().size() == 0) {
            this.db.execSQL(TABLE_INSERT_TYPE_OCCURRENCE());
        }
	}

	public boolean SaveOrUpdate(User user){
        if (user.getId()>0){
            return UpdateUser(user);
        }

        return InsertUser(user);
    }

	public boolean InsertUser(User user) {
		long res = this.db.insert(user.getClass().getSimpleName(), null, retornarContentValues(user));
        boolean b = res != -1;
        if(b){
            for (OccurrenceType ot:user.getOccurrenceTypes()) {
                b = Insert_User_OccurrenceType(user,ot);
                if(!b){ break; }
            }
        }
		return b;
	}

	public boolean DeleteUser(User user) {
		String[] argumentos = new String[] { String.valueOf(user.getId()) };
		int res = this.db.delete(user.getClass().getSimpleName(), "id=?", argumentos);
		return res != -1;
	}

	public boolean UpdateUser(User user) {
		String[] argumentos = new String[] { String.valueOf(user.getId()) };
		int res = this.db.update(user.getClass().getSimpleName(), retornarContentValues(user), "id=?", argumentos);
        boolean b = res != -1;
        if(b){
            for (OccurrenceType ot : user.getOccurrenceTypes()) {
                b = Update_User_OccurrenceType(user,ot);
                if(!b){ break; }
            }
        }

		return b;
	}

	public ArrayList<User> ListAllUser() {
		ArrayList<User> list = new ArrayList<>();
		this.cursor = this.db.rawQuery(SQL_SELECT_ALL(User.class), null);
		while (this.cursor.moveToNext()) {
			User user = new User();
            user.setId(cursor.getInt(this.cursor.getColumnIndex("id")));
            user.setRadiusKilometers(cursor.getInt(this.cursor.getColumnIndex("radiusKilometers")));
            user.setCity_occurrence(getCity(cursor.getInt(this.cursor.getColumnIndex("id_city_occurrence"))));
            user.setRadio_city(getRadioCity(cursor.getInt(this.cursor.getColumnIndex("id_city_radio"))));
            user.setName(cursor.getString(this.cursor.getColumnIndex("name")));
            user.setEmail(cursor.getString(this.cursor.getColumnIndex("email")));
            user.setPassword(cursor.getString(this.cursor.getColumnIndex("password")));
            user.setOccurrenceTypes(ListAllUser_TypeOccurrence(user.getId()));
			list.add(user);
		}
		return list;
	}

    private RadioCity getRadioCity(int id_city_radio) {
        return null;
    }

    private City getCity(int id_city_occurrence) {
        List<City> cities = DataBaseTemp.cities();
        for (City c :cities) {
            if(c.getId()==id_city_occurrence){
                return c;
            }
        }
        return null;
    }

    public boolean Insert_User_OccurrenceType(User user, OccurrenceType ot) {
        long res = this.db.insert("user_occurrencetype", null, retornarContentValues(user, ot));
        return res != -1;
    }

    public boolean Update_User_OccurrenceType(User user, OccurrenceType ot) {
        String[] argumentos = new String[] { String.valueOf(user.getId()) };
        int res = this.db.update("user_occurrencetype", retornarContentValues(user, ot), "id=?", argumentos);
        return res != -1;
    }

    private List<OccurrenceType> ListAllUser_TypeOccurrence(int id_user) {
        ArrayList<OccurrenceType> list = new ArrayList<>();
        this.cursor = this.db.rawQuery(SQL_SELECT_ALL_WHERE(OccurrenceType.class, "id_user", id_user), null);
        while (this.cursor.moveToNext()) {
            OccurrenceType ot = new OccurrenceType();
            ot.setId(cursor.getInt(this.cursor.getColumnIndex("id")));
            ot.name = cursor.getString(this.cursor.getColumnIndex("name"));
            list.add(ot);
        }
        return list;
    }

    public ContentValues retornarContentValues(User user) {
		ContentValues values = new ContentValues();
		values.put("id", user.getId());
		values.put("radiusKilometers", user.getRadiusKilometers());
		values.put("id_city_occurrence", user.getCity_occurrence().getId());
		values.put("id_city_radio", user.getRadio_city().getId());
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
		return values;
	}

    public ContentValues retornarContentValues(User user, OccurrenceType ot) {
        ContentValues values = new ContentValues();
        values.put("id_user", user.getId());
        values.put("id_occurrencetype", ot.getId());
        return values;
    }
}
