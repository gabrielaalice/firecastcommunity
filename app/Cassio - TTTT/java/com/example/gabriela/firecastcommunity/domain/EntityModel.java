package com.example.gabriela.firecastcommunity.domain;

import java.io.Serializable;

public class EntityModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}