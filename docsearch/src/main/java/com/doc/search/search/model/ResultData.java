package com.doc.search.search.model;

import java.io.Serializable;
import java.util.List;

public class ResultData implements Serializable {

	private static final long serialVersionUID = 222136205440266978L;
	private List<User> users;
	private String totalCount;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

}
