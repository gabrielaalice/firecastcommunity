package com.example.gabriela.firecastcommunity.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gabriela.firecastcommunity.domain.City;
import com.example.gabriela.firecastcommunity.domain.OccurrenceType;
import com.example.gabriela.firecastcommunity.domain.RadioCity;
import com.example.gabriela.firecastcommunity.domain.User;

import static br.com.zbra.androidlinq.Linq.stream;

public class FirecastDB {

	private SQLiteDatabase db;

	private static final String DB_NAME = "firecastComunityDB.db";

	// 0 -> Mode Private
	private static final int DB_ACESS_TYPE = 0;

	private final String TABLE_CREATE_USER = ""
			+ "CREATE TABLE IF NOT EXISTS "+ new User().getClass().getSimpleName()+" ("
			+ "id integer not null primary key AUTOINCREMENT,"
			+ "radiusKilometers integer, "
			+ "id_city_occurrence integer, "
			+ "id_city_radio integer, "
			+ "name varchar(45), "
			+ "email varchar(50), "
			+ "password varchar(100), " +
            "isNotify boolean, " +
            "isSoundNotify boolean, " +
            "isVibrateNotify boolean, " +
			"isSilenceNotify boolean, " +
			"silenceStartNotify DateTime, " +
			"silenceFinishNotify DateTime)";

	private final String TABLE_CREATE_TYPE_OCCURRENCE = ""
			+ "CREATE TABLE IF NOT EXISTS "
			+ new OccurrenceType(0,"").getClass().getSimpleName()
			+ "(id int not null primary key, "
			+ "name varchar(40) not null)";

	private final String TABLE_CREATE_TYPE_OCCURRENCE_USER = ""
			+ "CREATE TABLE IF NOT EXISTS user_occurrencetype (" +
			"  `id_user` INT NOT NULL, " +
			"  `id_occurrencetype` INT NOT NULL);";

	private final String TABLE_INSERT_TYPE_OCCURRENCE() {
		OccurrenceType ot = new OccurrenceType(0,"");
		return "INSERT INTO " + ot.getClass().getSimpleName() + "(id, name) VALUES(8, 'ACIDENTE DE TRÂNSITO')," +
                "(5, 'ATENDIMENTO PRÉ-HOSPITALAR'), " +
                "(2, 'AUXÍLIOS / APOIOS'), " +
                "(10,'AVERIGUAÇÃO / CORTE DE ÁRVORE'), " +
                "(11,'AVERIGUAÇÃO / MANEJO DE INSETO'), " +
                "(9, 'AÇÕES PREVENTIVAS'), " +
                "(7, 'DIVERSOS'), " +
                "(1, 'INCÊNDIO'), " +
                "(6, 'OCORRÊNCIA NÃO ATENDIDA'), " +
                "(3, 'PRODUTOS PERIGOSOS'), " +
                "(4, 'SALVAMENTO / BUSCA / RESGATE');";
	}

	private final String SQL_SELECT_ALL(String clazz) {
        return "SELECT * FROM " + clazz;
	}

	private final String SQL_SELECT_ALL_WHERE(String clazz, String fieldWhere, int id_value) {
		return "SELECT * FROM "+ clazz + " where "+ fieldWhere + " = "+ id_value + ";";
	}

	public FirecastDB(Context context) {
		this.db = context.openOrCreateDatabase(DB_NAME, DB_ACESS_TYPE,
				null);
		this.db.execSQL(TABLE_CREATE_USER);
		this.db.execSQL(TABLE_CREATE_TYPE_OCCURRENCE);
		this.db.execSQL(TABLE_CREATE_TYPE_OCCURRENCE_USER);
		if(ListAllOccurrenceType().size() == 0) {
			this.db.execSQL(TABLE_INSERT_TYPE_OCCURRENCE());
		}
	}

	public boolean SaveOrUpdate(User user){
		if(user.getId()==null){
			return InsertUser(user);
		}
		return UpdateUser(user);
	}

    public boolean InsertUser(User user) {
		long res = this.db.insert(user.getClass().getSimpleName(), null, retornarContentValues(user));

		List<OccurrenceType> occurrenceTypeList =user.getOccurrenceTypes();

		user = getUser();
		boolean b = res != -1;
		if(b){
			for (OccurrenceType ot : occurrenceTypeList) {
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
	    Delete_List_User_OccurrenceType(user);

		String[] argumentos = new String[] { String.valueOf(user.getId()) };
		int res = this.db.update(user.getClass().getSimpleName(), retornarContentValues(user), "id=?", argumentos);

		boolean b = res != -1;
		if(b){
			for (OccurrenceType ot : user.getOccurrenceTypes()) {
				b = Insert_User_OccurrenceType(user,ot);
				if(!b){ break; }
			}
		}

		return b;
	}

	public ArrayList<User> ListAllUser() {
		ArrayList<User> list = new ArrayList<>();
        Cursor cursor = this.db.rawQuery(SQL_SELECT_ALL("User"), null);
		while (cursor.moveToNext()) {
			User user = new User();
			user.setId(cursor.getInt(cursor.getColumnIndex("id")));
			user.setRadiusKilometers(cursor.getInt(cursor.getColumnIndex("radiusKilometers")));
			user.setId_city_occurrence(cursor.getInt(cursor.getColumnIndex("id_city_occurrence")));
			user.setId_city_radio(cursor.getInt(cursor.getColumnIndex("id_city_radio")));
			user.setName(cursor.getString(cursor.getColumnIndex("name")));
			user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
			user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
			user.setNotify(cursor.getInt(cursor.getColumnIndex("isNotify")) == 1);
            user.setSound(cursor.getInt(cursor.getColumnIndex("isSoundNotify")) == 1);
            user.setVibrate(cursor.getInt(cursor.getColumnIndex("isVibrateNotify")) == 1);
			user.setTimeSilence(cursor.getInt(cursor.getColumnIndex("isSilenceNotify")) == 1);
			user.setTimeStartSilence(ConvertStringInDate(cursor.getString(cursor.getColumnIndex("silenceStartNotify"))));
			user.setTimeFinishSilence(ConvertStringInDate(cursor.getString(cursor.getColumnIndex("silenceFinishNotify"))));
			user.setOccurrenceTypes(ListAllUser_TypeOccurrence(user.getId()));
			list.add(user);
		}
		return list;
	}

    private Date ConvertStringInDate(String value) {
        if(value == null){
            return null;
        }

        String expectedPattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
        try
        {
            Date date = formatter.parse(value);
            return date;
        }
        catch (ParseException e) {
            return null;
        }
    }

    public ArrayList<OccurrenceType> ListAllOccurrenceType() {
        ArrayList<OccurrenceType> list = new ArrayList<>();
        Cursor cursor = this.db.rawQuery(SQL_SELECT_ALL("OccurrenceType"), null);
        while (cursor.moveToNext()) {
            OccurrenceType ot = new OccurrenceType(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")));
            list.add(ot);
        }
        return list;
    }

	private RadioCity getRadioCity(int id_city_radio) {
		return null;
	}

	private City getCity(int id_city_occurrence) {
		List<City> cities = DataBaseTemp.cities();
		for (City c :cities) {
			if(c.id==id_city_occurrence){
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
		String[] argumentos = new String[] { String.valueOf(user.getId()), String.valueOf(ot.id) };
		int res = this.db.update("user_occurrencetype", retornarContentValues(user, ot),
                "id_user = ? AND id_occurrencetype = ?",
                argumentos);
		return res != -1;
	}

    public boolean Delete_User_OccurrenceType(User user, OccurrenceType ot) {
        String[] argumentos = new String[] { String.valueOf(user.getId()), String.valueOf(ot.id) };
        int res = this.db.delete("user_occurrencetype",
                "id_user = ? AND id_occurrencetype = ?",
                argumentos);
        return res != -1;
    }

    public boolean Delete_List_User_OccurrenceType(User user) {
        boolean b = true;
	    for (OccurrenceType ot:user.getOccurrenceTypes()) {
	        b = Delete_User_OccurrenceType(user,ot);
	        if(!b){ break; }
	    }
	    return b;
    }

	private List<OccurrenceType> ListAllUser_TypeOccurrence(int id_user) {
		ArrayList<OccurrenceType> list = new ArrayList<>();
		Cursor cursorUot = this.db.rawQuery(SQL_SELECT_ALL_WHERE("user_occurrencetype", "id_user", id_user), null);
		while (cursorUot.moveToNext()) {

		    int id_tipo = cursorUot.getInt(cursorUot.getColumnIndex("id_occurrencetype"));

		    Cursor cursorOT = this.db.rawQuery(SQL_SELECT_ALL_WHERE("OccurrenceType",
                    "id",
                    id_tipo),
                    null);

            while (cursorOT.moveToNext()) {
                int id = cursorOT.getInt(cursorOT.getColumnIndex("id"));
                String name = cursorOT.getString(cursorOT.getColumnIndex("name"));

                OccurrenceType ot = new OccurrenceType(id, name);
                list.add(ot);
            }
		}
		return list;
	}

	public ContentValues retornarContentValues(User user) {
		ContentValues values = new ContentValues();
		values.put("id", user.getId());
		values.put("radiusKilometers", user.getRadiusKilometers());
		values.put("id_city_occurrence", user.getId_city_occurrence());
		values.put("id_city_radio", user.getId_city_radio());
		values.put("name", user.getName());
		values.put("email", user.getEmail());
		values.put("password", user.getPassword());
		values.put("isNotify",user.isNotify());
        values.put("isSoundNotify",user.isSound());
        values.put("isVibrateNotify",user.isVibrate());
        values.put("isSilenceNotify",user.isTimeSilence());
        values.put("silenceStartNotify",ConvertDateInString(user.getTimeStartSilence()));
        values.put("silenceFinishNotify",ConvertDateInString(user.getTimeFinishSilence()));
		return values;
	}

    private String ConvertDateInString(Date value) {
	    if(value == null){
	        return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(value);
        return date;
    }

    public ContentValues retornarContentValues(User user, OccurrenceType ot) {
		ContentValues values = new ContentValues();
		values.put("id_user", user.getId());
		values.put("id_occurrencetype", ot.id);
		return values;
	}

    public User getUser() {
	    return stream(ListAllUser()).firstOrNull();
    }
}