package com.studentdb.jdbcspring.repo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.studentdb.jdbcspring.entity.student;

@Component
@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;

	public List<student> getAllStudents() throws SQLException {
		StudentRepository studentRepository = new StudentRepository();
		return studentRepository.getAllData();
	}



//	    public List<student> getAllStudents() {
//	        return studentRepository.findAll();
//	    }

//	    public student getStudentById(Long id) {
//	        return studentRepository.findById(id).orElse(null);
//	    }

	public void saveStudent(student student) throws SQLException {
		StudentRepository studentRepository = new StudentRepository();
		studentRepository.save(student);
	}

//	    public void deleteStudent(Long id) {
//	        studentRepository.deleteById(id);
//	    }

}
