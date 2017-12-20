package com.example.gabriela.firecastcommunity.utility;

import com.example.gabriela.firecastcommunity.domain.User;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    public static final int ACTIVITY_OCCURRENCE_TYPES = 50;
	public static final int ACTIVITY_DISTANCE_RADIUS_MAP = 51;
	public static final int ACTIVITY_NOTIFICATION = 52;
	public static final int ACTIVITY_FILTER = 53;
    public static final int NOTHING_CITY_ID = 0;
	public static final int ALL_CITIES_ID = -1;
	public static final String ALL_CITIES_TEXT = "TODAS";

	public static List<Class> TablesDB() {
		List<Class> classesTabelas = new ArrayList<Class>();
		classesTabelas.add(User.class);
		return classesTabelas;
	}


	public static final String Value = "Value";
	public static final String NAME_DB_FIRECAST = "FirecastCommunityDB";
	
}
