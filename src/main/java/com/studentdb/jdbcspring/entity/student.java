package com.studentdb.jdbcspring.entity;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class student implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int studentId;// = 1001;
	private String Name;
	private int Age;
	private String Sex ;
	private String StdClass;
	private String Fname;
	private String Mname;
	private String Address; 
	
	/**
	 * @return the studentId
	 */
	public int getStudentId() {
		return studentId;
	}
	/**
	 * @param studentId the studentId to set
	 */
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}
	/**
	 * @return the age
	 */
	public int getAge() {
		return Age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		Age = age;
	}
	/**
	 * @return the sex
	 */
	public String getSex() {
		return Sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		Sex = sex;
	}
	/**
	 * @return the stdClass
	 */
	public String getStdClass() {
		return StdClass;
	}
	/**
	 * @param stdClass the stdClass to set
	 */
	public void setStdClass(String stdClass) {
		StdClass = stdClass;
	}
	/**
	 * @return the fname
	 */
	public String getFname() {
		return Fname;
	}
	/**
	 * @param fname the fname to set
	 */
	public void setFname(String fname) {
		Fname = fname;
	}
	/**
	 * @return the mname
	 */
	public String getMname() {
		return Mname;
	}
	/**
	 * @param mname the mname to set
	 */
	public void setMname(String mname) {
		Mname = mname;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return Address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		Address = address;
	}

	
}
