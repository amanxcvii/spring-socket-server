package com.studentdb.jdbcspring.controller;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentdb.jdbcspring.entity.student;
import com.studentdb.jdbcspring.repo.StudentService;

@RestController
@RequestMapping
public class StudentController {
	private final StudentService studentService;
	private static StudentController instance;
	
	StudentController(StudentService studentService) {
		this.studentService = studentService;
	}
	
	public StudentController() {
		this.studentService = new StudentService();
		// TODO Auto-generated constructor stub
	}

	public static StudentController getDefault() {
		// TODO Auto-generated method stub
		if(instance == null) {
			instance = new StudentController(); 
		}
		return instance;
	}

	@GetMapping("/students/get/all")
	public List<student> getAllStudents() {
		try {
	        return studentService.getAllStudents();
//			return ResponseEntity.ok(studentService.getAllStudents());
		} catch (SQLException ex) {
			System.out.println("Error Occured");
			return null;
		}
	}

	public List<student> getAllStudentsAsList() {
		try {
			 return this.studentService.getAllStudents();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public void addStudent(student newStudent) {
		try {
			this.studentService.saveStudent(newStudent);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
