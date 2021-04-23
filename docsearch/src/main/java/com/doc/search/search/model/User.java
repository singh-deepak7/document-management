package com.doc.search.search.model;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = -2031002500412705637L;
	
	private String city;
	private String dept;
	private String name;
	private String id;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	

}
