package com.example.gabriela.firecastcommunity.utility;

import com.example.gabriela.firecastcommunity.domain.User;

import java.util.ArrayList;
import java.util.List;

public class Constant {
	public static List<Class> TablesDB() {
		List<Class> classesTabelas = new ArrayList<Class>();
		classesTabelas.add(User.class);
		return classesTabelas;
	}


	public static final String Value = "Value";
	public static final String NAME_DB_FIRECAST = "FirecastCommunityDB";
	
}
