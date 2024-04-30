package com.studentdb.jdbcspring.messages;

import java.io.Serializable;
import java.util.Vector;

public class serverRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8476201107310431189L;
	private Vector info;
	private String key;
	/**
	 * @return the info
	 */
	public Vector getInfo() {
		return info;
	}
	/**
	 * @param info the info to set
	 */
	public void setInfo(Vector info) {
		this.info = info;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

}
	